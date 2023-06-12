package cn.lq.web.handler;

import cn.lq.common.exception.BusinessException;
import cn.lq.common.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 * Created by winterchen on 2018/4/20.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Response businessException(Exception e) {

        String msg = "请求错误";
        if (e instanceof BusinessException) {
            msg = ((BusinessException) e).getErrorCode();
        }
        logger.error("find exception:e={}", e.getMessage());
        e.printStackTrace();
        return Response.fail(msg);
    }
//
//    @ExceptionHandler(value = Exception.class)
//    public String exception(Exception e){
//        logger.error("find exception:e={}",e.getMessage());
//        e.printStackTrace();
//        return "error/400";
//    }

}
