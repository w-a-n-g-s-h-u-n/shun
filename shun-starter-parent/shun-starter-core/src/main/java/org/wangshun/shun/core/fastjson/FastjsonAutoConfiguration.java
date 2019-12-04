package org.wangshun.shun.core.fastjson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@AllArgsConstructor
public class FastjsonAutoConfiguration {
    @Bean
    @SneakyThrows
    public ParserConfig fastjsonParserConfig() {
        ParserConfig globalInstance = ParserConfig.getGlobalInstance();
        // globalInstance.addAccept("org.springframework.");
        // globalInstance.setAutoTypeSupport(true);
        IdentityHashMap<Type, ObjectDeserializer> deserializers = globalInstance.getDeserializers();
        deserializers.put(LocalDateTime.class, TimestampCodec.instance);
        deserializers.put(LocalDate.class, TimestampCodec.instance);
        return globalInstance;
    }

    @Bean
    public SerializeConfig fastjsonSerializerConfig() {
        SerializeConfig globalInstance = SerializeConfig.getGlobalInstance();
        globalInstance.put(LocalDateTime.class, TimestampCodec.instance);
        globalInstance.put(LocalDate.class, TimestampCodec.instance);
        return globalInstance;
    }

    @Bean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        // 空值特别处理
        // WriteNullListAsEmpty 将Collection类型字段的字段空值输出为[]
        // WriteNullStringAsEmpty 将字符串类型字段的空值输出为空字符串 ""
        // WriteNullNumberAsZero 将数值类型字段的空值输出为0
        // WriteNullBooleanAsFalse 将Boolean类型字段的空值输出为false
        // WriteEnumUsingName Enum输出name()
        // UseISO8601DateFormat/WriteDateUseDateFormat 时间格式
        config.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.BrowserCompatible, SerializerFeature.WriteBigDecimalAsPlain);
        SerializeFilter[] newSerializeFilters =
            ArrayUtil.append(config.getSerializeFilters(), new ContextValueFilter() {
                @Override
                public Object process(BeanContext context, Object object, String name, Object value) {
                    if (null == context) {
                        return value;
                    }
                    Field field = context.getField();
                    if (null != field) {
                        if (null != field.getAnnotation(JsonIgnore.class)) {
                            return null;
                        }
                    }
                    if (null != context.getMethod() && null != context.getMethod().getAnnotation(JsonIgnore.class)) {
                        return null;
                    }
                    return value;
                }
            });
        config.setSerializeFilters(newSerializeFilters);
        return config;
    }

}
