package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.PostMapper;
import com.harvey.system.mapstruct.PostConverter;
import com.harvey.system.model.dto.PostDto;
import com.harvey.system.model.entity.Post;
import com.harvey.system.model.query.PostQuery;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 职位管理 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Service
@RequiredArgsConstructor
public class PostService extends ServiceImpl<PostMapper, Post> {
    private final PostMapper mapper;
    private final PostConverter converter;

    public Page<Post> queryPage(PostQuery query) {
        Page<Post> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<Post>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Post::getPostName, query.getKeywords())
                .orderByAsc(Post::getSort);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void savePost(PostDto dto) {
        Post entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updatePost(PostDto dto) {
        Post entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }
}
