package com.harvey.system.mapper;

import com.harvey.system.model.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统通知表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-19
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    NoticeVO getNoticeById(Long id);

}
