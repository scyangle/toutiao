package com.scy.configuration;

import com.scy.interceptor.PassportInterceptor;
import com.scy.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Shichengyao on 1/1/17.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private PassportInterceptor passportInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        super.addInterceptors(registry);
    }
}
