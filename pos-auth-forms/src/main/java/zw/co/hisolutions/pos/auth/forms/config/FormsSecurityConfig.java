package zw.co.hisolutions.pos.auth.forms.config;

//package zw.co.hisolutions.auth.config;
//
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import zw.co.hisolutions.auth.entity.MenuRoles;
//import zw.co.hisolutions.auth.service.MenuRolesService;
//
///**
// *
// * @author dgumbo
// */
//@Configuration
//@EnableWebSecurity
////@ComponentScan (basePackages = "zw.co.hisolutions.auth")
//@Order(-3)
//public class FormsSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//    //private final Environment environment;
//    private final MenuRolesService roleMenuService;
//
//    @Autowired
//    public FormsSecurityConfig(/*Environment environment,*/ UserDetailsService userDetailsService, MenuRolesService roleMenuService, PasswordEncoder passwordEncoder) {
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//        //this.environment = environment;
//        this.roleMenuService = roleMenuService; 
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
//        authBuilder
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        List<MenuRoles> menuRoles = roleMenuService.findAll();
//        http
//                .authorizeRequests()
//                .antMatchers(MATCHERS.PUBLIC_UNPROTECTED).permitAll() 
//                //.antMatchers("/admin").hasAuthority("ADMIN")
//                ;
//         
//        
//        menuRoles.forEach(menuRole -> {
//            try {
//                if (menuRole.isCreate()) {
//                    http.authorizeRequests().antMatchers(HttpMethod.PUT, menuRole.getMenu().getUri()).hasAuthority(menuRole.getRole().getAuthority());
//                }
//                if (menuRole.isEdit()) {
//                    http.authorizeRequests().antMatchers(HttpMethod.POST, menuRole.getMenu().getUri()).hasAuthority(menuRole.getRole().getAuthority());
//                }
//                if (menuRole.isDelete()) {
//                    http.authorizeRequests().antMatchers(HttpMethod.DELETE, menuRole.getMenu().getUri()).hasAuthority(menuRole.getRole().getAuthority());
//                }
//                if (menuRole.isView()) {
//                    http.authorizeRequests().antMatchers(HttpMethod.GET, menuRole.getMenu().getUri()).hasAuthority(menuRole.getRole().getAuthority());
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(FormsSecurityConfig.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//        
//        http
//                .authorizeRequests().anyRequest().authenticated()
//        .and()
//                .formLogin()
//                    .loginPage("/login")
//                .and()
//                .httpBasic(); 
//    }
//}
