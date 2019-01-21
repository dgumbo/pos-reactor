
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import zw.co.psmi.hms.authentication.entity.Role;
import zw.co.psmi.hms.authentication.entity.User;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UserService;
import zw.co.psmi.hms.stocks.dao.ApprovalDao;
import zw.co.psmi.hms.stocks.dao.StockRequestDao;
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.ApprovalStatus;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.service.ApprovalService;


public class ApprovalServiceImplTest { 

    private ApprovalDao approvalDao;
    private UserService userService;
    private ApprovalService approvalService;
    private Approval approval;
    private StockRequestDao stockRequestDao;
    
    @Before
    public void setUp(){
        this. approvalDao = mock(ApprovalDao.class); 
        this. userService = mock(UserService.class); 
        this. stockRequestDao = mock(StockRequestDao.class); 
        this. approvalService = new ApprovalServiceImpl( approvalDao, stockRequestDao, userService);    
        
        Role role = new Role();
        role.setId(1L);
        approval = new Approval();
        approval.setId(1L); 
        approval.setApprovalStatus(ApprovalStatus.CREATED);
        approval.setRemarks("Remarks");
        approval.setRole(role);
        StockRequest stockRequest = new StockRequest();
        stockRequest.setId(1L);
        approval.setStockRequest(stockRequest);
        
        setTestingAuthentication();
    }
    
    private void setTestingAuthentication() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = () -> "USER";
        authorities.add(grantedAuthority);
        Authentication authentication = mock(Authentication.class);
        UserLogin userLogin = new UserLogin("username", "password", authorities); 
        userLogin.setUserID(1L);
        userLogin.setRoleID(1L);
        userLogin.setUnitCode("H01");
        userLogin.setIp("192.200.0.4");
        userLogin.setBroswer("firefox");
        
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getDetails()).thenReturn(userLogin);
        when(authentication.getPrincipal()).thenReturn(userLogin);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
 
    @Test
    public void testGetDao() {
        assertEquals(approvalDao, approvalService.getDao());
    }

    @Test
    public void testGetByID() {
        approvalService.getByID(1L);
        verify(approvalDao, times(1)).findOne(any(Long.class));

        when(approvalDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(approvalService.getByID(2L));
    }

    @Test
    public void testSave() {
        when(userService.getByID(any(Long.class))).thenReturn(new User())  ;  
        when(approvalDao.findOne(any(Long.class))).thenReturn(approval)  ;                
        approvalService.save(this.approval);
        verify(approvalDao, times(1)).save(any(Approval.class));  
    }
    
}
