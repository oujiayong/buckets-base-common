package buckets.framework.base.common.web;

import buckets.framework.base.common.exception.RestException;
import buckets.framework.base.common.exception.RestStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author buckets
 * @date 2020/8/6
 */
public class BaseRestController {

    public BaseRestController() {
    }

    @ExceptionHandler({RestException.class})
    public RestException processApiException(RestException e) {
        return e;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public RestException processApiException(MethodArgumentNotValidException e) {
        List<String> msgList = new ArrayList<>();
        String errMsg = RestStatus.INVALID_MODEL_FIELDS.errorMsg();

        for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
            msgList.add(objectError.getDefaultMessage());
        }

        if (msgList.size() > 0) {
            errMsg = msgList.get(0);
        }

        return new RestException(RestStatus.INVALID_MODEL_FIELDS.errorCode(), errMsg, Arrays.toString(msgList.toArray()));
    }

    @ExceptionHandler({BindException.class})
    public RestException doBindException(BindException e) {
        List<String> msgList = new ArrayList<>();

        for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
            msgList.add(objectError.getDefaultMessage());
        }

        return new RestException(RestStatus.INVALID_PARAMS_CONVERSION, Arrays.toString(msgList.toArray()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public RestException doHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        if (msg != null && msg.startsWith("JSON parse error")) {
            String start = "field : ";
            String end = ";";
            msg = "字段[" + msg.substring(msg.indexOf(start) + start.length(), msg.indexOf(end)) + "]解析出错";
            return new RestException(RestStatus.INVALID_JSON_PARSE, msg);
        } else {
            return new RestException(msg, e);
        }
    }

}
