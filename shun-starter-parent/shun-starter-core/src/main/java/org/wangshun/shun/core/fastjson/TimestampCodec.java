package org.wangshun.shun.core.fastjson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.JSONSerializer;

public class TimestampCodec extends Jdk8DateCodec {

    public static final TimestampCodec instance = new TimestampCodec();

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        Class<? extends Object> clazz = object.getClass();
        if (clazz == LocalDateTime.class) {
            LocalDateTime dateTime = (LocalDateTime)object;
            serializer.out.writeLong(dateTime.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
        } else if (clazz == LocalDate.class) {
            LocalDate dateTime = (LocalDate)object;
            serializer.out.writeLong(dateTime.atStartOfDay(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
        }
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Class<? extends Object> clazz = object.getClass();
        if (clazz == LocalDateTime.class) {
            LocalDateTime dateTime = (LocalDateTime)object;
            serializer.out.writeLong(dateTime.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
        } else if (clazz == LocalDate.class) {
            LocalDate dateTime = (LocalDate)object;
            serializer.out.writeLong(dateTime.atStartOfDay(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
        }
    }

}
