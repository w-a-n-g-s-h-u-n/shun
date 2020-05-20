package org.wangshun.shun.web.util;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.wangshun.shun.core.http.R;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestfulUtils {
    public static void writeDataToResponse(HttpServletResponse response, R data) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONObject.toJSONString(data));
        } catch (IOException e) {
        }
    }
}
