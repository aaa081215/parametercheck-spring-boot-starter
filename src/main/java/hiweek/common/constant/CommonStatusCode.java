package hiweek.common.constant;

/**
 * 通用状态码(1~99999)
 *
 * @author wangpengpeng
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommonStatusCode {

    /**
     * hystrix fallback(9999)
     */
    public static final int HYSTRIX_FALLBACK = 9999;

    /**
     * 通用状态码起始偏移
     */
    private static final int COMMON_STATUS_OFFSET = 10000;

    /**
     * 参数不合法:(10001)
     */
    public static final int PARAM_INVALID = COMMON_STATUS_OFFSET + 1;


}
