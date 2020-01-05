package hiweek.verify;

import hiweek.common.constant.CommonStatusCode;
import hiweek.common.exception.ParamException;
import hiweek.common.model.ResultModel;
import hiweek.common.util.StringUtils;
import hiweek.verify.annotation.ParamCheck;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述:
 *
 * @author wangpp-b
 * @create 2020-01-02 19:43
 */
public class ParamAspect {
    /**
     * 配置环绕通知。
     * 校验参数是否合法
     *
     * @param joinPoint 切入点
     * @return 执行结果
     */
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ResultModel resultModel = new ResultModel();
        // 1 对包含ParamCheck注解的方法进行操作
        if (method.isAnnotationPresent(ParamCheck.class)) {
            // 2 获取注解paramCheckName要校验的参数名
            ParamCheck paramCheck = method.getAnnotation(ParamCheck.class);
            String paramCheckName = paramCheck.name();
            // 3 从joinPoint中获得获取方法的所有参数名
            String[] parameterNames = methodSignature.getParameterNames();
            // 4 根据注解中要校验的参数名与joinPoint中所有的参数名，获取到要校验的数据的下标
            int paramMapIndex = ArrayUtils.indexOf(parameterNames, paramCheckName);
            if (paramMapIndex == -1) {
                resultModel.setFailed(CommonStatusCode.PARAM_INVALID, "要校验的方法参数名应与注解字段相同");
                return resultModel;
            }
            // 5 根据joinPoint获取方法的所有元素与下标，拿到真实值
            Object[] args = joinPoint.getArgs();
            Map<String, String> realParamMap = (Map<String, String>) args[paramMapIndex];
            int index = 0;
            ParamCheck.Param[] params = paramCheck.params();
            String[] paramArray = new String[params.length];
            boolean[] requiredArray = new boolean[params.length];
            Class[] classArray = new Class[params.length];
            String[] format = new String[params.length];
            // 6 获得注解中定义的校验规则
            for (ParamCheck.Param paramItem : params) {
                paramArray[index] = paramItem.name();
                requiredArray[index] = paramItem.required();
                classArray[index] = paramItem.type();
                format[index] = paramItem.format();
                index++;
            }
            try {
                StringUtils.checkParam(realParamMap, paramArray, requiredArray, classArray, format);
            } catch (ParamException e) {
                resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
                return resultModel;
            }
        }
        return joinPoint.proceed();
    }
}
