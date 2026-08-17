package com.harvey.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiFaqMapper;
import com.harvey.ai.mapstruct.AiFaqConverter;
import com.harvey.ai.model.dto.AiFaqDto;
import com.harvey.ai.model.entity.AiFaq;
import com.harvey.ai.model.query.AiFaqQuery;
import com.harvey.ai.model.vo.AiFaqVO;
import com.harvey.ai.rag.TextTokenizer;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * AI客服常见问题 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Service
@RequiredArgsConstructor
public class AiFaqService extends ServiceImpl<AiFaqMapper, AiFaq> {

    private static final double K1 = 1.2;
    private static final double B = 0.75;
    /** 问题词项加权(问题匹配比答案更重要) */
    private static final double QUESTION_WEIGHT = 2.0;

    private final AiFaqConverter converter;
    private final TextTokenizer tokenizer;

    public Page<AiFaqVO> queryPage(AiFaqQuery query) {
        Page<AiFaq> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiFaq> wrapper = new LambdaQueryWrapper<AiFaq>()
                .like(StringUtils.isNotBlank(query.getKeywords()), AiFaq::getQuestion, query.getKeywords())
                .eq(StringUtils.isNotBlank(query.getCategory()), AiFaq::getCategory, query.getCategory())
                .eq(query.getEnabled() != null, AiFaq::getEnabled, query.getEnabled())
                .orderByAsc(AiFaq::getSort)
                .orderByDesc(AiFaq::getId);
        Page<AiFaq> result = this.page(page, wrapper);
        Page<AiFaqVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(converter::toVO).toList());
        return voPage;
    }

    /**
     * 启用的常见问题列表
     */
    public List<AiFaq> listEnabled() {
        return this.list(new LambdaQueryWrapper<AiFaq>()
                .eq(AiFaq::getEnabled, 1)
                .orderByAsc(AiFaq::getSort));
    }

    public void saveFaq(AiFaqDto dto) {
        AiFaq entity = converter.toEntity(dto);
        entity.setHitCount(0);
        this.save(entity);
    }

    public void updateFaq(AiFaqDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("常见问题ID不能为空");
        }
        this.updateById(converter.toEntity(dto));
    }

    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的常见问题");
        }
        this.removeByIds(ids);
    }

    /**
     * 命中次数 +1
     */
    public void incrHitCount(Long id) {
        if (id == null) {
            return;
        }
        AiFaq faq = this.getById(id);
        if (faq != null) {
            faq.setHitCount((faq.getHitCount() == null ? 0 : faq.getHitCount()) + 1);
            this.updateById(faq);
        }
    }

    /**
     * FAQ BM25 检索: 问题词项加权, 返回最相关条目
     */
    public List<RetrievedFaq> retrieve(String query, int topK) {
        List<AiFaq> faqs = listEnabled();
        List<RetrievedFaq> results = new ArrayList<>();
        if (faqs.isEmpty()) {
            return results;
        }
        List<String> queryTokens = tokenizer.tokenize(query);
        if (queryTokens.isEmpty()) {
            return results;
        }

        Map<String, Integer> docFreq = new HashMap<>();
        List<Map<String, Integer>> termFreqs = new ArrayList<>(faqs.size());
        List<Integer> lengths = new ArrayList<>(faqs.size());
        for (AiFaq faq : faqs) {
            Map<String, Integer> tf = new HashMap<>();
            for (String token : tokenizer.tokenize(faq.getQuestion())) {
                tf.merge(token, (int) QUESTION_WEIGHT, Integer::sum);
            }
            for (String token : tokenizer.tokenize(faq.getAnswer())) {
                tf.merge(token, 1, Integer::sum);
            }
            int len = tokenizer.normalize(faq.getQuestion()).length()
                    + tokenizer.normalize(faq.getAnswer()).length();
            for (String token : tf.keySet()) {
                docFreq.merge(token, 1, Integer::sum);
            }
            termFreqs.add(tf);
            lengths.add(len);
        }

        double avgDl = lengths.stream().mapToInt(Integer::intValue).average().orElse(1.0);
        int n = faqs.size();
        for (int i = 0; i < faqs.size(); i++) {
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
                results.add(new RetrievedFaq(faqs.get(i), score));
            }
        }

        results.sort((a, b) -> Double.compare(b.score(), a.score()));
        return results.subList(0, Math.min(topK, results.size()));
    }

    /**
     * FAQ 检索结果: 条目 + 得分
     */
    public record RetrievedFaq(AiFaq faq, double score) {
    }
}