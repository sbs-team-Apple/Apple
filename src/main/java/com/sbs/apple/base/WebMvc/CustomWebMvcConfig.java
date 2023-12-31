package com.sbs.apple.base.WebMvc;

import com.sbs.apple.base.app.AppConfig;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;



import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/gen/**")
                .addResourceLocations("file:///" + AppConfig.getGenFileDirPath() + "/");
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthenticationInterceptor())
//                .addPathPatterns("/**") // Apply interceptor to all paths
//                .excludePathPatterns("/", "/login", "/register","/img/**","/showWarningPage"); // Exclude specific paths if needed
//    }

    @GetMapping("/showWarningPage")
    public String showWarningPage() {
        // Logic to display the warning page
        return "warningPage"; // Assuming "warningPage" is the name of the view/template to display the warning
    }


}

