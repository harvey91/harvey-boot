package com.harvey.core.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Harvey
 * @date 2024-10-30 20:59
 **/
public class CustomIdentifierGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String id = format.format(date);
        return Long.valueOf(id);
    }
}
