package com.harvey.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.system.model.cache.CacheExpireDTO;
import com.harvey.system.model.cache.CacheKeyQuery;
import com.harvey.system.model.cache.CacheKeyVO;
import com.harvey.system.model.cache.CacheValueDTO;
import com.harvey.system.model.cache.CacheValueVO;
import com.harvey.system.service.CacheManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 缓存管理 前端控制器：缓存 key 查询、查看、新增/修改、删除、过期时间
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Tag(name = "缓存管理")
@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
public class CacheManageController {

    private final CacheManageService cacheManageService;

    @Operation(summary = "缓存 key 分页查询")
    @SaCheckPermission("tool:cache:select")
    @GetMapping("/keys")
    public RespResult<PageResult<CacheKeyVO>> keys(CacheKeyQuery query) {
        return RespResult.success(cacheManageService.pageKeys(query));
    }

    @Operation(summary = "缓存 key 详情")
    @SaCheckPermission("tool:cache:select")
    @GetMapping("/detail")
    public RespResult<CacheValueVO> detail(@RequestParam("key") String key) {
        return RespResult.success(cacheManageService.getValue(key));
    }

    @Operation(summary = "新增/修改缓存")
    @SaCheckPermission("tool:cache:modify")
    @PostMapping
    public RespResult<String> set(@RequestBody @Valid CacheValueDTO dto) {
        cacheManageService.setValue(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除缓存")
    @SaCheckPermission("tool:cache:delete")
    @DeleteMapping
    public RespResult<String> delete(@RequestParam("keys") List<String> keys) {
        cacheManageService.deleteKeys(keys);
        return RespResult.success();
    }

    @Operation(summary = "设置过期时间")
    @SaCheckPermission("tool:cache:modify")
    @PutMapping("/expire")
    public RespResult<String> expire(@RequestBody @Valid CacheExpireDTO dto) {
        cacheManageService.expire(dto);
        return RespResult.success();
    }

    @Operation(summary = "取消过期时间")
    @SaCheckPermission("tool:cache:modify")
    @DeleteMapping("/persist")
    public RespResult<String> persist(@RequestParam("key") String key) {
        cacheManageService.persist(key);
        return RespResult.success();
    }
}
