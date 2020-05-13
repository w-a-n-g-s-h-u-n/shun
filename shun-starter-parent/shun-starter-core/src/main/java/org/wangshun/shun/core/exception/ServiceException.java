package org.wangshun.shun.core.exception;

import cn.hutool.core.util.StrUtil;

public class ServiceException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "系统异常";
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super(DEFAULT_MESSAGE);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Object... params) {
        super(StrUtil.format(message, params));
    }

    public ServiceException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
