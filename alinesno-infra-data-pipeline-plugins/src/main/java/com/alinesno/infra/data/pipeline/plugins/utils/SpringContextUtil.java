package com.alinesno.infra.data.pipeline.plugins.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class SpringContextUtil {
 
    // Spring应用上下文环境
    @Getter
    private static ApplicationContext applicationContext;
 
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public static void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     *  根据beanId返回Spring中的实例
     * @Date 2019-08-07 17:36
     * @param
     * @return
     **/
    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }
}