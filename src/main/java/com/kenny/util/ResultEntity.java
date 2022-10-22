package com.kenny.util;

/*
 *
 * */
public class ResultEntity<T> {
    public static final String SUCCESS = "0";
    public static final String FAIL = "-1";


    private String code;

    private String msg;

    private T data;


    public static <E> ResultEntity<E> successWithoutData() {
        return new ResultEntity<E>(SUCCESS, null, null);
    }

    public static <E> ResultEntity<E> successWithoutData(String msg) {
        return new ResultEntity<E>(SUCCESS, msg, null);
    }

    public static <E> ResultEntity<E> successWithData(E e) {
        return new ResultEntity<E>(SUCCESS, null, e);
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
