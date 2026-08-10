package com.harvey.system.service;

import com.harvey.common.exception.BadParameterException;
import com.harvey.system.model.db.ColumnInfoVO;
import com.harvey.system.model.db.SqlResultVO;
import com.harvey.system.model.db.TableDataVO;
import com.harvey.system.model.db.TableInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理服务：表结构、表数据、在线SQL执行
 *
 * @author harvey
 * @since 2026-08-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbManageService {

    private final DataSource dataSource;

    private static final List<String> SYSTEM_SCHEMAS = List.of(
            "information_schema", "mysql", "performance_schema", "sys");

    /* ============================== 元数据 ============================== */

    /**
     * 当前连接默认数据库
     */
    public String currentDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DATABASE()")) {
            return rs.next() ? rs.getString(1) : null;
        } catch (SQLException e) {
            throw new BadParameterException("获取当前数据库失败: " + e.getMessage());
        }
    }

    /**
     * 数据库列表
     */
    public List<String> listDatabases() {
        String sql = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA ORDER BY SCHEMA_NAME";
        List<String> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString(1);
                if (!SYSTEM_SCHEMAS.contains(name)) {
                    list.add(name);
                }
            }
        } catch (SQLException e) {
            throw new BadParameterException("获取数据库列表失败: " + e.getMessage());
        }
        return list;
    }

    /**
     * 表列表
     */
    public List<TableInfoVO> listTables(String database, String keywords) {
        String sql = "SELECT TABLE_NAME, TABLE_COMMENT, ENGINE, TABLE_COLLATION, TABLE_ROWS, " +
                "DATA_LENGTH, CREATE_TIME, UPDATE_TIME " +
                "FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE'";
        if (keywords != null && !keywords.isBlank()) {
            sql += " AND TABLE_NAME LIKE CONCAT('%', ?, '%')";
        }
        sql += " ORDER BY TABLE_NAME";
        List<TableInfoVO> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, database);
            if (keywords != null && !keywords.isBlank()) {
                ps.setString(2, keywords);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TableInfoVO vo = new TableInfoVO();
                    vo.setTableName(rs.getString("TABLE_NAME"));
                    vo.setTableComment(rs.getString("TABLE_COMMENT"));
                    vo.setEngine(rs.getString("ENGINE"));
                    vo.setTableCollation(rs.getString("TABLE_COLLATION"));
                    vo.setTableRows(rs.getLong("TABLE_ROWS"));
                    vo.setDataLength(rs.getLong("DATA_LENGTH"));
                    vo.setCreateTime(String.valueOf(rs.getObject("CREATE_TIME")));
                    vo.setUpdateTime(String.valueOf(rs.getObject("UPDATE_TIME")));
                    list.add(vo);
                }
            }
        } catch (SQLException e) {
            throw new BadParameterException("获取表列表失败: " + e.getMessage());
        }
        return list;
    }

    /**
     * 列列表
     */
    public List<ColumnInfoVO> listColumns(String database, String table) {
        String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, " +
                "COLUMN_DEFAULT, COLUMN_COMMENT, COLUMN_KEY, EXTRA, ORDINAL_POSITION " +
                "FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? " +
                "ORDER BY ORDINAL_POSITION";
        List<ColumnInfoVO> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, database);
            ps.setString(2, table);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ColumnInfoVO vo = new ColumnInfoVO();
                    vo.setColumnName(rs.getString("COLUMN_NAME"));
                    vo.setColumnType(rs.getString("COLUMN_TYPE"));
                    vo.setDataType(rs.getString("DATA_TYPE"));
                    vo.setMaxLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
                    vo.setIsNullable(rs.getString("IS_NULLABLE"));
                    vo.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
                    vo.setColumnComment(rs.getString("COLUMN_COMMENT"));
                    vo.setColumnKey(rs.getString("COLUMN_KEY"));
                    vo.setExtra(rs.getString("EXTRA"));
                    vo.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
                    list.add(vo);
                }
            }
        } catch (SQLException e) {
            throw new BadParameterException("获取表结构失败: " + e.getMessage());
        }
        return list;
    }

    /**
     * 获取主键列（无主键返回第一个列）
     */
    public String getPrimaryKey(String database, String table) {
        List<ColumnInfoVO> columns = listColumns(database, table);
        for (ColumnInfoVO column : columns) {
            if (column.isPk()) {
                return column.getColumnName();
            }
        }
        return columns.isEmpty() ? null : columns.get(0).getColumnName();
    }

    /**
     * 查看建表语句
     */
    public String showCreateTable(String database, String table) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + quote(database) + "." + quote(table))) {
            return rs.next() ? rs.getString("Create Table") : "";
        } catch (SQLException e) {
            throw new BadParameterException("获取建表语句失败: " + e.getMessage());
        }
    }

    /* ============================== 表数据 ============================== */

    /**
     * 分页查询表数据
     */
    public TableDataVO pageData(String database, String table, int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1 || pageSize > 500) {
            pageSize = 10;
        }
        List<ColumnInfoVO> columns = listColumns(database, table);
        TableDataVO vo = new TableDataVO();
        vo.setColumns(columns);
        vo.setPrimaryKey(getPrimaryKey(database, table));
        vo.setCurrent(pageNum);
        vo.setSize(pageSize);
        String tableName = quote(database) + "." + quote(table);
        long total = 0;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
                if (rs.next()) {
                    total = rs.getLong(1);
                }
            }
            vo.setTotal(total);
            long offset = (long) (pageNum - 1) * pageSize;
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT " + pageSize + " OFFSET " + offset)) {
                vo.setRows(resultSetToList(rs));
            }
        } catch (SQLException e) {
            throw new BadParameterException("查询表数据失败: " + e.getMessage());
        }
        return vo;
    }

    /**
     * 新增表数据
     */
    public int insertData(String database, String table, Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            throw new BadParameterException("请填写数据");
        }
        List<ColumnInfoVO> columns = listColumns(database, table);
        List<String> names = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        row.forEach((key, value) -> {
            ColumnInfoVO col = findColumn(columns, key);
            if (col != null) {
                names.add(col.getColumnName());
                values.add(toJdbcValue(col, value));
            }
        });
        if (names.isEmpty()) {
            throw new BadParameterException("没有可写入的列");
        }
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(quote(database)).append(".")
                .append(quote(table)).append(" (");
        sql.append(String.join(",", names.stream().map(this::quote).toList()));
        sql.append(") VALUES (");
        sql.append(String.join(",", values.stream().map(v -> "?").toList()));
        sql.append(")");
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new BadParameterException("新增数据失败: " + e.getMessage());
        }
    }

    /**
     * 修改表数据（按主键）
     */
    public int updateData(String database, String table, Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            throw new BadParameterException("请填写数据");
        }
        String pk = getPrimaryKey(database, table);
        Object pkValue = row.get(pk);
        if (pkValue == null || pkValue.toString().isBlank()) {
            throw new BadParameterException("缺少主键值: " + pk);
        }
        List<ColumnInfoVO> columns = listColumns(database, table);
        List<String> sets = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        row.forEach((key, value) -> {
            if (!key.equals(pk)) {
                ColumnInfoVO col = findColumn(columns, key);
                if (col != null) {
                    sets.add(quote(col.getColumnName()) + " = ?");
                    values.add(toJdbcValue(col, value));
                }
            }
        });
        if (sets.isEmpty()) {
            throw new BadParameterException("没有可更新的列");
        }
        StringBuilder sql = new StringBuilder("UPDATE ").append(quote(database)).append(".")
                .append(quote(table)).append(" SET ").append(String.join(",", sets));
        sql.append(" WHERE ").append(quote(pk)).append(" = ?");
        values.add(toJdbcValue(findColumn(columns, pk), pkValue));
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new BadParameterException("修改数据失败: " + e.getMessage());
        }
    }

    /**
     * 删除表数据（按主键）
     */
    public int deleteData(String database, String table, String pkValue) {
        String pk = getPrimaryKey(database, table);
        if (pkValue == null || pkValue.isBlank()) {
            throw new BadParameterException("缺少主键值");
        }
        String sql = "DELETE FROM " + quote(database) + "." + quote(table) + " WHERE " + quote(pk) + " = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, pkValue);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new BadParameterException("删除数据失败: " + e.getMessage());
        }
    }

    /* ============================== SQL 执行 ============================== */

    /**
     * 执行 DDL（表结构设计）
     */
    public String executeDdl(String database, String ddl) {
        if (ddl == null || ddl.isBlank()) {
            throw new BadParameterException("DDL 语句不能为空");
        }
        List<String> statements = splitStatements(ddl);
        if (statements.isEmpty()) {
            throw new BadParameterException("DDL 语句不能为空");
        }
        try (Connection conn = dataSource.getConnection()) {
            conn.setCatalog(database);
            conn.setAutoCommit(true);
            int total = 0;
            for (String statement : statements) {
                try (Statement stmt = conn.createStatement()) {
                    total += stmt.executeUpdate(statement);
                }
            }
            return "执行成功，共执行 " + statements.size() + " 条 DDL 语句";
        } catch (SQLException e) {
            throw new BadParameterException("DDL 执行失败: " + e.getMessage());
        }
    }

    /**
     * 在线执行 SQL（支持多条语句）
     */
    public List<SqlResultVO> executeSql(String database, String sql) {
        if (sql == null || sql.isBlank()) {
            throw new BadParameterException("SQL 语句不能为空");
        }
        List<String> statements = splitStatements(sql);
        if (statements.isEmpty()) {
            throw new BadParameterException("SQL 语句不能为空");
        }
        List<SqlResultVO> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            if (database != null && !database.isBlank()) {
                conn.setCatalog(database);
            }
            conn.setAutoCommit(true);
            for (String statement : statements) {
                SqlResultVO vo = executeSingle(conn, statement);
                if (vo != null) {
                    results.add(vo);
                }
            }
        } catch (SQLException e) {
            throw new BadParameterException("SQL 执行失败: " + e.getMessage());
        }
        return results;
    }

    private SqlResultVO executeSingle(Connection conn, String sql) {
        SqlResultVO vo = new SqlResultVO();
        vo.setSql(sql);
        String trimmed = sql.trim().toLowerCase();
        if (trimmed.startsWith("use ")) {
            throw new BadParameterException("不支持 USE 语句，请通过接口参数切换数据库");
        }
        boolean query = trimmed.startsWith("select")
                || trimmed.startsWith("show")
                || trimmed.startsWith("desc")
                || trimmed.startsWith("describe")
                || trimmed.startsWith("explain")
                || trimmed.startsWith("with")
                || trimmed.startsWith("pr")
                || trimmed.startsWith("values");
        long start = System.currentTimeMillis();
        try (Statement stmt = conn.createStatement()) {
            if (query) {
                vo.setType("SELECT");
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    vo.setColumns(columnNames(rs));
                    vo.setRows(resultSetToList(rs));
                    vo.setMessage("查询成功，返回 " + vo.getRows().size() + " 行");
                }
            } else {
                boolean isDdl = trimmed.startsWith("create")
                        || trimmed.startsWith("alter")
                        || trimmed.startsWith("drop")
                        || trimmed.startsWith("truncate")
                        || trimmed.startsWith("rename");
                boolean isInsert = trimmed.startsWith("insert");
                boolean isUpdate = trimmed.startsWith("update");
                boolean isDelete = trimmed.startsWith("delete");
                vo.setType(isDdl ? "DDL" : (isInsert ? "INSERT" : (isUpdate ? "UPDATE" : (isDelete ? "DELETE" : "OTHER"))));
                vo.setAffected(stmt.executeUpdate(sql));
                vo.setMessage("执行成功，影响 " + vo.getAffected() + " 行");
            }
        } catch (SQLException e) {
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());
        } finally {
            vo.setElapsed(System.currentTimeMillis() - start);
        }
        return vo;
    }

    /* ============================== 工具方法 ============================== */

    private List<String> columnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        List<String> names = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            names.add(meta.getColumnLabel(i));
        }
        return names;
    }

    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= count; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    private ColumnInfoVO findColumn(List<ColumnInfoVO> columns, String columnName) {
        return columns.stream()
                .filter(c -> c.getColumnName().equalsIgnoreCase(columnName))
                .findFirst()
                .orElse(null);
    }

    /**
     * 将前端字符串值转换为对应数据库列的值
     */
    private Object toJdbcValue(ColumnInfoVO col, Object value) {
        if (value == null) {
            return null;
        }
        String str = String.valueOf(value);
        if (str.isBlank()) {
            return null;
        }
        String dataType = col.getDataType() == null ? "" : col.getDataType().toLowerCase();
        try {
            if (isIntegerType(dataType)) {
                return Long.parseLong(str);
            }
            if (isDecimalType(dataType)) {
                return new BigDecimal(str);
            }
            if ("bit".equals(dataType)) {
                return "true".equalsIgnoreCase(str) || "1".equals(str) ? 1 : 0;
            }
        } catch (NumberFormatException ignored) {
            // 非数字字符串，按原样处理
        }
        return str;
    }

    private boolean isIntegerType(String dataType) {
        return "tinyint".equals(dataType) || "smallint".equals(dataType) || "mediumint".equals(dataType)
                || "int".equals(dataType) || "integer".equals(dataType) || "bigint".equals(dataType)
                || "year".equals(dataType);
    }

    private boolean isDecimalType(String dataType) {
        return "decimal".equals(dataType) || "numeric".equals(dataType)
                || "float".equals(dataType) || "double".equals(dataType)
                || "real".equals(dataType);
    }

    private String quote(String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }

    /**
     * 拆分多条 SQL（支持引号与注释）
     */
    public List<String> splitStatements(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        char quote = 0;
        boolean lineComment = false;
        boolean blockComment = false;
        int length = sql.length();
        for (int i = 0; i < length; i++) {
            char c = sql.charAt(i);
            char next = i + 1 < length ? sql.charAt(i + 1) : '\0';
            if (lineComment) {
                if (c == '\n') {
                    lineComment = false;
                    sb.append('\n');
                }
                continue;
            }
            if (blockComment) {
                if (c == '*' && next == '/') {
                    blockComment = false;
                    i++;
                    sb.append(' ');
                }
                continue;
            }
            if (quote != 0) {
                sb.append(c);
                if (c == '\\') {
                    if (i + 1 < length) {
                        sb.append(sql.charAt(++i));
                    }
                } else if (c == quote) {
                    quote = 0;
                }
                continue;
            }
            if (c == '-' && next == '-') {
                lineComment = true;
                i++;
                continue;
            }
            if (c == '#') {
                lineComment = true;
                continue;
            }
            if (c == '/' && next == '*') {
                blockComment = true;
                i++;
                continue;
            }
            if (c == '\'' || c == '"' || c == '`') {
                quote = c;
                sb.append(c);
                continue;
            }
            if (c == ';') {
                addStatement(statements, sb);
                continue;
            }
            sb.append(c);
        }
        addStatement(statements, sb);
        return statements;
    }

    private void addStatement(List<String> statements, StringBuilder sb) {
        String statement = sb.toString().trim();
        sb.setLength(0);
        if (!statement.isEmpty()) {
            statements.add(statement);
        }
    }

}
