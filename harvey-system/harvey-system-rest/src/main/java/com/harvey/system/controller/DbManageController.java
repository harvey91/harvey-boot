package com.harvey.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.db.ColumnInfoVO;
import com.harvey.system.model.db.DdlExecuteDTO;
import com.harvey.system.model.db.SqlExecuteDTO;
import com.harvey.system.model.db.SqlResultVO;
import com.harvey.system.model.db.TableDataVO;
import com.harvey.system.model.db.TableInfoVO;
import com.harvey.system.service.DbManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据库管理 前端控制器：表结构、表数据、在线SQL
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Tag(name = "数据库管理")
@RestController
@RequestMapping("/api/v1/db")
@RequiredArgsConstructor
public class DbManageController {

    private final DbManageService dbManageService;

    @Operation(summary = "数据库列表")
    @SaCheckPermission("tool:db:select")
    @GetMapping("/databases")
    public RespResult<List<String>> databases() {
        return RespResult.success(dbManageService.listDatabases());
    }

    @Operation(summary = "表列表")
    @SaCheckPermission("tool:db:select")
    @GetMapping("/{database}/tables")
    public RespResult<List<TableInfoVO>> tables(@PathVariable("database") String database,
                                                @RequestParam(value = "keywords", required = false) String keywords) {
        return RespResult.success(dbManageService.listTables(database, keywords));
    }

    @Operation(summary = "表结构（列列表）")
    @SaCheckPermission("tool:db:select")
    @GetMapping("/{database}/{table}/columns")
    public RespResult<List<ColumnInfoVO>> columns(@PathVariable("database") String database,
                                                  @PathVariable("table") String table) {
        return RespResult.success(dbManageService.listColumns(database, table));
    }

    @Operation(summary = "查看建表语句")
    @SaCheckPermission("tool:db:select")
    @GetMapping("/{database}/{table}/ddl")
    public RespResult<String> ddl(@PathVariable("database") String database,
                                  @PathVariable("table") String table) {
        return RespResult.success(dbManageService.showCreateTable(database, table));
    }

    @Operation(summary = "表数据分页查询")
    @SaCheckPermission("tool:db:select")
    @GetMapping("/{database}/{table}/data/page")
    public RespResult<TableDataVO> dataPage(@PathVariable("database") String database,
                                            @PathVariable("table") String table,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return RespResult.success(dbManageService.pageData(database, table, pageNum, pageSize));
    }

    @Operation(summary = "新增表数据")
    @SaCheckPermission("tool:db:create")
    @PostMapping("/{database}/{table}/data")
    public RespResult<Integer> insertData(@PathVariable("database") String database,
                                          @PathVariable("table") String table,
                                          @RequestBody Map<String, Object> row) {
        return RespResult.success(dbManageService.insertData(database, table, row));
    }

    @Operation(summary = "修改表数据")
    @SaCheckPermission("tool:db:modify")
    @PutMapping("/{database}/{table}/data")
    public RespResult<Integer> updateData(@PathVariable("database") String database,
                                          @PathVariable("table") String table,
                                          @RequestBody Map<String, Object> row) {
        return RespResult.success(dbManageService.updateData(database, table, row));
    }

    @Operation(summary = "删除表数据")
    @SaCheckPermission("tool:db:delete")
    @DeleteMapping("/{database}/{table}/data")
    public RespResult<Integer> deleteData(@PathVariable("database") String database,
                                          @PathVariable("table") String table,
                                          @RequestParam("pkValue") String pkValue) {
        return RespResult.success(dbManageService.deleteData(database, table, pkValue));
    }

    @Operation(summary = "执行 DDL（表结构设计）")
    @SaCheckPermission("tool:db:ddl")
    @PostMapping("/{database}/ddl")
    public RespResult<String> executeDdl(@PathVariable("database") String database,
                                         @RequestBody @Valid DdlExecuteDTO dto) {
        return RespResult.success(dbManageService.executeDdl(database, dto.getDdl()));
    }

    @Operation(summary = "在线执行 SQL")
    @SaCheckPermission("tool:db:execute")
    @PostMapping("/sql")
    public RespResult<List<SqlResultVO>> executeSql(@RequestBody @Valid SqlExecuteDTO dto) {
        return RespResult.success(dbManageService.executeSql(dto.getDatabase(), dto.getSql()));
    }

}
