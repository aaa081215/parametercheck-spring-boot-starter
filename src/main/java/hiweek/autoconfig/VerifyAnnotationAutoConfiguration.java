package hiweek.autoconfig;

import hiweek.verify.ParamAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存切面配置
 *
 * @author wangpengpeng
 * @version 1.0.0
 * @since 1.0.0
 */
@Aspect
@Configuration
public class VerifyAnnotationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ParamAspect paramAspect() {
        return new ParamAspect();
    }

    /**
     * aop切入点
     */
    @Around("@annotation(hiweek.verify.annotation.ParamCheck)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return paramAspect().around(joinPoint);
    }

}
