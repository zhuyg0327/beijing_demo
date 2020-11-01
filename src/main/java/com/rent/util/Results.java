package com.rent.util;

/**
 * 封装返回值
 * by zyg
 * 2020.08.23
 */
public class Results<T> {
    //返回信息
    private String message;
    //返回码
    private int retCode;
    //返回数据
    private T data;

    private Results(T data) {
        this.retCode = 0;
        this.message = "成功";
        this.data = data;
    }

    private Results(int retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }

    /**
     * 成功时调用，传入返回值
     */
    public static <T> Results<T> success(T data) {
        return new Results<T>(data);
    }

    /**
     * 成功时调用，无需返回值
     */
    public static <T> Results<T> success() {
        return (Results<T>) success("");
    }

    /**
     * 失败时调用，重新定义返回值code和信息
     */
    public static <T> Results<T> error(int retCode, String message) {
        return new Results<T>(retCode, message);
    }
}
