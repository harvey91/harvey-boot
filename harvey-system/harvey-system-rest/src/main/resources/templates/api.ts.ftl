import request from "@/utils/request";

const BASE_URL = "/${moduleName}/${businessModule}";

const ${entityName}API = {
  /** 分页查询 */
  getPage(queryParams: ${entityName}PageQuery) {
    return request<any, PageResult<${entityName}PageVO[]>>({
      url: `${BASE_URL}/page`,
      method: "get",
      params: queryParams,
    });
  },

  /** id查询表单 */
  getFormData(id: number) {
    return request<any, ${entityName}Form>({
      url: `${BASE_URL}/form/${id}`,
      method: "get",
    });
  },

  /** 新增 */
  create(data: ${entityName}Form) {
    return request({
      url: `${BASE_URL}/create`,
      method: "post",
      data,
    });
  },

  /** 修改 */
  modify(data: ${entityName}Form) {
    return request({
      url: `${BASE_URL}/modify`,
      method: "put",
      data,
    });
  },

  /** 删除 */
  deleteByIds(ids: number[]) {
    return request({
      url: `${BASE_URL}/delete`,
      method: "delete",
      data: ids,
    });
  },
};

export default ${entityName}API;

/** ${tableComment}分页查询参数 */
export interface ${entityName}PageQuery extends PageQuery {
<#list queryColumns as column>
  /** ${column.fieldComment} */
  ${column.fieldName}?: ${column.tsType};
<#if column.isDateRange>
  /** ${column.fieldComment}开始时间 */
  begin${column.fieldNameCap}?: string;
  /** ${column.fieldComment}结束时间 */
  end${column.fieldNameCap}?: string;
</#if>
</#list>
}

/** ${tableComment}分页对象 */
export interface ${entityName}PageVO {
<#list listColumns as column>
  /** ${column.fieldComment} */
  ${column.fieldName}?: ${column.tsType};
</#list>
  /** 创建时间 */
  createTime?: string;
}

/** ${tableComment}表单 */
export interface ${entityName}Form {
  /** 主键id */
  id?: number;
<#list formColumns as column>
  /** ${column.fieldComment} */
  ${column.fieldName}?: ${column.tsType};
</#list>
}
