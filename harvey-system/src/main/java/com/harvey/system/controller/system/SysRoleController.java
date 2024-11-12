package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.vo.OptionVO;
import com.harvey.system.entity.SysRole;
import com.harvey.system.service.ISysRoleService;
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
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {
    private final ISysRoleService sysRoleService;

    @GetMapping("/form/{roleId}")
    public RespResult<SysRole> findById(@PathVariable(value = "roleId") Long roleId) {
        SysRole sysRole = sysRoleService.getById(roleId);
        return RespResult.success(sysRole);
    }

    @GetMapping("/page")
    public RespResult<PageResult<SysRole>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        Page<SysRole> rolePage = sysRoleService.page(page);
        return RespResult.success(PageResult.of(rolePage));
    }

    @GetMapping("/option")
    public RespResult<List<OptionVO>> option() {
        List<SysRole> list = sysRoleService.list();
        List<OptionVO> optionVOList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)) {
            for (SysRole sysRole : list) {
                OptionVO optionVO = new OptionVO();
                optionVO.setValue(sysRole.getId());
                optionVO.setLabel(sysRole.getRoleName());
                optionVOList.add(optionVO);
            }
        }
        return RespResult.success(optionVOList);
    }

    @PreAuthorize("@ex.hasPerm('sys:role:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:role:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:role:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
            sysRoleService.removeByIds(ids);
        return RespResult.success();
    }
}
