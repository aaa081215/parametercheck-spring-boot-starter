package hiweek.common.util;


import hiweek.common.exception.ParamException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具类
 *
 * @author wangpengpeng
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringUtils {

    /**
     * 字母数字
     */
    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 数字
     */
    private static final String DIGIT = "0123456789";

    /**
     * rest格式的url。如：/a/{b}/{c}
     */
    private static Pattern PATTERN_REST_URL = Pattern.compile("\\{[^\\}]*\\}");

    /**
     * 整数(byte,short,int,long)正则表达式
     */
    public static final Pattern PATTERN_INT = Pattern.compile("-?[0-9]+");

    /**
     * 浮点型(float,double,decimal)正则表达式
     */
    public static final Pattern PATTERN_FLOAT = Pattern.compile("-?[0-9]+(.[0-9]+)?");

    /**
     * 验证字符串是否为空
     *
     * @param str 字串
     * @return 是否为空(null或 " ")
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 随机获取指定长度的字串
     *
     * @param length 字串长度
     * @param digit  是否是随机的数字(true:数字;false:包含字母)
     */
    public static String random(int length, boolean digit) {
        String base = digit ? DIGIT : ALPHANUM;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(length % base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 使用参数填充rest格式的url
     *
     * @param restUrl    rest格式的url。如：/a/{b}/{c}
     * @param pathParams 要填充rest占位符的参数
     * @return 使用指定参数填充后的restUrl
     */
    public static String parseRestUrl(String restUrl, Object... pathParams) {
        Matcher matcher = PATTERN_REST_URL.matcher(restUrl);
        int index = 0;
        while (matcher.find()) {
            try {
                restUrl = restUrl.replace(matcher.group(), URLEncoder.encode(pathParams[index++].toString(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return restUrl;
    }

    /**
     * 使用参数填充rest格式的url
     *
     * @param restUrl    rest格式的url。如：/a/{b}/{c}
     * @param params     请求消息行后面携带的参数"?a=xxx&b=xxx"
     * @param pathParams 要填充rest占位符的参数
     * @return 使用指定参数填充后的restUrl
     */
    public static String parseRestUrl(String restUrl, Map<String, String> params, Object... pathParams) {
        StringBuilder sb = new StringBuilder(parseRestUrl(restUrl, pathParams));
        if (params.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (i == 0) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * 将字串转换为指定类型，只支持Integer,Float,Double,java.util.Date四种类型
     *
     * @param targetClass 目标类型，只支持String,Integer,Long,Float,Double,java.util.Date六种类型
     * @param src         要转换的字串
     * @param dateFormat  java.util.Date类型必须填写要转换的格式
     * @return 转换后的数据
     * @throws NumberFormatException 数字转换异常
     * @throws ParseException        日期解析异常
     * @throws ParamException        参数异常
     */
    public static Object convert(Class targetClass, String src, String dateFormat) throws NumberFormatException,
            ParseException, ParamException {
        if (targetClass == String.class) {
            return src;
        } else if (targetClass == Integer.class) {
            return Integer.valueOf(src);
        } else if (targetClass == Long.class) {
            return Long.valueOf(src);
        } else if (targetClass == Float.class) {
            return Float.valueOf(src);
        } else if (targetClass == Double.class) {
            return Double.valueOf(src);
        } else if (targetClass == Date.class) {
            return new SimpleDateFormat(dateFormat).parse(src);
        } else {
            throw new ParamException("不支持将数据转换为" + targetClass + "类型");
        }
    }

    /**
     * 参数检查。检查对应的参数是否存在及类型是否合法，不符合时抛出ParamException异常
     * 参数名称，是否必须，类型数组必须一一对应。
     * 建议将日期类型放在数组前几位，这样日期格式数组可以只填写前几位
     *
     * @param params     参数列表
     * @param paramNames 参数名称数组
     * @param required   参数是否必须
     * @param type       要转换的类型,只支持String,Integer,Long,Float,Double,java.util.Date六种类型
     * @param dateFormat 如果类型为日期，必须填写日期格式
     * @return 转换结果
     * @throws ParamException 参数检查未通过时抛异常
     */
    public static void checkParam(Map<String, String> params, String[] paramNames, boolean[] required,
                                  Class[] type, String[] dateFormat) throws ParamException {
        /*
         * 检查paramNames,required,type的数组长度是否一致
         */
        if (!(paramNames.length == required.length
                && required.length == type.length)) {
            throw new ParamException("paramNames,required,type的数组长度必须一致");
        }

        /*
         * 1)参数校验。参数为空,但必填,则抛出异常
         * 2)移除非必填参数。参数为空,非必填,则从params中移除该参数键值对
         * 3)只收集paramNames声明的参数，保存到filterParams中
         */
        Map<String, String> filterParams = new HashMap<>(20);
        for (int i = 0; i < paramNames.length; i++) {
            String paramValue = params.get(paramNames[i]);
            // 1)参数为空,但必填,则抛出异常
            if (required[i] && StringUtils.isEmpty(paramValue)) {
                throw new ParamException("参数[" + paramNames[i] + "]不能为空");
            }

            // 2)参数为空,非必填,则从params中移除该参数键值对,跳出本次循环
            if (!required[i] && StringUtils.isEmpty(paramValue)) {
                params.remove(paramNames[i]);
                continue;
            }

            // 参数不为空,检查数据类型
            if (!StringUtils.isEmpty(paramValue)) {
                try {
                    convert(type[i], paramValue, (type[i] == Date.class) ? dateFormat[i] : null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ParamException("参数[" + paramNames[i] + "]类型不合法");
                }
            }

            // 3)只收集paramNames声明的参数
            filterParams.put(paramNames[i], paramValue);
        }

        // 将过滤后的参数保存到params
        params.clear();
        params.putAll(filterParams);
    }

    /**
     * 从字符串析取整数，析取失败则返回指定的默认值
     *
     * @param input        待转化为整数的字符串
     * @param defaultValue 默认值
     * @return int类型
     */
    public static int parseInt(String input, int defaultValue) {
        if (isEmpty(input) || !PATTERN_INT.matcher(input).matches()) {
            return defaultValue;
        }
        return Integer.parseInt(input);
    }

    /**
     * 从字符串析取整数，析取失败则返回指定的默认值
     *
     * @param input        待转化为整数的字符串
     * @param defaultValue 默认值
     * @return long类型
     */
    public static long parseLong(String input, long defaultValue) {
        if (isEmpty(input) || !PATTERN_INT.matcher(input).matches()) {
            return defaultValue;
        }
        return Long.parseLong(input);
    }

    /**
     * 检查ip是否与正则表达式匹配,多个正则表达式使用","分隔。
     * regex:192.168.*.1
     * ip:192.168.1.1
     *
     * @param regex 正则表达式
     * @param ip    ip地址
     * @return ip是否与正则表达式匹配
     */
    public static boolean matches(String regex, String ip) {
        String[] patterns = regex.split(",");
        for (String pattern : patterns) {
            if (ip.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
