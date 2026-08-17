package com.harvey.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiDocChunkMapper;
import com.harvey.ai.mapper.AiDocMapper;
import com.harvey.ai.mapper.AiKnowledgeBaseMapper;
import com.harvey.ai.mapstruct.AiKnowledgeBaseConverter;
import com.harvey.ai.model.dto.AiKnowledgeBaseDto;
import com.harvey.ai.model.entity.AiDoc;
import com.harvey.ai.model.entity.AiDocChunk;
import com.harvey.ai.model.entity.AiKnowledgeBase;
import com.harvey.ai.model.query.AiKnowledgeBaseQuery;
import com.harvey.ai.model.vo.AiKnowledgeBaseVO;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * AI知识库 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Service
@RequiredArgsConstructor
public class AiKnowledgeBaseService extends ServiceImpl<AiKnowledgeBaseMapper, AiKnowledgeBase> {

    private final AiKnowledgeBaseConverter converter;
    private final AiDocMapper docMapper;
    private final AiDocChunkMapper chunkMapper;

    public Page<AiKnowledgeBaseVO> queryPage(AiKnowledgeBaseQuery query) {
        Page<AiKnowledgeBase> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiKnowledgeBase> wrapper = new LambdaQueryWrapper<AiKnowledgeBase>()
                .like(StringUtils.isNotBlank(query.getKeywords()), AiKnowledgeBase::getName, query.getKeywords())
                .eq(query.getEnabled() != null, AiKnowledgeBase::getEnabled, query.getEnabled())
                .orderByDesc(AiKnowledgeBase::getCreateTime);
        Page<AiKnowledgeBase> result = this.page(page, wrapper);
        Page<AiKnowledgeBaseVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(converter::toVO).toList());
        return voPage;
    }

    /**
     * 启用的知识库列表(用于对话端选择)
     */
    public List<AiKnowledgeBaseVO> listEnabled() {
        return this.list(new LambdaQueryWrapper<AiKnowledgeBase>()
                        .eq(AiKnowledgeBase::getEnabled, 1)
                        .orderByAsc(AiKnowledgeBase::getSort))
                .stream().map(converter::toVO).toList();
    }

    public void saveKnowledgeBase(AiKnowledgeBaseDto dto) {
        AiKnowledgeBase entity = converter.toEntity(dto);
        entity.setDocCount(0);
        entity.setChunkCount(0);
        this.save(entity);
    }

    public void updateKnowledgeBase(AiKnowledgeBaseDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("知识库ID不能为空");
        }
        this.updateById(converter.toEntity(dto));
    }

    /**
     * 删除知识库(连同文档与分块)
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的知识库");
        }
        for (Long id : ids) {
            this.removeById(id);
            docMapper.delete(new LambdaQueryWrapper<AiDoc>().eq(AiDoc::getKbId, id));
            chunkMapper.delete(new LambdaQueryWrapper<AiDocChunk>().eq(AiDocChunk::getKbId, id));
        }
    }

    /**
     * 增减文档/分块统计
     */
    public void incrCounts(Long kbId, int docDelta, int chunkDelta) {
        AiKnowledgeBase kb = this.getById(kbId);
        if (kb == null) {
            return;
        }
        int docCount = Math.max(0, (kb.getDocCount() == null ? 0 : kb.getDocCount()) + docDelta);
        int chunkCount = Math.max(0, (kb.getChunkCount() == null ? 0 : kb.getChunkCount()) + chunkDelta);
        kb.setDocCount(docCount);
        kb.setChunkCount(chunkCount);
        this.updateById(kb);
    }
}