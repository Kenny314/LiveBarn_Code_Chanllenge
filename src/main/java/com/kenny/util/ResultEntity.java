package com.kenny.util;

/**
 * A encapsulation class which can give a universe object return to front
 * including
 * code:0 success, other fail;
 * msg: the description of the return result
 * data: a object which always retrieved from Database
 *
 * @param <T>
 */
public class ResultEntity<T> {
    public static final String SUCCESS = "0";
    public static final String FAIL = "-1";




    private T data;

    private String code;

    private String msg;
    public static <E> ResultEntity<E> successWithoutData() {
        return new ResultEntity<E>(SUCCESS, null, null);
    }

    public static <E> ResultEntity<E> successWithoutData(String msg) {
        return new ResultEntity<E>(SUCCESS, msg, null);
    }

    public static <E> ResultEntity<E> successWithData(E e) {
        return new ResultEntity<E>(SUCCESS, null, e);
    }

    public static <E> ResultEntity<E> successWithData(E e,String msg) {
        return new ResultEntity<E>(SUCCESS, msg, e);
    }

    public static <E> ResultEntity<E> failed(String message) {
        return new ResultEntity<E>(FAIL, message, null);
    }

    public ResultEntity(String result, String msg, T data) {
        this.code = result;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
