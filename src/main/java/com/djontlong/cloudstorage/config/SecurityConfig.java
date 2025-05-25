package com.djontlong.cloudstorage.config;

import com.djontlong.cloudstorage.services.AuthenticationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // разрешить доступ к указанным страницам/папкам без авторизации
        http.authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated();

        // настройка формы входа и указание кастомной страницы входа
        http.formLogin()
                .loginPage("/login")
                .permitAll();

        // после успешного входа перенаправлять пользователя на указанную страницу
        http.formLogin()
                .defaultSuccessUrl("/home", true);

        // настройка формы выхода (logout)
        http.logout()
		.permitAll();
        
    }
    


}