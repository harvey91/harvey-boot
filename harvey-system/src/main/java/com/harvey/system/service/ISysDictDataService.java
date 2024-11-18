package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.domain.query.DictQueryParam;
import com.harvey.system.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统字典数据表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
public interface ISysDictDataService extends IService<SysDictData> {

    Page<SysDictData> queryPage(DictQueryParam queryParam);
}
