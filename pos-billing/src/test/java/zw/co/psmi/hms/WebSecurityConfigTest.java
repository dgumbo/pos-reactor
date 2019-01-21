
package zw.co.psmi.hms;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import zw.co.psmi.hms.authentication.dao.RoleUserUnitDao;
import zw.co.psmi.hms.authentication.dao.UserDao;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.RoleService;
import zw.co.psmi.hms.authentication.service.UnitService;


public class WebSecurityConfigTest { 

    private WebSecurityConfig webSecurityConfig;
    private UserDao userDao;
    private UnitService unitService;
    private RoleService roleService;
    private RoleUserUnitDao roleUserUnitDao;
    
    @Before
    public void setUp() { 
        this.userDao = mock( UserDao.class);
        this.                roleUserUnitDao = mock(RoleUserUnitDao.class);
        this. roleService = mock( RoleService.class);
        this.unitService = mock( UnitService.class); 
        this.webSecurityConfig = new WebSecurityConfig( userDao, roleUserUnitDao, roleService, unitService);
    
        this.setTestingAuthentication();
    }
    
    private void setTestingAuthentication() {
        List<GrantedAuthority> authorities = new ArrayList<>(); 
        authorities.add(() -> "USER");
        
        UserLogin userLogin = new UserLogin("username", "password", authorities);
        userLogin.setRoleID(1L);
        userLogin.setUserID(1L);
        userLogin.setUnitCode("H01");
        userLogin.setIp("192.200.0.4");
        userLogin.setBroswer("firefox");
        
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userLogin) ;
        when(authentication.getDetails()).thenReturn(userLogin) ; 
        when(authentication.isAuthenticated()).thenReturn(true) ; 
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @Test
    public void testUserDetailsService() {
    }

    @Test
    public void testUserService() {
    }

    @Test
    public void testBCryptPasswordEncoder() {
    }

    @Test
    public void testConfigure() throws Exception {
    }

    @Test
    public void testConfigureGlobal() throws Exception {
    }
    
}
