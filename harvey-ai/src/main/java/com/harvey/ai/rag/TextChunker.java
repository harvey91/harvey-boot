package com.harvey.ai.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本分块器
 * <p>
 * 按段落聚合, 目标长度 600 字符, 相邻分块重叠约 150 字符, 避免切断语义。
 *
 * @author harvey
 * @since 2026-08-17
 */
@Component
public class TextChunker {

    private static final int TARGET_SIZE = 600;
    private static final int MAX_SIZE = 1000;
    private static final int OVERLAP_SIZE = 150;

    /**
     * 将长文本切分为有序分块
     */
    public List<String> chunk(String text) {
        List<String> chunks = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return chunks;
        }
        String[] paragraphs = text.split("\\n+");
        StringBuilder current = new StringBuilder();
        for (String paragraph : paragraphs) {
            String p = paragraph.trim();
            if (p.isEmpty()) {
                continue;
            }
            // 段落本身超过上限, 按句子继续切
            if (p.length() > MAX_SIZE) {
                flush(chunks, current);
                current = new StringBuilder();
                for (String piece : splitBySentence(p)) {
                    appendPiece(chunks, current, piece);
                }
                continue;
            }
            appendPiece(chunks, current, p);
        }
        flush(chunks, current);
        return chunks;
    }

    private void appendPiece(List<String> chunks, StringBuilder current, String piece) {
        if (current.length() + piece.length() + 1 <= TARGET_SIZE || current.length() == 0) {
            if (current.length() > 0) {
                current.append('\n');
            }
            current.append(piece);
        } else {
            // 保留上一块尾部作为重叠
            String tail = current.substring(Math.max(0, current.length() - OVERLAP_SIZE));
            flush(chunks, current);
            current.append(tail);
            if (current.length() > 0) {
                current.append('\n');
            }
            current.append(piece);
        }
    }

    private void flush(List<String> chunks, StringBuilder current) {
        if (current.length() > 0) {
            chunks.add(current.toString());
            current.setLength(0);
        }
    }

    private List<String> splitBySentence(String paragraph) {
        List<String> pieces = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : paragraph.toCharArray()) {
            sb.append(c);
            if (c == '。' || c == '！' || c == '？' || c == '；' || c == '\n') {
                if (sb.length() >= TARGET_SIZE) {
                    pieces.add(sb.toString().trim());
                    sb.setLength(0);
                }
            }
        }
        if (sb.length() > 0) {
            pieces.add(sb.toString().trim());
        }
        return pieces;
    }
}