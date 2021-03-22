package com.hubbbs.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        * authorizeRequests 所有security全注解配置实现开端
        * antMatchers("/**") 要拦截的路径  permitAll() 表示任何权限都可以访问
          anyRequest() 任何的请求 authenticated() 认证后才能访问
          and().csrf().disable();   固定写法，抵挡csrf攻击
        * */
        http
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable();
    }
}
