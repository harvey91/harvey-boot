package com.harvey.ai.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiDocChunkMapper;
import com.harvey.ai.model.entity.AiDocChunk;
import com.harvey.ai.model.query.AiDocChunkQuery;
import com.harvey.ai.model.vo.AiDocChunkVO;
import com.harvey.ai.mapstruct.AiDocChunkConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * AI知识库文档分块 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Service
@RequiredArgsConstructor
public class AiDocChunkService extends ServiceImpl<AiDocChunkMapper, AiDocChunk> {

    private final AiDocChunkConverter converter;

    public List<AiDocChunkVO> listChunkPage(AiDocChunkQuery query) {
        List<AiDocChunk> list = this.list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AiDocChunk>()
                .eq(AiDocChunk::getDocId, query.getDocId())
                .orderByAsc(AiDocChunk::getChunkIndex));
        return list.stream().map(converter::toVO).toList();
    }

    public List<AiDocChunk> listChunks(Wrapper<AiDocChunk> wrapper) {
        return this.list(wrapper);
    }
}