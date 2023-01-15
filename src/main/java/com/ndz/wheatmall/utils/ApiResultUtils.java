package com.ndz.wheatmall.utils;

import com.ndz.wheatmall.common.bean.ApiResult;
import com.ndz.wheatmall.common.enums.StateEnum;
import com.ndz.wheatmall.common.enums.StatusCode;

/**
 * 统一返回工具类
 */
public class ApiResultUtils {
    public ApiResultUtils() {}

    public static <T> ApiResult<T> makeCustomizeMsg(int status, String msg, T data) {
        ApiResult<T> rm = new ApiResult<>(status, msg);
        rm.setData(data);
        return rm;
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return new ApiResult<>(StateEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ApiResult<T> ok(String message) {
        return new ApiResult<>(StateEnum.SUCCESS.getCode(), message,null);
    }

    public static <T> ApiResult<T> ok(T data) {
        ApiResult<T> rm = new ApiResult<>(StateEnum.SUCCESS);
        rm.setData(data);
        return rm;
    }

    /**
     * 对未进行错误枚举定义的使用关闭
     */
    public static <T> ApiResult<T> error(StatusCode errorStatusCode, T data) {
        return new ApiResult<>(errorStatusCode);
    }

    public static <T> ApiResult<T> error(Integer code, String msg, T data) {
        return new ApiResult<>(code, msg, data);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(StateEnum.FAILED.getCode(), message, null);
    }

    public static <T> ApiResult<T> error() {
        return new ApiResult<>(StateEnum.FAILED);
    }
}
