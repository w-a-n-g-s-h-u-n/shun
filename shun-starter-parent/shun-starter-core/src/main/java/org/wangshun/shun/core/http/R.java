package org.wangshun.shun.core.http;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class R {
    public static final Integer SYS_SYSTEM = 0;

    public static final Integer CODE_SUCCESS = SYS_SYSTEM + 0;
    public static final String MSG_SUCCESS = "success";
    public static final String DATA_SUCCESS = "成功";

    public static final Integer CODE_ERROR = SYS_SYSTEM + 1;
    public static final String MSG_ERROR = "系统错误";
    public static final String DATA_ERROR = "error";

    private Integer code;
    private String msg;
    private Object data;

    public static R of(Boolean success) {
        if (null != success && success) {
            return success();
        }
        return error();
    }

    public static R success() {
        return new R(CODE_SUCCESS, MSG_SUCCESS, DATA_SUCCESS);
    }

    public static R success(Object data) {
        return new R(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    public static R build(boolean success) {
        return success ? success() : error();
    }

    public static R error() {
        return new R(CODE_ERROR, MSG_ERROR, DATA_ERROR);
    }

    public static R error(Exception e) {
        return R.error(e.getLocalizedMessage());
    }

    public static R error(String msg) {
        return new R(CODE_ERROR, msg, DATA_ERROR);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
