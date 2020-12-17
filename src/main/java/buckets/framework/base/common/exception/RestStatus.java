package buckets.framework.base.common.exception;

/**
 * @author buckets
 * @date 2020/8/7
 */
public enum RestStatus implements IRestStatus {

    /**
     * 状态码及对应信息
     */
    SUCCESS(0,"执行成功"),
    SERVER_ERROR(50000,"系统繁忙，请稍后再试"),

    INVALID_MODEL_FIELDS(40001, "字段校验非法"),
    INVALID_PARAMS_CONVERSION(40002, "参数类型非法"),
    INVALID_JSON_PARSE(40003, "JSON解释出错");


    private int errorCode;
    private String errorMsg;
    RestStatus(int errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }
    @Override
    public String errorMsg() {
        return errorMsg;
    }
}
