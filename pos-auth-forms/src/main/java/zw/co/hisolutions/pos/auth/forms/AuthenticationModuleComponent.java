package zw.co.hisolutions.pos.auth.forms;
 
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
  
@Configuration 
@ComponentScan //(basePackages = "zw.co.hisolutions.auth")
public @interface AuthenticationModuleComponent {
  
}
