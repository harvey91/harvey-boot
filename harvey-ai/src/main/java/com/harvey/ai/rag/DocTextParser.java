package com.harvey.ai.rag;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文档文本解析器
 * <p>
 * 支持 txt/md/pdf/docx, 其余类型尝试按 UTF-8 文本解析
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Component
public class DocTextParser {

    /**
     * 解析文件内容为纯文本
     *
     * @param bytes    文件字节
     * @param fileName 原始文件名(用于识别类型)
     */
    public String parse(byte[] bytes, String fileName) {
        String type = fileType(fileName);
        return switch (type) {
            case "pdf" -> parsePdf(bytes);
            case "docx" -> parseDocx(bytes);
            default -> new String(bytes, StandardCharsets.UTF_8);
        };
    }

    public String fileType(String fileName) {
        if (fileName == null) {
            return "txt";
        }
        int idx = fileName.lastIndexOf('.');
        return idx < 0 ? "txt" : fileName.substring(idx + 1).toLowerCase();
    }

    private String parsePdf(byte[] bytes) {
        try (PDDocument document = PDDocument.load(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("PDF 解析失败", e);
            throw new IllegalArgumentException("PDF 解析失败: " + e.getMessage());
        }
    }

    private String parseDocx(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(bytes))) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text != null && !text.isBlank()) {
                    sb.append(text).append('\n');
                }
            }
        } catch (IOException e) {
            log.error("Word 解析失败", e);
            throw new IllegalArgumentException("Word 解析失败: " + e.getMessage());
        }
        return sb.toString();
    }

    public boolean isSupport(String fileName) {
        return List.of("txt", "md", "pdf", "docx").contains(fileType(fileName));
    }
}