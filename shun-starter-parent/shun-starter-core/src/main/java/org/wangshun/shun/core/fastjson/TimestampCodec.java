package org.wangshun.shun.core.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimestampCodec extends Jdk8DateCodec {

    public static final TimestampCodec instance = new TimestampCodec();

    @Override
    public void write(JSONSerializer serializer, Object object, BeanContext context) throws IOException {
        Class<? extends Object> clazz = object.getClass();
        if (clazz == Instant.class) {
            Instant instant = (Instant) object;
            serializer.out.writeString(String.valueOf(instant.toEpochMilli()));
            return;
        } else if (clazz == LocalDateTime.class) {
            LocalDateTime dateTime = (LocalDateTime) object;
            serializer.out
                    .writeString(String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            return;
        } else if (clazz == LocalDate.class) {
            LocalDate dateTime = (LocalDate) object;
            serializer.out
                    .writeString(String.valueOf(dateTime.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            return;
        }
        super.write(serializer, object, context);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        Class<? extends Object> clazz = object.getClass();
        if (clazz == Instant.class) {
            Instant instant = (Instant) object;
            serializer.out.writeString(String.valueOf(instant.toEpochMilli()));
            return;
        } else if (clazz == LocalDateTime.class) {
            LocalDateTime dateTime = (LocalDateTime) object;
            serializer.out
                    .writeString(String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            return;
        } else if (clazz == LocalDate.class) {
            LocalDate dateTime = (LocalDate) object;
            serializer.out
                    .writeString(String.valueOf(dateTime.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            return;
        }
        super.write(serializer, object, fieldName, fieldType, features);
    }

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, String format, int feature) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            long millis = lexer.longValue();
            lexer.nextToken();

            if ("unixtime".equals(format)) {
                millis *= 1000;
            }

            if (type == Instant.class) {
                return (T) Instant.ofEpochMilli(millis);
            }
        }
        return super.deserialze(parser, type, fieldName, format, feature);
    }
}
