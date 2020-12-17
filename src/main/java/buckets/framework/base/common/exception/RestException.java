package buckets.framework.base.common.exception;

/**
 * @author buckets
 * @date 2020/8/7
 */
public class RestException extends RuntimeException {

    private String errorMsg;
    private int errorCode = -1;
    private String error;

    public RestException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public RestException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public RestException(int errorCode, String errorMsg, String error) {
        this(errorCode,errorMsg);
        this.error = error;
    }

    public RestException(IRestStatus restStatus) {
        this(restStatus.errorCode(),restStatus.errorMsg());
    }

    public RestException(IRestStatus restStatus, String error) {
        this(restStatus);
        this.error = error;
    }

    public RestException(String errorMsg, Exception e) {
        this(errorMsg);
        this.setError(e);
    }

    private void setError(Exception e) {
        this.errorCode = RestStatus.SERVER_ERROR.errorCode();
        if (this.errorMsg == null) {
            this.errorMsg = RestStatus.SERVER_ERROR.errorMsg();
        }
        this.error = e.toString();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }
}
