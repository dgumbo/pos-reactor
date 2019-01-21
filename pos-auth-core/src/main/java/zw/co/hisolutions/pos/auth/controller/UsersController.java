package zw.co.hisolutions.pos.auth.controller;
 
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.auth.entity.User;
import zw.co.hisolutions.pos.auth.service.UsersService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/users")
public class UsersController   {  
    private final UsersService usersService ;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    } 
     
    @GetMapping("/getAll")
    public List<User> login( ) {
        System.out.println("zw.co.hisolutions.auth.controller.UsersController.login()");
        
        return usersService.findAll() ;
    } 
}
