package org.wangshun.shun.core.prop;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.security")
@Data
@Component
public class PermitUrlProperties {

    private static final String[] DEFAULT_IGNORE_URLS = new String[]{"/error", "/actuator/**"};
    private static final String[] DEFAULT_STATIC_RESOURCES = new String[]{"/**/*.html", "/**/*.js", "/**/*.css",
            "/**/*.png", "/**/*.gif", "/**/*.jpg", "/**/*.svg", "/**/*.woff", "/**/*.ttf", "/**/*.pdf", "/**/*.ico"};

    private String[] ignoreUrls;

    private String[] staticResources;

    public String[] getIgnoreUrls() {
        return ArrayUtil.addAll(ignoreUrls, DEFAULT_IGNORE_URLS);
    }

    public String[] getStaticResources() {
        return ArrayUtil.addAll(staticResources, DEFAULT_STATIC_RESOURCES);
    }

}
