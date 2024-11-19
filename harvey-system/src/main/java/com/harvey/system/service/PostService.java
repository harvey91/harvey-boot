package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.PostMapper;
import com.harvey.system.model.entity.Post;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统职位表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Service
public class PostService extends ServiceImpl<PostMapper, Post> {

}
