package com.harvey.system.controller;

import com.harvey.system.base.RespResult;
import com.harvey.system.model.entity.Dept;
import com.harvey.system.model.vo.DeptVO;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.service.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统部门表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class DeptController {
    private final DeptService deptService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<Dept> formById(@PathVariable(value = "id") Long id) {
        Dept entity = deptService.getById(id);
        return RespResult.success(entity);
    }

    @Operation(summary = "部门列表-tree")
    @GetMapping("/list")
    public RespResult<List<DeptVO>> list() {
        List<Dept> list = deptService.list();
        List<DeptVO> deptVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, List<Dept>> collect = list.stream().collect(Collectors.groupingBy(Dept::getParentId));
            if (collect.containsKey(0L)) {
                // 获取所有最顶级部门列表
                List<Dept> parantDeptList = collect.get(0L);
                for (Dept dept : parantDeptList) {
                    DeptVO deptVO = new DeptVO();
                    BeanUtils.copyProperties(dept, deptVO);
                    deptVOList.add(deptVO);
                    // 递归封装所有部门
                    recursion(deptVO, collect);
                }
            }
        }
        return RespResult.success(deptVOList);
    }

    @Operation(summary = "部门下拉列表-tree")
    @GetMapping("/option")
    public RespResult<List<OptionVO<Long>>> option() {
        List<Dept> list = deptService.list();
        List<OptionVO<Long>> optionVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return RespResult.success();
        }

        Map<Long, List<Dept>> collect = list.stream().collect(Collectors.groupingBy(Dept::getParentId));
        if (collect.containsKey(0L)) {
            List<Dept> deptList = collect.get(0L);
            for (Dept dept : deptList) {
                OptionVO<Long> optionVO = OptionVO.<Long>builder().value(dept.getId()).label(dept.getDeptName()).build();
                optionVOList.add(optionVO);
                recursionOption(optionVO, collect);
            }
        }
        return RespResult.success(optionVOList);
    }

    @Operation(summary = "新增部门")
    @PreAuthorize("@ex.hasPerm('sys:dept:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody Dept entity) {
        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        deptService.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "编辑部门")
    @PreAuthorize("@ex.hasPerm('sys:dept:modify')")
    @PostMapping("/modify")
    public RespResult<String> modify(@RequestBody Dept entity) {
        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        deptService.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除部门")
    @PreAuthorize("@ex.hasPerm('sys:dept:delete')")
    @PostMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        deptService.removeByIds(ids);
        return RespResult.success();
    }

    /**
     * 递归所有下级部门
     * @param parentDeptVO
     * @param collect
     */
    public void recursion(DeptVO parentDeptVO, Map<Long, List<Dept>> collect) {
        if (!collect.containsKey(parentDeptVO.getId())) {
            return;
        }
        List<DeptVO> deptVOList = new ArrayList<>();
        List<Dept> childDeptList = collect.get(parentDeptVO.getId());
        for (Dept dept : childDeptList) {
            DeptVO deptVO = new DeptVO();
            BeanUtils.copyProperties(dept, deptVO);
            deptVOList.add(deptVO);
            recursion(deptVO, collect);
        }
        parentDeptVO.setChildren(deptVOList);
    }

    /**
     * 递归所有下级部门-下拉列表
     * @param parentOptionVO
     * @param collect
     */
    public void recursionOption(OptionVO<Long> parentOptionVO, Map<Long, List<Dept>> collect) {
        if (!collect.containsKey(parentOptionVO.getValue())) {
            return;
        }
        List<OptionVO<Long>> optionVOList = new ArrayList<>();
        List<Dept> childDeptList = collect.get(parentOptionVO.getValue());
        for (Dept dept : childDeptList) {
            OptionVO<Long> optionVO = OptionVO.<Long>builder().value(dept.getId()).label(dept.getDeptName()).build();
            optionVOList.add(optionVO);
            recursionOption(optionVO, collect);
        }
        parentOptionVO.setChildren(optionVOList);
    }
}
