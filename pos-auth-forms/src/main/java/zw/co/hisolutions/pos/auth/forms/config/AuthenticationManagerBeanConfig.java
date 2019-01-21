package zw.co.hisolutions.pos.auth.forms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author dgumbo
 */
@Configuration
@Order(0)
public class AuthenticationManagerBeanConfig extends WebSecurityConfigurerAdapter {      
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { 
        http
                .csrf().disable()
                .cors().disable()
                ; 
    }
}
