package com.harvey.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.entity.NoticeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.query.NoticeUserQuery;
import com.harvey.system.model.vo.NoticeUserVO;

/**
 * <p>
 * 系统通知指定用户表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-20
 */
public interface NoticeUserMapper extends BaseMapper<NoticeUser> {

    Page<NoticeUserVO> selectMyPage(Page<NoticeUserVO> page, NoticeUserQuery query);
}
