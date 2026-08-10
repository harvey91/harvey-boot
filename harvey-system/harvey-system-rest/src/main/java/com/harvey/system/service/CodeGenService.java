package com.harvey.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.model.query.Query;
import com.harvey.core.model.PageResult;
import com.harvey.system.model.db.ColumnInfoVO;
import com.harvey.system.model.db.GeneratorPreviewVO;
import com.harvey.system.model.db.TablePageVO;
import com.harvey.system.model.dto.GenConfigDto;
import com.harvey.system.model.dto.GenFieldConfigDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成服务：表元数据读取 + 模板渲染 + 打包下载
 *
 * @author harvey
 * @since 2026-08-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGenService {

    private final DbManageService dbManageService;
    private final GenConfigService genConfigService;

    private Configuration freemarkerConfiguration;

    private static final Set<String> BASE_COLUMNS = Set.of(
            "id", "remark", "sort", "enabled", "create_time", "update_time", "deleted");

    private static final List<String> TABLE_PREFIXES = List.of("t_", "c_", "sys_", "biz_");

    @PostConstruct
    public void init() {
        freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_32);
        freemarkerConfiguration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setNumberFormat("computer");
    }

    /* ============================== 表分页 ============================== */

    /**
     * 数据表分页列表
     */
    public PageResult<TablePageVO> getTablePage(Query query) {
        String database = dbManageService.currentDatabase();
        List<com.harvey.system.model.db.TableInfoVO> tables = dbManageService.listTables(database, query.getKeywords());
        List<TablePageVO> list = new ArrayList<>();
        tables.forEach(table -> {
            TablePageVO vo = new TablePageVO();
            vo.setTableName(table.getTableName());
            vo.setTableComment(table.getTableComment());
            vo.setEngine(table.getEngine());
            vo.setTableCollation(table.getTableCollation());
            vo.setCreateTime(table.getCreateTime());
            vo.setIsConfigured(genConfigService.existsByTableName(table.getTableName()) ? 1 : 0);
            list.add(vo);
        });
        IPage<TablePageVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        int fromIndex = (int) Math.min((long) (query.getPageNum() - 1) * query.getPageSize(), list.size());
        int toIndex = (int) Math.min(fromIndex + query.getPageSize(), list.size());
        page.setRecords(new ArrayList<>(list.subList(fromIndex, toIndex)));
        page.setTotal(list.size());
        return PageResult.of(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal());
    }

    /* ============================== 配置 ============================== */

    /**
     * 获取生成配置（未配置时根据表结构生成默认配置）
     */
    public GenConfigDto getGenConfig(String tableName) {
        GenConfigDto config = genConfigService.getByTableName(tableName);
        return config != null ? config : buildDefaultConfig(tableName);
    }

    /**
     * 保存生成配置
     */
    public GenConfigDto saveGenConfig(String tableName, GenConfigDto dto) {
        if (dto.getTableName() == null || dto.getTableName().isBlank()) {
            dto.setTableName(tableName);
        }
        return genConfigService.saveConfig(dto);
    }

    /**
     * 重置生成配置
     */
    public void resetGenConfig(String tableName) {
        genConfigService.resetConfig(tableName);
    }

    private GenConfigDto buildDefaultConfig(String tableName) {
        GenConfigDto dto = new GenConfigDto();
        dto.setTableName(tableName);
        String entityName = toUpperCamel(removePrefix(tableName));
        dto.setEntityName(entityName);
        dto.setBusinessName(entityName);
        dto.setModuleName("system");
        dto.setPackageName("com.harvey");
        dto.setAuthor("harvey");
        dto.setBackendAppName("harvey-system");
        dto.setFrontendAppName("harvey-vue3-admin");

        String database = dbManageService.currentDatabase();
        List<ColumnInfoVO> columns = dbManageService.listColumns(database, tableName);
        List<GenFieldConfigDto> fieldConfigs = new ArrayList<>();
        for (ColumnInfoVO column : columns) {
            GenFieldConfigDto field = new GenFieldConfigDto();
            field.setColumnName(column.getColumnName());
            field.setColumnType(column.getColumnType());
            field.setFieldName(toCamel(column.getColumnName()));
            field.setFieldType(javaType(column.getDataType()));
            field.setFieldComment(column.getColumnComment());
            field.setIsShowInList(column.isPk() ? 0 : 1);
            field.setIsShowInForm(isFormColumn(column) ? 1 : 0);
            field.setIsShowInQuery(isQueryColumn(column) ? 1 : 0);
            field.setIsRequired("NO".equals(column.getIsNullable()) && !isSystemFillColumn(column.getColumnName()) ? 1 : 0);
            field.setFormType(formType(column));
            field.setQueryType(queryType(column));
            field.setMaxLength(column.getMaxLength() == null ? 0 : column.getMaxLength().intValue());
            field.setFieldSort(column.getOrdinalPosition() == null ? 0 : column.getOrdinalPosition());
            field.setDictType("");
            fieldConfigs.add(field);
        }
        dto.setFieldConfigs(fieldConfigs);
        return dto;
    }

    private boolean isFormColumn(ColumnInfoVO column) {
        if (column.isPk()) {
            return false;
        }
        if (isSystemFillColumn(column.getColumnName())) {
            return false;
        }
        String dataType = column.getDataType() == null ? "" : column.getDataType().toLowerCase();
        return !dataType.contains("text") && !dataType.contains("blob") && !dataType.contains("json");
    }

    private boolean isQueryColumn(ColumnInfoVO column) {
        if (column.isPk() || isSystemFillColumn(column.getColumnName())) {
            return false;
        }
        String dataType = column.getDataType() == null ? "" : column.getDataType().toLowerCase();
        return dataType.contains("char") || dataType.contains("date") || dataType.contains("time")
                || "int".equals(dataType) || "bigint".equals(dataType);
    }

    private boolean isSystemFillColumn(String columnName) {
        String name = columnName.toLowerCase();
        return "create_time".equals(name) || "update_time".equals(name) || "deleted".equals(name);
    }

    private int formType(ColumnInfoVO column) {
        String dataType = column.getDataType() == null ? "" : column.getDataType().toLowerCase();
        if (dataType.contains("char") || dataType.contains("enum") || dataType.contains("set") || dataType.contains("json")) {
            return 1;
        }
        if (dataType.contains("text")) {
            return 7;
        }
        if (dataType.contains("date") || dataType.contains("time") || dataType.contains("timestamp")) {
            return "date".equals(dataType) ? 8 : 9;
        }
        if (dataType.contains("int") || dataType.contains("decimal") || dataType.contains("double")
                || dataType.contains("float") || dataType.contains("bit") || dataType.contains("numeric")) {
            return 5;
        }
        return 1;
    }

    private int queryType(ColumnInfoVO column) {
        String dataType = column.getDataType() == null ? "" : column.getDataType().toLowerCase();
        if (dataType.contains("date") || dataType.contains("time") || dataType.contains("timestamp")) {
            return 4; // BETWEEN
        }
        if (dataType.contains("char") || dataType.contains("text")) {
            return 2; // LIKE
        }
        return 1; // EQ
    }

    /* ============================== 预览 / 下载 ============================== */

    /**
     * 代码生成预览
     */
    public List<GeneratorPreviewVO> preview(String tableName) {
        GenConfigDto config = getGenConfig(tableName);
        Map<String, Object> model = buildDataModel(config);
        List<GeneratorPreviewVO> files = new ArrayList<>();

        String packagePath = (config.getPackageName() + "." + config.getModuleName()).replace('.', '/');
        String backendBase = "src/main/java/" + config.getBackendAppName() + "/" + packagePath;
        String entity = (String) model.get("entityName");
        String businessModule = (String) model.get("businessModule");
        String backendApp = config.getBackendAppName();
        String frontendApp = config.getFrontendAppName();

        files.add(new GeneratorPreviewVO(backendBase + "/model/entity", entity + ".java",
                render("entity.java.ftl", model)));
        files.add(new GeneratorPreviewVO(backendBase + "/mapper", entity + "Mapper.java",
                render("mapper.java.ftl", model)));
        files.add(new GeneratorPreviewVO("src/main/resources/mapper", entity + "Mapper.xml",
                render("mapper.xml.ftl", model)));
        files.add(new GeneratorPreviewVO(backendBase + "/service", entity + "Service.java",
                render("service.java.ftl", model)));
        files.add(new GeneratorPreviewVO(backendBase + "/service/impl", entity + "ServiceImpl.java",
                render("serviceImpl.java.ftl", model)));

        String apiBase = (frontendApp == null || frontendApp.isBlank() ? "" : frontendApp + "/")
                + "src/api/" + config.getModuleName();
        String viewBase = (frontendApp == null || frontendApp.isBlank() ? "" : frontendApp + "/")
                + "src/views/" + config.getModuleName() + "/" + businessModule;
        files.add(new GeneratorPreviewVO(apiBase, businessModule + ".ts",
                render("api.ts.ftl", model)));
        files.add(new GeneratorPreviewVO(viewBase, "index.vue",
                render("index.vue.ftl", model)));

        if (backendApp == null || backendApp.isBlank()) {
            files.forEach(f -> f.setPath(f.getPath().replace("src/main/java//", "src/main/java/")));
        }
        return files;
    }

    /**
     * 打包下载代码
     */
    public byte[] download(String tableName) {
        List<GeneratorPreviewVO> files = preview(tableName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {
            for (GeneratorPreviewVO file : files) {
                String entryName = file.getPath().replace('\\', '/') + "/" + file.getFileName();
                zos.putNextEntry(new ZipEntry(entryName));
                zos.write(file.getContent().getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        } catch (Exception e) {
            throw new BadParameterException("代码打包失败: " + e.getMessage());
        }
        return baos.toByteArray();
    }

    /* ============================== 数据模型 ============================== */

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildDataModel(GenConfigDto config) {
        Map<String, Object> model = new HashMap<>();
        String tableName = config.getTableName();
        String entityName = config.getEntityName();
        if (entityName == null || entityName.isBlank()) {
            entityName = toUpperCamel(removePrefix(tableName));
        }
        String moduleName = config.getModuleName() == null || config.getModuleName().isBlank() ? "system" : config.getModuleName();
        String packageName = config.getPackageName() == null || config.getPackageName().isBlank() ? "com.harvey" : config.getPackageName();
        String businessModule = toCamel(entityName);
        String permissionPrefix = moduleName + ":" + businessModule;

        model.put("tableName", tableName);
        model.put("tableComment", config.getBusinessName());
        model.put("entityName", entityName);
        model.put("moduleName", moduleName);
        model.put("packageName", packageName);
        model.put("businessName", config.getBusinessName());
        model.put("businessModule", businessModule);
        model.put("permissionPrefix", permissionPrefix);
        model.put("author", config.getAuthor() == null || config.getAuthor().isBlank() ? "harvey" : config.getAuthor());
        model.put("date", LocalDate.now().toString());
        model.put("backendAppName", config.getBackendAppName());
        model.put("frontendAppName", config.getFrontendAppName());

        List<GenFieldConfigDto> fieldConfigs = config.getFieldConfigs();
        boolean extendsBase = true;
        if (fieldConfigs != null) {
            List<String> names = fieldConfigs.stream().map(GenFieldConfigDto::getColumnName)
                    .map(String::toLowerCase).toList();
            boolean hasId = names.contains("id");
            boolean hasCreateTime = names.contains("create_time");
            boolean hasUpdateTime = names.contains("update_time");
            boolean hasDeleted = names.contains("deleted");
            extendsBase = hasId && hasCreateTime && hasUpdateTime && hasDeleted;
        }
        model.put("extendsBaseEntity", extendsBase);

        List<Map<String, Object>> columns = new ArrayList<>();
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        List<Map<String, Object>> listColumns = new ArrayList<>();
        List<Map<String, Object>> formColumns = new ArrayList<>();
        boolean hasBigDecimal = false;
        boolean hasLocalDate = false;
        boolean hasLocalDateTime = false;

        if (fieldConfigs != null) {
            fieldConfigs.sort((a, b) -> {
                int sa = a.getFieldSort() == null ? 0 : a.getFieldSort();
                int sb = b.getFieldSort() == null ? 0 : b.getFieldSort();
                return Integer.compare(sa, sb);
            });
            for (GenFieldConfigDto field : fieldConfigs) {
                String columnName = field.getColumnName();
                boolean isPk = "id".equalsIgnoreCase(columnName);
                if (extendsBase && BASE_COLUMNS.contains(columnName.toLowerCase())) {
                    continue;
                }
                Map<String, Object> col = new LinkedHashMap<>();
                col.put("columnName", columnName);
                col.put("fieldName", field.getFieldName());
                col.put("fieldNameCap", toUpperCamelFirst(field.getFieldName()));
                col.put("fieldComment", field.getFieldComment());
                col.put("fieldType", field.getFieldType());
                col.put("tsType", tsType(field.getFieldType()));
                col.put("formType", field.getFormType() == null ? 1 : field.getFormType());
                col.put("queryType", field.getQueryType() == null ? 1 : field.getQueryType());
                col.put("maxLength", field.getMaxLength() == null ? 0 : field.getMaxLength());
                col.put("isPk", isPk);
                col.put("isRequired", Integer.valueOf(1).equals(field.getIsRequired()));
                col.put("dictType", field.getDictType() == null ? "" : field.getDictType());
                boolean isDate = "LocalDate".equals(field.getFieldType()) || "LocalDateTime".equals(field.getFieldType());
                col.put("isDateRange", isDate && Integer.valueOf(4).equals(field.getQueryType()));
                columns.add(col);

                if (Integer.valueOf(1).equals(field.getIsShowInQuery())) {
                    queryColumns.add(col);
                }
                if (Integer.valueOf(1).equals(field.getIsShowInList())) {
                    listColumns.add(col);
                }
                if (Integer.valueOf(1).equals(field.getIsShowInForm()) && !isPk) {
                    formColumns.add(col);
                }
                String fieldType = field.getFieldType() == null ? "" : field.getFieldType();
                if ("BigDecimal".equals(fieldType)) {
                    hasBigDecimal = true;
                }
                if ("LocalDate".equals(fieldType)) {
                    hasLocalDate = true;
                }
                if ("LocalDateTime".equals(fieldType)) {
                    hasLocalDateTime = true;
                }
            }
        }
        model.put("columns", columns);
        model.put("queryColumns", queryColumns);
        model.put("listColumns", listColumns);
        model.put("formColumns", formColumns);
        model.put("hasBigDecimal", hasBigDecimal);
        model.put("hasLocalDate", hasLocalDate);
        model.put("hasLocalDateTime", hasLocalDateTime);
        return model;
    }

    private String render(String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new BadParameterException("模板渲染失败: " + e.getMessage());
        }
    }

    /* ============================== 工具方法 ============================== */

    private String javaType(String dataType) {
        if (dataType == null) {
            return "String";
        }
        String dt = dataType.toLowerCase();
        if (dt.contains("tinyint") || dt.contains("smallint") || dt.contains("mediumint") || dt.contains("int")) {
            return "Integer";
        }
        if (dt.contains("bigint")) {
            return "Long";
        }
        if (dt.contains("decimal") || dt.contains("numeric")) {
            return "BigDecimal";
        }
        if (dt.contains("float")) {
            return "Float";
        }
        if (dt.contains("double") || dt.contains("real")) {
            return "Double";
        }
        if (dt.contains("bit") || dt.contains("boolean") || dt.contains("bool")) {
            return "Boolean";
        }
        if (dt.contains("date")) {
            return "LocalDate";
        }
        if (dt.contains("time") || dt.contains("timestamp")) {
            return "LocalDateTime";
        }
        if (dt.contains("blob")) {
            return "byte[]";
        }
        return "String";
    }

    private String tsType(String javaType) {
        if (javaType == null) {
            return "string";
        }
        return switch (javaType) {
            case "Integer", "Long", "Float", "Double", "BigDecimal" -> "number";
            case "Boolean" -> "boolean";
            default -> "string";
        };
    }

    private String removePrefix(String tableName) {
        String lower = tableName.toLowerCase();
        for (String prefix : TABLE_PREFIXES) {
            if (lower.startsWith(prefix)) {
                return tableName.substring(prefix.length());
            }
        }
        return tableName;
    }

    private String toUpperCamel(String name) {
        String camel = toCamel(name);
        return camel.isEmpty() ? camel : Character.toUpperCase(camel.charAt(0)) + camel.substring(1);
    }

    private String toUpperCamelFirst(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String toCamel(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        boolean upper = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_' || c == '-' || c == ' ') {
                upper = true;
            } else if (upper) {
                sb.append(Character.toUpperCase(c));
                upper = false;
            } else if (i == 0) {
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
