package com.tutorial.booking.system.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration
@ComponentScan
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/home/**").hasAnyRole( "STAFF", "STUDENT")
                .antMatchers("/event/**").hasAnyRole( "STAFF", "STUDENT")
                .antMatchers("/user/edit/times", "/user/edit/room").hasRole("STAFF")
                .antMatchers("/user/**").hasAnyRole("STAFF", "STUDENT")
                .antMatchers("/staff/**", "/search").hasAnyRole("STAFF", "STUDENT")
                .antMatchers("/notifications/**").hasAnyRole("STAFF", "STUDENT")

                .antMatchers("/", "/registration", "/login").permitAll()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/home")
                .and().logout().invalidateHttpSession(true).clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){return new BCryptPasswordEncoder(); }
}
