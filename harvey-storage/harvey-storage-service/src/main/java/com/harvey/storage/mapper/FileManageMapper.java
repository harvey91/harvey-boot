package com.harvey.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.storage.model.entity.FileManage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件管理 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-12-05
 */
@Mapper
public interface FileManageMapper extends BaseMapper<FileManage> {

}
