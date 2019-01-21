package zw.co.hisolutions.pos.init.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;  

/**
 *
 * @author dgumbo
 */
@Controller
public class InitUsersController {
    
//    @Autowired
//    UsersService userService;
    
    @GetMapping("/init/users")
    @ResponseBody
    public String init(){
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
//        List <UserRole> userAuthorities = new ArrayList(); 
//        UserRole userAuth = userService.createAuthority(new UserRole("USER"));
//        userAuthorities.add(userAuth);
         
//        User user = new User();
//        user.setActiveStatus(true);
//        user.setEnabled(true);
//        user.setPassword(encoder.encode("user"));
//        user.setUsername("user");
//        user.setRoles(userAuthorities);
//        userService.createUser(user);
                
//        User admin = new User();
//        admin.setActiveStatus(true);
//        admin.setEnabled(true);
//        admin.setPassword(encoder.encode("admin"));
//        admin.setUsername("admin");
        
//        List <UserRole> adminAuthorities = new ArrayList(); 
//        UserRole authorityADMIN = userService.createAuthority(new UserRole("ADMIN"));
//        adminAuthorities.add(userAuth);
//        adminAuthorities.add(authorityADMIN);
//        admin.setRoles(adminAuthorities);
//        userService.createUser(admin);
        
        return "Successiful";
    }
}
