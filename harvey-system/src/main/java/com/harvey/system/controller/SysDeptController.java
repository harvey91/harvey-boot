package com.harvey.system.controller;

import com.harvey.system.base.RespResult;
import com.harvey.system.domain.vo.DeptVO;
import com.harvey.system.domain.vo.OptionVO;
import com.harvey.system.entity.SysDept;
import com.harvey.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController {
    private final ISysDeptService sysDeptService;

    @GetMapping("/form/{id}")
    public RespResult<SysDept> formById(@PathVariable(value = "id") Long id) {
        SysDept sysDept = sysDeptService.getById(id);
        return RespResult.success(sysDept);
    }

    /**
     * 查询所有部门树列表
     * @return
     */
    @GetMapping("/list")
    public RespResult<List<DeptVO>> list() {
        List<SysDept> list = sysDeptService.list();
        List<DeptVO> deptVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, List<SysDept>> collect = list.stream().collect(Collectors.groupingBy(SysDept::getParentId));
            if (collect.containsKey(0L)) {
                // 获取所有最顶级部门列表
                List<SysDept> parantDeptList = collect.get(0L);
                for (SysDept dept : parantDeptList) {
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

    /**
     * 查询所有部门下拉树列表
     * @return
     */
    @GetMapping("/option")
    public RespResult<List<OptionVO>> option() {
        List<SysDept> list = sysDeptService.list();
        List<OptionVO> optionVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return RespResult.success();
        }

        Map<Long, List<SysDept>> collect = list.stream().collect(Collectors.groupingBy(SysDept::getParentId));
        if (collect.containsKey(0L)) {
            List<SysDept> deptList = collect.get(0L);
            for (SysDept dept : deptList) {
                OptionVO optionVO = new OptionVO();
                optionVO.setValue(dept.getId());
                optionVO.setLabel(dept.getDeptName());
                optionVOList.add(optionVO);
                recursionOption(optionVO, collect);
            }
        }
        return RespResult.success(optionVOList);
    }

    /**
     * 保存
     * @param sysDept
     * @return
     */
    @PostMapping("/save")
    public RespResult<String> save(@RequestBody SysDept sysDept) {
        if (sysDept.getParentId() == null) {
            sysDept.setParentId(0L);
        }
        sysDeptService.saveOrUpdate(sysDept);
        return RespResult.success();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    public RespResult<String> delete(@RequestBody Long[] ids) {
        if (ids == null) {
            return RespResult.fail("id不能为空");
        }
        sysDeptService.removeByIds(Arrays.asList(ids));
        return RespResult.success();
    }

    /**
     * 递归所有下级部门
     * @param parentDeptVO
     * @param collect
     */
    public void recursion(DeptVO parentDeptVO, Map<Long, List<SysDept>> collect) {
        if (!collect.containsKey(parentDeptVO.getId())) {
            return;
        }
        List<DeptVO> deptVOList = new ArrayList<>();
        List<SysDept> childDeptList = collect.get(parentDeptVO.getId());
        for (SysDept dept : childDeptList) {
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
    public void recursionOption(OptionVO parentOptionVO, Map<Long, List<SysDept>> collect) {
        if (!collect.containsKey(parentOptionVO.getValue())) {
            return;
        }
        List<OptionVO> optionVOList = new ArrayList<>();
        List<SysDept> childDeptList = collect.get(parentOptionVO.getValue());
        for (SysDept dept : childDeptList) {
            OptionVO optionVO = new OptionVO();
            optionVO.setValue(dept.getId());
            optionVO.setLabel(dept.getDeptName());
            optionVOList.add(optionVO);
            recursionOption(optionVO, collect);
        }
        parentOptionVO.setChildren(optionVOList);
    }
}
