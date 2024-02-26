package com.SportRentalInventorySystem.BackEnd.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.SportRentalInventorySystem.BackEnd.Security.jwt.AuthEntryPointJwt;
import com.SportRentalInventorySystem.BackEnd.Security.jwt.AuthTokenFilter;
import com.SportRentalInventorySystem.BackEnd.Security.services.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
         jsr250Enabled = true,
        prePostEnabled = true)
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()
            //These are public paths everyone has access to this path
            .antMatchers("/resources/**",  "/error", "/api/account/**" ,"/api/payment/**","/api/Reservation/**").permitAll()  
            .antMatchers("/api/public/**","/api/Reservation/**").permitAll()         
            //ManagerController will access admin content
            .antMatchers("/api/product/**", "/api/category/**","/api/Customer/**","/api/users/*","/api/AdminDashboard/**").hasAuthority("ROLE_ADMIN")
            //StaffController will access Staff content
            .antMatchers("/api/staff/*","/api/users/*").hasAuthority("ROLE_MODERATOR")
            //UserController will access logged in users content
            .antMatchers("/api/users/*").hasAuthority("ROLE_USER")
            .anyRequest().authenticated() 
            .and() 
            .rememberMe().userDetailsService(this.userDetailsService)
            .and()
            //logout will log the user out by invalidated session.
            .logout().permitAll()
            .logoutRequestMatcher(new AntPathRequestMatcher("/api/users/logout", "POST"))
            .and()
            //login form and path
            .formLogin().loginPage("/api/users/login")
             .usernameParameter("username").passwordParameter("password")
            .defaultSuccessUrl("/")
            .permitAll()
           
           
           .and()
            //enable basic authentication
           .httpBasic();
       
        //.and()
            //We will handle it later.
            //Cross side request forgery
        //   .csrf().disable()
        
         //  .authorizeRequests().antMatchers("/resources/**",  "/error", "/api/users/**").permitAll()
           // .antMatchers("/api/test/**").permitAll()
           // .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}