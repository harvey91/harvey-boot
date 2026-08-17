package com.harvey.ai.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本分词器(检索用)
 * <p>
 * 中文按相邻双字(字符bigram)切分, 英文按单词切分, 兼容中英混合。无需外部依赖, 离线可用。
 *
 * @author harvey
 * @since 2026-08-17
 */
@Component
public class TextTokenizer {

    private static final Pattern ASCII_WORD = Pattern.compile("[a-z0-9]{2,}");
    private static final Pattern CJK_RUN = Pattern.compile("[\\u4e00-\\u9fff]+");
    private static final Pattern FULL_WIDTH = Pattern.compile("[\\uff01-\\uff5e]");

    /**
     * 标准化文本: 转小写、全角转半角
     */
    public String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String lower = text.toLowerCase();
        Matcher m = FULL_WIDTH.matcher(lower);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, String.valueOf((char) (m.group().charAt(0) - 0xfee0)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 分词: 英文单词 + 中文双字
     */
    public List<String> tokenize(String text) {
        String normalized = normalize(text);
        List<String> tokens = new ArrayList<>();
        Matcher ascii = ASCII_WORD.matcher(normalized);
        while (ascii.find()) {
            tokens.add(ascii.group());
        }
        Matcher cjk = CJK_RUN.matcher(normalized);
        while (cjk.find()) {
            String run = cjk.group();
            if (run.length() == 1) {
                tokens.add(run);
            } else {
                for (int i = 0; i < run.length() - 1; i++) {
                    tokens.add(run.substring(i, i + 2));
                }
            }
        }
        return tokens;
    }
}