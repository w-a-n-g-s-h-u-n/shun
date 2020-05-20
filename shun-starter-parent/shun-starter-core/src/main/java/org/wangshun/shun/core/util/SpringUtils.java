package org.wangshun.shun.core.util;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取上下文bean。
 *
 * <pre>
 * 在spring配置文件中做如下配置。
 * </pre>
 */
@Component
@Lazy(false)
@Slf4j
public class SpringUtils implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtils.context = context;

    }

    /**
     * 获取spring容器上下文。
     */
    public static ApplicationContext getApplicaitonContext() {
        return context;
    }

    /**
     * 根据beanId获取配置在系统中的对象实例。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        try {
            return (T) context.getBean(beanId);
        } catch (Exception ex) {
            log.debug("getBean:" + beanId + "," + ex.getMessage());
        }
        return null;
    }

    /**
     * 获取Spring容器的Bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        try {
            return context.getBean(beanClass);
        } catch (Exception ex) {
            log.debug("getBean:" + beanClass + "," + ex.getMessage());
        }
        return null;
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("清除SpringContextHolder中的ApplicationContext:" + context);
        }
        context = null;
    }

    /**
     * 根据指定的接口或基类获取实现类列表。
     */
    public static <T> List<Class<?>> getImplClass(Class<T> clazz) throws ClassNotFoundException {
        List<Class<?>> list = new ArrayList<>();

        Map<String, T> beanMap = context.getBeansOfType(clazz);
        for (T bean : beanMap.values()) {
            String name = bean.getClass().getName();
            int pos = name.indexOf("$$");
            if (pos > 0) {
                name = name.substring(0, name.indexOf("$$"));
            }
            Class<?> cls = Class.forName(name);

            list.add(cls);
        }
        return list;
    }

    /**
     * 获取接口的实现类实例。
     */
    public static <T> Map<String, T> getImplInstance(Class<T> clazz) {
        return context.getBeansOfType(clazz);
    }

    public static <T> List<T> getImplInstanceArray(Class<T> clazz) {
        List<T> list = new ArrayList<>();

        Map<String, T> map = context.getBeansOfType(clazz);

        for (T t : map.values()) {
            list.add(t);
        }
        return list;
    }

    /**
     * 发布事件。
     */
    public static void publishEvent(ApplicationEvent event) {
        if (context != null) {
            context.publishEvent(event);
        }
    }

    /**
     * 获取当前系统环境<br>
     * 目前仅支持单一环境配置。请勿配置多个参数 dev sit 之类。 环境配置的判断参考下面代码<br>
     * doGetActiveProfiles().contains(profile) || (doGetActiveProfiles().isEmpty() &&
     * doGetDefaultProfiles().contains(profile))
     */
    private static String currentProfiles = null;

    public static String getCtxEnvironment() {
        if (currentProfiles != null) {
            return currentProfiles;
        }

        Environment environment = context.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();

        if (ArrayUtil.isNotEmpty(activeProfiles)) {
            currentProfiles = activeProfiles[0];
            return currentProfiles;
        }

        String[] defaultProfiles = environment.getDefaultProfiles();
        if (ArrayUtil.isNotEmpty(defaultProfiles)) {
            currentProfiles = defaultProfiles[0];
            return defaultProfiles[0];
        }

        throw new RuntimeException("查找不到正确的环境属性配置！");
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() {
        SpringUtils.clearHolder();
    }

}
