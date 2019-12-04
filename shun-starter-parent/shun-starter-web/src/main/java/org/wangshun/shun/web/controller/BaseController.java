package org.wangshun.shun.web.controller;

import org.wangshun.shun.core.http.R;

import cn.hutool.core.util.StrUtil;

public class BaseController {

    protected R resultHandler(String result, String success, String error) {
        if (result == null) {
            result = error;
        }
        if (StrUtil.isBlank(result)) {
            return R.success().setData(success);
        } else {
            return R.error().setMsg(result);
        }
    }
}
