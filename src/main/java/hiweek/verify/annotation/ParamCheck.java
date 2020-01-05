package hiweek.verify.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述: 参数校验自定义注解
 *
 * @author wangpp-b
 * @create 2020-01-02 18:43
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCheck {

    /**
     * 存放参数的变量名
     */
    String name();

    /**
     * 要校验的参数
     */
    Param[] params();

    /**
     * 自定义注解，用来描述要校验的参数信息
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Param {

        /**
         * 参数名
         */
        String name();

        /**
         * 是否必须
         */
        boolean required() default false;

        /**
         * 参数类型
         */
        Class type();

        /**
         * 参数格式
         */
        String format() default "";
    }
}
