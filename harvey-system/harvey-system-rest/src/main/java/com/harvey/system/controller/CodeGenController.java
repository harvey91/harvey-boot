package com.harvey.system.controller;

import com.harvey.common.model.query.Query;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.system.model.db.GeneratorPreviewVO;
import com.harvey.system.model.db.TablePageVO;
import com.harvey.system.model.dto.GenConfigDto;
import com.harvey.system.service.CodeGenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 代码生成 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Tag(name = "代码生成")
@RestController
@RequestMapping("/api/v1/codegen")
@RequiredArgsConstructor
public class CodeGenController {

    private final CodeGenService codeGenService;

    @Operation(summary = "数据表分页列表")
    @GetMapping("/table/page")
    public RespResult<PageResult<TablePageVO>> tablePage(Query query) {
        return RespResult.success(codeGenService.getTablePage(query));
    }

    @Operation(summary = "获取代码生成配置")
    @GetMapping("/{tableName}/config")
    public RespResult<GenConfigDto> getGenConfig(@PathVariable("tableName") String tableName) {
        return RespResult.success(codeGenService.getGenConfig(tableName));
    }

    @Operation(summary = "保存代码生成配置")
    @PostMapping("/{tableName}/config")
    public RespResult<GenConfigDto> saveGenConfig(@PathVariable("tableName") String tableName,
                                                  @RequestBody GenConfigDto dto) {
        return RespResult.success(codeGenService.saveGenConfig(tableName, dto));
    }

    @Operation(summary = "重置代码生成配置")
    @DeleteMapping("/{tableName}/config")
    public RespResult<String> resetGenConfig(@PathVariable("tableName") String tableName) {
        codeGenService.resetGenConfig(tableName);
        return RespResult.success();
    }

    @Operation(summary = "代码生成预览")
    @GetMapping("/{tableName}/preview")
    public RespResult<List<GeneratorPreviewVO>> preview(@PathVariable("tableName") String tableName) {
        return RespResult.success(codeGenService.preview(tableName));
    }

    @Operation(summary = "下载生成代码")
    @GetMapping("/{tableName}/download")
    public ResponseEntity<byte[]> download(@PathVariable("tableName") String tableName) {
        byte[] data = codeGenService.download(tableName);
        String fileName = URLEncoder.encode(tableName + ".zip", StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

}
