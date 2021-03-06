package com.template.common;

/**
 * Created by Cloud on 2016/7/4.
 */
public class Result<T> {

    private boolean isSuccess = false;// 处理结果是否正确
    private int resultCode;// 处理结果Code
    private String message;// 处理信息
    private Exception exception;
    private T date;// 结果数据
    /**
     * 成功
     */
    public static int RESULT_SUCCESS = 0;
    /**
     * 失败：处理结果没有报错，但是未得到预期的结果或数据时，返回此Code
     */
    public static int RESULT_FAILURE = 1;
    /**
     * 未知错误：处理结果报错，但是无法确切定位错误来源及原因时，返回此Code
     */
    public static int RESULT_UNKNOWN_ERROR = 10;
    /**
     * 网络错误：处理时由于网络错误导致失败，返回此Code
     */
    public static int RESULT_NET_ERROR = 11;
    /**
     * 数据库错误：处理时由于数据库问题导致的失败，返回此Code
     */
    public static int RESULT_DB_ERROR = 12;

    // TODO: 2016/7/4 以后可能还需增加新的错误Code。


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    /**
     * 不带参的构造函数
     */
    public Result() {
    }

    /**
     * 带参构造函数
     *
     * @param isSuccess 标识结果是否成功
     * @param resulCode 结果码
     * @param message   消息
     * @param date      需要的消息
     */
    public Result(boolean isSuccess, int resulCode, String message, Exception exception, T date) {
        this.isSuccess = isSuccess;
        this.resultCode = resulCode;
        this.message = message;
        this.exception = exception;
        this.date = date;
    }
}