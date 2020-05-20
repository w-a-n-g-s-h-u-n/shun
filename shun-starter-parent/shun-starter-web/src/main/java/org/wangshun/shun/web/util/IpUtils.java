package org.wangshun.shun.web.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.wangshun.shun.core.util.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * IP
 *
 * @Author: zwen
 * @Date: 2019/7/20 15:21
 */
@Slf4j
@UtilityClass
public class IpUtils {
    private static String VALIDE_IP_REGEX = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private final String BASIC_ = "Basic ";
    private final String UNKNOWN = "unknown";
    private final String LOCAL_IP_ADDR = "127.0.0.1";

    private static String ALLOWABLE_IP_REGEX = null;

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("(127[\\.]0[\\.]0[\\.]1)|")
                .append("(localhost)|")
                .append("(10[\\.]\\d{1,3}[\\.]\\d{1,3}[\\.]\\d{1,3})|")
                .append("(172[\\.]((1[6-9])|(2\\d)|(3[01]))[\\.]\\d{1,3}[\\.]\\d{1,3})|")
                .append("(192[\\.]168[\\.]\\d{1,3}[\\.]\\d{1,3})");
        ALLOWABLE_IP_REGEX = sb.toString();
    }

    // 系统的本地IP地址
    private static String LOCAL_IP = null;
    private static String HOST_NAME = null;

    /**
     * 正确的ip地址
     *
     * @param ip
     * @return
     */
    public static boolean isValideIp(String ip) {
        return StringUtils.isEmpty(ip) || ip.matches(VALIDE_IP_REGEX);
    }

    /**
     * 是否是局域网中的ip
     *
     * @param ip
     * @return
     */
    public static boolean isLocalNetwork(String ip) {
        return isValideIp(ip) || ip.matches(ALLOWABLE_IP_REGEX);
    }

    /**
     * 获取本机的ip地址
     *
     * @return
     */
    public static String getLocalIp() {
        if (LOCAL_IP == null) {
            String ip = "";
            try {
                InetAddress inetAddr = InetAddress.getLocalHost();
                HOST_NAME = inetAddr.getHostName();
                byte[] addr = inetAddr.getAddress();
                for (int i = 0; i < addr.length; i++) {
                    if (i > 0) {
                        ip += ".";
                    }
                    ip += addr[i] & 0xFF;
                }
            } catch (UnknownHostException e) {
                ip = UNKNOWN;
                log.error(e.getMessage());
            } finally {
                LOCAL_IP = ip;
            }
        }
        return LOCAL_IP;
    }

    /**
     * <p>
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid,Nginx等反向代理软件就不能获取到客户端的真实IP地址了，
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120,
     * 192.168.1.130, 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request 当前请求
     * @return IP 地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        try {
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (ip.equals(LOCAL_IP_ADDR)) {
                    // 根据网卡取本机配置的IP
                    ip = getLocalIp();
                }
            }
            // 对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() = 15
            if (ip != null && ip.length() > 15) {
                if (ip.indexOf(",") > 0) {
                    ip = ip.substring(0, ip.indexOf(","));
                }
            }
        } finally {
            LOCAL_IP = ip;
        }

        return LOCAL_IP;
    }

    /**
     * 获取当前IP所在城市[BAIDU]
     * 只在生产环境使用
     *
     * @param request
     * @return
     */
    public static JSONObject getAreaByIp(HttpServletRequest request) {
        JSONObject mapData = null;
        if ("prod".equalsIgnoreCase(SpringUtils.getCtxEnvironment())) {
            String httpUrl = "http://apis.baidu.com/apistore/iplookupservice/iplookup";
            String httpArg = getIpAddr(request);// 获取IP
            if (!StringUtils.isEmpty(httpArg)) {
                try {
                    BufferedReader reader = null;
                    StringBuffer sbf = new StringBuffer();
                    httpUrl = httpUrl + "?ip=" + httpArg.split(",")[0];
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("apikey", "4d863e94ba7bad7c7ecf89a50c121029");
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                        sbf.append("\r\n");
                    }
                    reader.close();
                    String result = sbf.toString();

                    // 解析json
                    if (!StringUtils.isEmpty(result)) {
                        if (log.isInfoEnabled()) log.info("baidu response result : {}", result);
                        JSONObject jsonResult = JSONObject.parseObject(result);
                        if (jsonResult != null) {
                            if (jsonResult.get("errNum") != null && Integer.parseInt(jsonResult.get("errNum").toString()) < 1 && jsonResult.get("errMsg") != null && ("SUCCESS".equalsIgnoreCase(jsonResult.get("errMsg").toString()) || "SUCCESS".equalsIgnoreCase(jsonResult.get("retMsg").toString()))) {
                                mapData = jsonResult.getJSONObject("retData");
                            } else {
                                log.error("[BAIDU]获取当前IP所属城市出错,开始调用淘宝接口-{}", result);
                                mapData = getAreaByTaoBaoIp(request);
                            }
                        } else {
                            log.error("[BAIDU]获取当前IP所属城市出错-{}", result);
                            mapData = getAreaByTaoBaoIp(request);
                        }
                    }
                } catch (Exception e) {
                    log.error("调用百度IP地址API发生异常", e);
                    mapData = getAreaByTaoBaoIp(request);
                }
            } else {
                log.error("无效IP不能获取所属城市[BAIDU]");
            }
        }
        return mapData;
    }

    /**
     * 获取当前IP所在城市[TAOBAO]
     * 只在生产环境使用
     *
     * @param request
     * @return
     */
    private static JSONObject getAreaByTaoBaoIp(HttpServletRequest request) {
        JSONObject mapData = null;
        if ("prod".equalsIgnoreCase(SpringUtils.getCtxEnvironment())) {
            String httpUrl = "http://ip.taobao.com/service/getIpInfo.php";
            String httpArg = getIpAddr(request);// 获取IP
            if (!StringUtils.isEmpty(httpArg)) {
                try {
                    BufferedReader reader = null;
                    StringBuffer sbf = new StringBuffer();
                    httpUrl = httpUrl + "?ip=" + httpArg.split(",")[0];
                    URL url = new URL(httpUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        sbf.append(strRead);
                        sbf.append("\r\n");
                    }
                    reader.close();
                    String result = sbf.toString();

                    // 解析json
                    if (!StringUtils.isEmpty(result)) {
                        if (log.isInfoEnabled()) log.info("taobao response result : {}", result);
                        JSONObject jsonResult = JSONObject.parseObject(result);
                        if (jsonResult != null) {
                            if ("0".equals(jsonResult.get("code").toString())) {
                                mapData = jsonResult.getJSONObject("data");
                            } else {
                                mapData = null;
                                log.error("[TAOBAO]获取当前IP所属城市出错-{}", result);
                            }
                        } else {
                            log.error("[TAOBAO]获取当前IP所属城市出错-{}", result);
                        }
                    }
                } catch (Exception e) {
                    log.error("调用淘宝IP地址API发生异常", e);
                }
            } else {
                log.error("无效IP不能获取所属城市[TAOBAO]");
            }
        }
        return mapData;
    }

    /**
     * 从request 获取CLIENT_ID
     *
     * @return
     */
    @SneakyThrows
    public String[] getClientId(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BASIC_)) {
            throw new RuntimeException("请求头中client信息为空");
        }
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharsetUtil.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }
}
