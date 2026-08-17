package com.harvey.ai.rag;

import com.harvey.ai.model.entity.AiDocChunk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BM25 检索评分(离线词法检索, 无需嵌入模型)
 * <p>
 * 中文以双字词元 + 英文单词构成词项, 按 BM25 公式对分块打分。
 *
 * @author harvey
 * @since 2026-08-17
 */
@Component
@RequiredArgsConstructor
public class Bm25Retriever {

    private static final double K1 = 1.2;
    private static final double B = 0.75;

    private final TextTokenizer tokenizer;

    /**
     * 从候选分块中检索与 query 最相关的 topK 个分块
     */
    public List<RetrievedChunk> retrieve(List<AiDocChunk> chunks, String query, int topK) {
        List<RetrievedChunk> results = new ArrayList<>();
        if (chunks == null || chunks.isEmpty()) {
            return results;
        }
        List<String> queryTokens = tokenizer.tokenize(query);
        if (queryTokens.isEmpty()) {
            return results;
        }

        // 每块的词频与长度
        List<Map<String, Integer>> termFreqs = new ArrayList<>(chunks.size());
        List<Integer> lengths = new ArrayList<>(chunks.size());
        // 包含词项的块数(用于 idf)
        Map<String, Integer> docFreq = new HashMap<>();
        for (AiDocChunk chunk : chunks) {
            Map<String, Integer> tf = new HashMap<>();
            for (String token : tokenizer.tokenize(chunk.getContent())) {
                tf.merge(token, 1, Integer::sum);
            }
            int len = tokenizer.normalize(chunk.getContent()).length();
            for (String token : tf.keySet()) {
                docFreq.merge(token, 1, Integer::sum);
            }
            termFreqs.add(tf);
            lengths.add(len);
        }

        double avgDl = lengths.stream().mapToInt(Integer::intValue).average().orElse(1.0);
        int n = chunks.size();

        for (int i = 0; i < chunks.size(); i++) {
            Map<String, Integer> tf = termFreqs.get(i);
            int dl = lengths.get(i);
            double score = 0.0;
            for (String token : queryTokens) {
                int freq = tf.getOrDefault(token, 0);
                if (freq == 0) {
                    continue;
                }
                int df = docFreq.getOrDefault(token, 0);
                double idf = Math.log(1 + (n - df + 0.5) / (df + 0.5));
                double tfNorm = freq * (K1 + 1) / (freq + K1 * (1 - B + B * dl / avgDl));
                score += idf * tfNorm;
            }
            if (score > 0) {
                results.add(new RetrievedChunk(chunks.get(i), score));
            }
        }

        results.sort((a, b) -> Double.compare(b.score(), a.score()));
        return results.subList(0, Math.min(topK, results.size()));
    }

    /**
     * 检索结果: 分块 + 得分
     */
    public record RetrievedChunk(AiDocChunk chunk, double score) {
    }
}