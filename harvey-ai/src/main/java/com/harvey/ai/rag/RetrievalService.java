package com.harvey.ai.rag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.harvey.ai.model.entity.AiDoc;
import com.harvey.ai.model.entity.AiDocChunk;
import com.harvey.ai.service.AiDocChunkService;
import com.harvey.ai.service.AiDocService;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识库检索服务
 * <p>
 * 加载知识库分块, 使用 BM25 词法检索返回最相关分块(带文档名, 用于引用)。
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RetrievalService {

    private final AiDocService docService;
    private final AiDocChunkService chunkService;
    private final Bm25Retriever retriever;

    /**
     * 检索最相关分块
     *
     * @param kbIds 知识库ID集合, 为空则检索全部启用知识库
     * @param query 检索问题
     * @param topK  返回条数
     */
    public List<RetrievedChunkResult> retrieve(List<Long> kbIds, String query, int topK) {
        List<AiDocChunk> chunks = loadChunks(kbIds);
        if (chunks.isEmpty()) {
            return List.of();
        }
        List<Bm25Retriever.RetrievedChunk> hits = retriever.retrieve(chunks, query, topK);
        return hits.stream().map(hit -> {
            AiDoc doc = docService.getById(hit.chunk().getDocId());
            return new RetrievedChunkResult(hit.chunk(), hit.score(),
                    doc == null ? "" : doc.getFileName());
        }).toList();
    }

    private List<AiDocChunk> loadChunks(List<Long> kbIds) {
        LambdaQueryWrapper<AiDocChunk> wrapper = new LambdaQueryWrapper<AiDocChunk>()
                .in(StringUtils.isNotEmpty(kbIds), AiDocChunk::getKbId, kbIds)
                .orderByAsc(AiDocChunk::getId)
                .last("limit 10000");
        return chunkService.listChunks(wrapper);
    }

    /**
     * 检索结果: 分块 + 得分 + 来源文档名
     */
    public record RetrievedChunkResult(AiDocChunk chunk, double score, String fileName) {
    }
}