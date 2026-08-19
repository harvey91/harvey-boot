package com.harvey.ai.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiDocMapper;
import com.harvey.ai.mapstruct.AiDocConverter;
import com.harvey.ai.model.entity.AiDoc;
import com.harvey.ai.model.entity.AiDocChunk;
import com.harvey.ai.model.query.AiDocQuery;
import com.harvey.ai.model.vo.AiDocVO;
import com.harvey.ai.rag.DocTextParser;
import com.harvey.ai.rag.TextChunker;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.core.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * AI知识库文档 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiDocService extends ServiceImpl<AiDocMapper, AiDoc> {

    private final AiDocConverter converter;
    private final AiDocChunkService chunkService;
    private final AiKnowledgeBaseService knowledgeBaseService;
    private final DocTextParser textParser;
    private final TextChunker textChunker;
    private final StorageService storageService;

    public Page<AiDocVO> queryPage(AiDocQuery query) {
        Page<AiDoc> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiDoc> wrapper = new LambdaQueryWrapper<AiDoc>()
                .eq(query.getKbId() != null, AiDoc::getKbId, query.getKbId())
                .like(StringUtils.isNotBlank(query.getKeywords()), AiDoc::getFileName, query.getKeywords())
                .orderByDesc(AiDoc::getCreateTime);
        Page<AiDoc> result = this.page(page, wrapper);
        Page<AiDocVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(converter::toVO).toList());
        return voPage;
    }

    /**
     * 上传并解析文档
     */
    @Transactional(rollbackFor = Throwable.class)
    public AiDoc upload(Long kbId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (knowledgeBaseService.getById(kbId) == null) {
            throw new BusinessException("知识库不存在");
        }
        String fileName = file.getOriginalFilename();
        String fileType = textParser.fileType(fileName);
        if (!textParser.isSupport(fileName)) {
            throw new BusinessException("不支持的文件类型, 请上传 txt/md/pdf/docx");
        }

        // 原文件归档
        String fileUrl = null;
        try {
            String md5 = DigestUtil.md5Hex(file.getBytes());
            fileUrl = storageService.store(file, md5, fileType);
        } catch (IOException e) {
            log.warn("文档归档失败: {}", fileName, e);
        }

        AiDoc doc = new AiDoc();
        doc.setKbId(kbId);
        doc.setFileName(fileName);
        doc.setFileType(fileType);
        doc.setFileSize(file.getSize());
        doc.setFileUrl(fileUrl);
        doc.setStatus(1);
        doc.setChunkCount(0);
        this.save(doc);

        try {
            String text = textParser.parse(file.getBytes(), fileName);
            List<String> chunks = textChunker.chunk(text);
            if (chunks.isEmpty()) {
                throw new BusinessException("文档内容为空, 无法建立知识库");
            }
            List<AiDocChunk> chunkEntities = new ArrayList<>(chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                AiDocChunk chunk = new AiDocChunk();
                chunk.setKbId(kbId);
                chunk.setDocId(doc.getId());
                chunk.setChunkIndex(i);
                chunk.setContent(chunks.get(i));
                chunk.setCharCount(chunks.get(i).length());
                chunkEntities.add(chunk);
            }
            chunkService.saveBatch(chunkEntities);

            doc.setStatus(2);
            doc.setChunkCount(chunkEntities.size());
            doc.setParseTime(LocalDateTime.now());
            this.updateById(doc);

            knowledgeBaseService.incrCounts(kbId, 1, chunkEntities.size());
            return doc;
        } catch (Exception e) {
            log.error("文档解析失败: {}", fileName, e);
            doc.setStatus(3);
            this.updateById(doc);
            throw new BusinessException("文档解析失败: " + e.getMessage());
        }
    }

    /**
     * 重新解析文档: 读取归档文件重新分块, 替换旧分块并更新统计
     */
    @Transactional(rollbackFor = Throwable.class)
    public AiDoc reparse(Long docId) {
        AiDoc doc = docId == null ? null : this.getById(docId);
        if (doc == null) {
            throw new BusinessException("文档不存在");
        }
        if (StringUtils.isBlank(doc.getFileUrl())) {
            throw new BusinessException("文档没有归档文件, 无法重新解析, 请删除后重新上传");
        }
        try {
            byte[] bytes = storageService.loadBytes(doc.getFileUrl());
            String text = textParser.parse(bytes, doc.getFileName());
            List<String> chunks = textChunker.chunk(text);
            if (chunks.isEmpty()) {
                throw new BusinessException("文档内容为空, 无法建立知识库");
            }

            int oldChunk = doc.getChunkCount() == null ? 0 : doc.getChunkCount();
            chunkService.remove(new LambdaQueryWrapper<AiDocChunk>().eq(AiDocChunk::getDocId, docId));

            List<AiDocChunk> chunkEntities = new ArrayList<>(chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                AiDocChunk chunk = new AiDocChunk();
                chunk.setKbId(doc.getKbId());
                chunk.setDocId(docId);
                chunk.setChunkIndex(i);
                chunk.setContent(chunks.get(i));
                chunk.setCharCount(chunks.get(i).length());
                chunkEntities.add(chunk);
            }
            chunkService.saveBatch(chunkEntities);

            doc.setStatus(2);
            doc.setChunkCount(chunkEntities.size());
            doc.setParseTime(LocalDateTime.now());
            this.updateById(doc);

            knowledgeBaseService.incrCounts(doc.getKbId(), 0, chunkEntities.size() - oldChunk);
            return doc;
        } catch (Exception e) {
            log.error("文档重新解析失败: {}", doc.getFileName(), e);
            doc.setStatus(3);
            this.updateById(doc);
            throw new BusinessException("文档重新解析失败: " + e.getMessage());
        }
    }

    /**
     * 删除文档(连同分块), 并更新知识库统计
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的文档");
        }
        for (Long id : ids) {
            AiDoc doc = this.getById(id);
            if (doc == null) {
                continue;
            }
            chunkService.remove(new LambdaQueryWrapper<AiDocChunk>().eq(AiDocChunk::getDocId, id));
            this.removeById(id);
            knowledgeBaseService.incrCounts(doc.getKbId(), -1, -(doc.getChunkCount() == null ? 0 : doc.getChunkCount()));
        }
    }
}