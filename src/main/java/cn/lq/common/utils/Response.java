package cn.lq.common.utils;

/**
 * 返回的参数封装类
 *
 * @author winterchen
 * @date 2018/4/20
 */
public class Response<T> {

    private static final String CODE_SUCCESS = "success";

    private static final String CODE_FAIL = "fail";

    private String code;
    private T data;
    private String msg;

    public Response() {
    }

    public Response(String code) {
        this.code = code;
    }

    public Response(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Response<?> success() {
        return new Response<>(CODE_SUCCESS);
    }

    public static <R> Response<R> success(R data) {
        return new Response<>(CODE_SUCCESS, data);
    }

    public static Response<?> fail(String msg) {
        return new Response<>(CODE_FAIL, msg);
    }

    public static Response<?> widthCode(String errorCode) {
        return new Response<>(errorCode);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
