package buckets.framework.base.common.web;

import buckets.framework.base.common.exception.IRestStatus;
import buckets.framework.base.common.exception.RestException;
import buckets.framework.base.common.exception.RestStatus;
import buckets.framework.base.common.web.body.ServerResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author buckets
 * @date 2020/8/7
 */
@Configuration
@ControllerAdvice(assignableTypes = {BaseRestController.class})
public class RestResponseBody implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(RestResponseBody.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Class clz = methodParameter.getParameterType();
        return !ServerResponse.class.isAssignableFrom(clz);
    }

    @Override
    public ServerResponse beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (object == null) {
            return ServerResponse.SUCCESS_RESPONSE;
        }

        if (object instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            return new ServerResponse(RestStatus.INVALID_PARAMS_CONVERSION.errorCode(),
                    String.format("%s参数,类型%s缺失", ((org.springframework.web.bind.MissingServletRequestParameterException) object).getParameterName(),
                            ((org.springframework.web.bind.MissingServletRequestParameterException) object).getParameterType()));
        } else if (object instanceof RestException) {
            RestException exec = (RestException) object;
            if (exec.getErrorCode() != 0) {
                log.error("出现错误：" + exec.getErrorMsg());
            }
            return new ServerResponse(exec);
        } else if (object instanceof Exception) {
            return ServerResponse.INTERNAL_ERROR;
        } else if (object instanceof IRestStatus) {
            IRestStatus restStatus = (IRestStatus) object;
            return new ServerResponse(restStatus.errorCode(), restStatus.errorMsg());
        } else {
            return new ServerResponse(object);
        }
    }
}
