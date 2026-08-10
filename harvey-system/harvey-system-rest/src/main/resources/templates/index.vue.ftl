<!-- ${tableComment} -->
<template>
  <div class="app-container">
    <div class="search-bar">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
<#list queryColumns as column>
<#if column.isDateRange>
        <el-form-item label="${column.fieldComment}" prop="begin${column.fieldNameCap}">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            @change="handleDateRangeChange"
          />
        </el-form-item>
<#else>
        <el-form-item label="${column.fieldComment}" prop="${column.fieldName}">
          <el-input
            v-model="queryParams.${column.fieldName}"
            placeholder="请输入${column.fieldComment}"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
</#if>
</#list>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <template #icon>
              <Search />
            </template>
            搜索
          </el-button>
          <el-button @click="handleResetQuery">
            <template #icon>
              <Refresh />
            </template>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-wrapper">
      <template #header>
        <el-button
          v-hasPerm="['${permissionPrefix}:create']"
          type="success"
          @click="handleOpenDialog()"
        >
          <template #icon>
            <Plus />
          </template>
          新增
        </el-button>
        <el-button
          v-hasPerm="['${permissionPrefix}:delete']"
          type="danger"
          :disabled="ids.length === 0"
          @click="handleDelete()"
        >
          <template #icon>
            <Delete />
          </template>
          删除
        </el-button>
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="pageData"
        highlight-current-row
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
<#list listColumns as column>
<#if column.isPk>
        <el-table-column type="index" label="序号" width="60" />
</#if>
</#list>
<#list listColumns as column>
<#if !column.isPk>
        <el-table-column
          key="${column.fieldName}"
          label="${column.fieldComment}"
          prop="${column.fieldName}"
          min-width="120"
        />
</#if>
</#list>
        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button
              v-hasPerm="['${permissionPrefix}:modify']"
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row.id)"
            >
              <template #icon>
                <Edit />
              </template>
              编辑
            </el-button>
            <el-button
              v-hasPerm="['${permissionPrefix}:delete']"
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row.id)"
            >
              <template #icon>
                <Delete />
              </template>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="handleQuery"
      />
    </el-card>

    <!-- ${tableComment}表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="600px"
      @close="handleCloseDialog"
    >
      <el-form
        ref="dataFormRef"
        :model="formData"
        :rules="rules"
        label-suffix=":"
        label-width="110px"
      >
<#list formColumns as column>
        <el-form-item
          label="${column.fieldComment}"
          <#if column.isRequired>prop="${column.fieldName}"</#if>
        >
<#if column.formType == 2 && column.dictType != "">
          <el-select
            v-model="formData.${column.fieldName}"
            placeholder="请选择${column.fieldComment}"
            style="width: 100%"
          >
            <el-option
              v-for="item in dictOptions('${column.dictType}')"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
<#elseif column.formType == 3>
          <el-radio-group v-model="formData.${column.fieldName}">
            <el-radio :value="1">是</el-radio>
            <el-radio :value="0">否</el-radio>
          </el-radio-group>
<#elseif column.formType == 5>
          <el-input-number
            v-model="formData.${column.fieldName}"
            placeholder="请输入${column.fieldComment}"
            style="width: 100%"
          />
<#elseif column.formType == 6>
          <el-switch v-model="formData.${column.fieldName}" :active-value="1" :inactive-value="0" />
<#elseif column.formType == 7>
          <el-input
            v-model="formData.${column.fieldName}"
            :rows="3"
            type="textarea"
            placeholder="请输入${column.fieldComment}"
          />
<#elseif column.formType == 8>
          <el-date-picker
            v-model="formData.${column.fieldName}"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择${column.fieldComment}"
            style="width: 100%"
          />
<#elseif column.formType == 9>
          <el-date-picker
            v-model="formData.${column.fieldName}"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择${column.fieldComment}"
            style="width: 100%"
          />
<#else>
          <el-input
            v-model="formData.${column.fieldName}"
            placeholder="请输入${column.fieldComment}"
            :maxlength="<#if column.maxLength &gt; 0>${column.maxLength}<#else>100</#if>"
          />
</#if>
        </el-form-item>
</#list>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit">确定</el-button>
          <el-button @click="handleCloseDialog">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "${entityName}",
  inheritAttrs: false,
});

import ${entityName}API, {
  ${entityName}PageVO,
  ${entityName}Form,
  ${entityName}PageQuery,
} from "@/api/${moduleName}/${businessModule}";
import DictAPI from "@/api/system/dict";

const queryFormRef = ref(ElForm);
const dataFormRef = ref(ElForm);

const loading = ref(false);
const ids = ref<number[]>([]);
const total = ref(0);

const queryParams = reactive<${entityName}PageQuery>({
  pageNum: 1,
  pageSize: 10,
});

const pageData = ref<${entityName}PageVO[]>([]);

const dialog = reactive({
  title: "",
  visible: false,
});

const formData = reactive<${entityName}Form>({
  id: undefined,
});

const rules = reactive({
<#list formColumns as column>
<#if column.isRequired>
  ${column.fieldName}: [
    { required: true, message: "请输入${column.fieldComment}", trigger: "blur" },
  ],
</#if>
</#list>
});

const dateRange = ref<[string, string] | null>(null);

/** 字典数据 */
const dictMap = ref<Record<string, OptionType[]>>({});
function dictOptions(dictCode: string) {
  return dictMap.value[dictCode] || [];
}

/** 日期范围查询 */
function handleDateRangeChange(val: [string, string] | null) {
<#list queryColumns as column>
<#if column.isDateRange>
  queryParams.begin${column.fieldNameCap} = val?.[0];
  queryParams.end${column.fieldNameCap} = val?.[1];
</#if>
</#list>
}

/** 查询 */
function handleQuery() {
  loading.value = true;
  ${entityName}API.getPage(queryParams)
    .then((data) => {
      pageData.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 重置查询 */
function handleResetQuery() {
  queryFormRef.value.resetFields();
  dateRange.value = null;
  queryParams.pageNum = 1;
  handleQuery();
}

/** 行复选框选中记录选中ID集合 */
function handleSelectionChange(selection: any) {
  ids.value = selection.map((item: any) => item.id);
}

/** 打开弹窗 */
function handleOpenDialog(id?: number) {
  dialog.visible = true;
  if (id) {
    dialog.title = "修改${tableComment}";
    ${entityName}API.getFormData(id).then((data) => {
      Object.assign(formData, data);
    });
  } else {
    dialog.title = "新增${tableComment}";
    formData.id = undefined;
  }
}

/** 提交表单 */
function handleSubmit() {
  dataFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const id = formData.id;
      if (id) {
        ${entityName}API.modify(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        ${entityName}API.create(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 关闭弹窗 */
function handleCloseDialog() {
  dialog.visible = false;
  dataFormRef.value.resetFields();
  dataFormRef.value.clearValidate();
}

/** 删除 */
function handleDelete(id?: number) {
  const dataIds = id ? [id] : ids.value;
  if (dataIds.length === 0) {
    ElMessage.warning("请勾选删除项");
    return;
  }
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      ${entityName}API.deleteByIds(dataIds)
        .then(() => {
          ElMessage.success("删除成功");
          handleResetQuery();
        })
        .finally(() => (loading.value = false));
    },
    () => {}
  );
}

onMounted(() => {
  handleQuery();
  DictAPI.getList().then((data) => {
    dictMap.value = {};
    data.forEach((item) => {
      dictMap.value[item.dictCode] = (item.dictDataList || []).map(
        (it: any) => ({ label: it.label, value: it.value })
      );
    });
  });
});
</script>
