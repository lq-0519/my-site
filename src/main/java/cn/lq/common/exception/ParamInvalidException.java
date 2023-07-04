package cn.lq.common.exception;

/**
 * 参数异常
 *
 * @author liqian477
 * @date 2023/06/30 14:38
 */
public class ParamInvalidException extends RuntimeException {
    public ParamInvalidException() {
    }

    public ParamInvalidException(String message) {
        super(message);
    }

    public ParamInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamInvalidException(Throwable cause) {
        super(cause);
    }

}
