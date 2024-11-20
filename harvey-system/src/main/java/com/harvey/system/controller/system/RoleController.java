package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.model.entity.Role;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@Tag(name = "角色管理Controller")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{roleId}")
    public RespResult<Role> findById(@PathVariable(value = "roleId") Long roleId) {
        Role sysRole = roleService.getById(roleId);
        return RespResult.success(sysRole);
    }

    @Operation(summary = "角色分页列表")
    @PreAuthorize("@ex.hasPerm('sys:role:list')")
    @GetMapping("/page")
    public RespResult<PageResult<Role>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        // TODO 查询条件
        Page<Role> page = new Page<>(pageNum, pageSize);
        Page<Role> rolePage = roleService.page(page);
        return RespResult.success(PageResult.of(rolePage));
    }

    @Operation(summary = "角色下拉列表-键值对")
    @GetMapping("/option")
    public RespResult<List<OptionVO>> option() {
        List<Role> list = roleService.list();
        List<OptionVO> optionVOList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)) {
            for (Role role : list) {
                optionVOList.add(OptionVO.builder().value(role.getId()).label(role.getRoleName()).build());
            }
        }
        return RespResult.success(optionVOList);
    }

    @Operation(summary = "新增角色")
    @PreAuthorize("@ex.hasPerm('sys:role:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody Role sysRole) {
        roleService.save(sysRole);
        return RespResult.success();
    }

    @Operation(summary = "编辑角色")
    @PreAuthorize("@ex.hasPerm('sys:role:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody Role sysRole) {
        roleService.updateById(sysRole);
        return RespResult.success();
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("@ex.hasPerm('sys:role:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
            roleService.removeByIds(ids);
        return RespResult.success();
    }
}
