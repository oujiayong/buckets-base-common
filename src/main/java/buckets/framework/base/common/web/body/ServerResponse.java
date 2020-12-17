package buckets.framework.base.common.web.body;

import buckets.framework.base.common.exception.IRestStatus;
import buckets.framework.base.common.exception.RestException;
import buckets.framework.base.common.exception.RestStatus;

/**
 * controller接口返回结果封装对象
 * @author buckets
 * @date 2020/8/7
 */
public class ServerResponse {

    public static final ServerResponse SUCCESS_RESPONSE = new ServerResponse(RestStatus.SUCCESS);
    public static final ServerResponse INTERNAL_ERROR = new ServerResponse(RestStatus.SERVER_ERROR);

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码对应描述
     */
    private String message;

    /**
     * 返回数据，通常只有返回的状态码 code = 0 时才会有返回数据
     */
    private Object data;

    public ServerResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServerResponse(RestException ex) {
        this(ex.getErrorCode(), ex.getErrorMsg());
    }

    public ServerResponse(IRestStatus iRestStatus) {
        this(iRestStatus.errorCode(), iRestStatus.errorMsg());
    }

    public ServerResponse(Object data) {
        this(RestStatus.SUCCESS);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
