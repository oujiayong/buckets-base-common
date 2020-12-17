package buckets.framework.base.common.exception;

/**
 * @author buckets
 * @date 2020/8/7
 */
public interface IRestStatus {

    /**
     * 状态码
     * @return code
     */
    int errorCode();

    /**
     * 异常信息
     * @return msg
     */
    String errorMsg();
}
