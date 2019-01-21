package zw.co.hisolutions.pos.auth.forms.controller;
 
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dgumbo
 */
@Controller
public class FormsLoginController   {  
     
    @GetMapping("/login")
    public ModelAndView login( ModelAndView model, CsrfToken csrf ) {
        System.out.println("zw.co.hisolutions.controllers.SecurityController.login()");
        System.out.println("_csrf : " + csrf);
                
        model.addObject("_csrf", csrf) ;
        model.setViewName("auth/login");
        return model;
    }
     
    @GetMapping("/logout")
    public ModelAndView logout( ModelAndView model ) {
        System.out.println("zw.co.hisolutions.controllers.SecurityController.logout()");
        
        model.setViewName("auth/logout");
        return model;
    }
    
}
