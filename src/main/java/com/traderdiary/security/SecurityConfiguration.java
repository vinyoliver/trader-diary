package com.traderdiary.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan(basePackages = {"com.traderdiary.rest"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService users;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;


    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/ArquillianServletRunner/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/i18n/**").permitAll()
                .antMatchers("/img/**").permitAll()

                .antMatchers("/rest/usuario/create").permitAll()

                .antMatchers("/scripts/**").permitAll()
                .antMatchers("/styles/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login.jsp")
                .defaultSuccessUrl("/")
                .permitAll();

        http.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login.jsp");

        http.csrf().disable();//TODO: Ativar/tratar cross site reference.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(users).passwordEncoder(
                new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("fonts/**");
        web.ignoring().antMatchers("i18n/**");
        web.ignoring().antMatchers("img/**");
        web.ignoring().antMatchers("scripts/**");
        web.ignoring().antMatchers("styles/**");
        web.ignoring().antMatchers("index.html");

//
//		web.ignoring().antMatchers("usuario/**");
//		web.ignoring().antMatchers("views/**");
//		web.ignoring().antMatchers("rest/usuario/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
