package hiweek.common.model;

/**
 * 服务层通用的返回结果
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */

public class ResultModel<T> {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回码:错误
     */
    public static final int RESULT_ERROR = -1;

    /**
     * 返回码:成功
     */
    public static final int RESULT_SUCCESS = 0;

    /**
     * sleuth traceId
     */
    private String traceId;

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 结果数据
     */
    private T data;

    /**
     * 设置失败返回结果
     *
     * @param resultModel 错误时服务的返回结果
     */
    public ResultModel<T> setFailed(ResultModel resultModel) {
        return setFailed(resultModel.getCode(), resultModel.getMsg());
    }

    /**
     * 设置失败返回结果
     *
     * @param code 错误码
     * @param msg  错误描述
     */
    public ResultModel<T> setFailed(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        return this;
    }

}
