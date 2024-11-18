package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.domain.vo.DictVO;
import com.harvey.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
public interface ISysDictService extends IService<SysDict> {

    Page<SysDict> queryPage(DictQueryParam queryParam);

    List<DictVO> queryDictVOList();
}
