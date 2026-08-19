package com.harvey.screen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.screen.model.dto.ScreenMediaPushDto;
import com.harvey.screen.model.query.ScreenMediaQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenMediaVO;
import com.harvey.screen.service.ScreenMediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 信发媒体库 前端控制器
 *
 * @author Harvey
 */
@Tag(name = "信发媒体库")
@RestController
@RequestMapping("/screen/media")
@RequiredArgsConstructor
public class ScreenMediaController {

    private final ScreenMediaService screenMediaService;

    @Operation(summary = "分页列表")
    @SaCheckPermission("screen:media:list")
    @GetMapping("/page")
    public RespResult<PageResult<ScreenMediaVO>> page(ScreenMediaQuery query) {
        Page<ScreenMediaVO> page = screenMediaService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "上传媒体", description = "multipart 表单字段名 file，额外参数 mediaType(1图片 2视频)")
    @SaCheckPermission("screen:media:upload")
    @PostMapping("/upload")
    public RespResult<ScreenMediaVO> upload(@RequestParam("file") MultipartFile file,
                                            @RequestParam("mediaType") Integer mediaType) throws IOException {
        return RespResult.success(screenMediaService.upload(file, mediaType));
    }

    @Operation(summary = "推送媒体到设备")
    @SaCheckPermission("screen:media:push")
    @PostMapping("/push")
    public RespResult<ScreenCommandSendVO> push(@RequestBody @Validated ScreenMediaPushDto dto) {
        return RespResult.success(screenMediaService.push(dto));
    }

    @Operation(summary = "删除")
    @SaCheckPermission("screen:media:delete")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        screenMediaService.deleteById(id);
        return RespResult.success();
    }
}