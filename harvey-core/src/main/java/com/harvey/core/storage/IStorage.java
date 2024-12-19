package com.harvey.core.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 对象存储服务接口
 *
 * @author Harvey
 * @date 2024-12-05 22:23
 **/
public interface IStorage {

    void store(InputStream inputStream, long contentLength, String contentType, String keyName);

    Stream<Path> loadAll();

    Path load(String keyName);

    Resource loadAsResource(String keyName);

    void delete(String keyName);

    String generateUrl(String keyName);
}
