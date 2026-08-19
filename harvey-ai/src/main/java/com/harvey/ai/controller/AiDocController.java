package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.ai.model.entity.AiDoc;
import com.harvey.ai.model.query.AiDocChunkQuery;
import com.harvey.ai.model.query.AiDocQuery;
import com.harvey.ai.model.vo.AiDocChunkVO;
import com.harvey.ai.model.vo.AiDocVO;
import com.harvey.ai.service.AiDocChunkService;
import com.harvey.ai.service.AiDocService;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * AI 知识库文档 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Tag(name = "AI 知识库文档")
@RestController
@RequestMapping("/ai/doc")
@RequiredArgsConstructor
public class AiDocController {

    private final AiDocService docService;
    private final AiDocChunkService chunkService;

    @Operation(summary = "文档分页列表")
    @SaCheckPermission("ai:doc:list")
    @GetMapping("/page")
    public RespResult<PageResult<AiDocVO>> page(AiDocQuery query) {
        Page<AiDocVO> page = docService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "上传文档并解析")
    @SaCheckPermission("ai:doc:upload")
    @PostMapping("/upload")
    public RespResult<AiDoc> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam("kbId") Long kbId) {
        return RespResult.success(docService.upload(kbId, file));
    }

    @Operation(summary = "删除文档")
    @SaCheckPermission("ai:doc:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        docService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "重新解析文档")
    @SaCheckPermission("ai:doc:upload")
    @PostMapping("/reparse")
    public RespResult<AiDoc> reparse(@RequestParam("id") Long id) {
        if (id == null) {
            return RespResult.fail("id不能为空");
        }
        return RespResult.success(docService.reparse(id));
    }

    @Operation(summary = "文档分块列表")
    @SaCheckPermission("ai:doc:list")
    @GetMapping("/chunk/page")
    public RespResult<List<AiDocChunkVO>> chunkPage(AiDocChunkQuery query) {
        return RespResult.success(chunkService.listChunkPage(query));
    }
}