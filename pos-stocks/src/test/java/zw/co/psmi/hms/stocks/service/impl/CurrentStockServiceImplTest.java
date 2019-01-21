
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.stocks.dao.BookStockLockViewDao;
import zw.co.psmi.hms.stocks.dao.CurrentStockDao;
import zw.co.psmi.hms.stocks.dao.CurrentStockViewDao;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.service.CurrentStockService;


public class CurrentStockServiceImplTest {

    private CurrentStockDao currentStockDao;
    private BookStockLockViewDao bookStockLockViewDao;
    private CurrentStockService currentStockService;
    private CurrentStockViewDao currentStockViewDao;

    @Before
    public void setUp() {
        this.currentStockDao = mock(CurrentStockDao.class);
        this.bookStockLockViewDao = mock(BookStockLockViewDao.class);
        this.currentStockViewDao=mock(CurrentStockViewDao.class);
        
        
        this.currentStockService = new CurrentStockServiceImpl(currentStockDao, bookStockLockViewDao,currentStockViewDao);
        
       // this.setTestingAuthentication() ;
    }

    @Test
    public void testGetDao() {
        assertEquals(currentStockDao, currentStockService.getDao());
    }

    @Test
    public void testSave() {
        assertEquals("Sucessfully saved CurrentStock", currentStockService.save(new CurrentStock()));
        verify(currentStockDao, times(1)).save(any(CurrentStock.class));
    }

    @Test
    public void testFindAll() {
        currentStockService.findAll();
        verify(currentStockDao, times(1)).findAll();
    }

    @Test
    public void testFindAllAvailable() {
        assertThat(currentStockService.findAllAvailable(), hasSize(0));
       // verify(currentStockDao, times(0)).findByUnitUnitCodeAndStockStatusOrderByStockTypeGenericStockTypeName(any(String.class), any(StockStatus.class));

        List<CurrentStock> currentStockList = new ArrayList();
        CurrentStock currentStock = new CurrentStock();
        currentStockList.add(currentStock);
        when(currentStockDao.findByUnitUnitCodeAndStockStatusOrderByStockTypeGenericStockTypeName(any(String.class), eq(StockStatus.AVAILABLE))).thenReturn(currentStockList);

        this.setTestingAuthentication();
        assertThat(currentStockService.findAllAvailable(), hasSize(1));
//        verify(currentStockDao, times(1)).findByUnitUnitCodeAndStockStatusOrderByStockTypeGenericStockTypeName(any(String.class), eq(StockStatus.AVAILABLE));
    }

//    @Test
//    public void testFindAllAvailableByLocation() { 
//        assertThat(currentStockService.findAllAvailableByLocation(null), hasSize(0));
//        verify(currentStockDao, times(0)).findByUnitUnitCodeAndLocationAndStockStatusOrderByStockTypeName(any(String.class), any(Location.class), eq(StockStatus.AVAILABLE));
//
//        List<CurrentStock> currentStockList = new ArrayList();
//        CurrentStock currentStock = new CurrentStock();
//        currentStockList.add(currentStock);
//        when(currentStockDao.findByUnitUnitCodeAndLocationAndStockStatusOrderByStockTypeGenericStockTypeName(any(String.class), any(Location.class), eq(StockStatus.AVAILABLE))).thenReturn(currentStockList);
//
//        this.setTestingAuthentication();
//        assertThat(currentStockService.findAllAvailableByLocation(new Location()), hasSize(1));
//        verify(currentStockDao, times(1)).findByUnitUnitCodeAndLocationAndStockStatusOrderByStockTypeName(any(String.class), any(Location.class), eq(StockStatus.AVAILABLE));
//    }
//
//    @Test 
//    public void testGetBookStockLockView() { 
//        removeTestingAuthentication();
//        
//        List<BookStockLockView> bookStockLockViewList = new ArrayList();
//        when(bookStockLockViewDao.getBookStockLockView(any(String.class))).thenReturn(bookStockLockViewList);
//        assertThat(currentStockService.getBookStockLockView(), hasSize(0));
//        verify(currentStockDao, times(0)).findByUnitUnitCodeAndLocationAndStockStatusOrderByStockTypeName(any(String.class), any(Location.class), eq(StockStatus.AVAILABLE));
//
//        BookStockLockView bookStockLockView = null ;
//        bookStockLockViewList.add(bookStockLockView);
//
//        this.setTestingAuthentication();
//        assertThat(currentStockService.getBookStockLockView(), hasSize(1));
//        verify(bookStockLockViewDao, times(1)).getBookStockLockView(any(String.class));
 
//    }
//
//    @Test(expected = Exception.class)
//    public void testGetBookStockLockView_WithException() {     
//        this.setTestingAuthenticationThrowError();
//        assertThat(currentStockService.findAllAvailable(), hasSize(0));
//        
//        this.setTestingAuthentication() ;
//    }

    @Test
    public void testFindByBookStock() { 
        List<CurrentStock> currentStockList = new ArrayList();
        CurrentStock currentStock = new CurrentStock();
        currentStockList.add(currentStock);
        when(bookStockLockViewDao.findByBookStock(any( BookStock.class))).thenReturn(currentStockList);

        assertThat (currentStockService.findByBookStock(new BookStock()), hasSize(1));
        verify(bookStockLockViewDao, times(1)).findByBookStock(any( BookStock.class));
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
    
       private void removeTestingAuthentication() {          
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null) ;
        when(authentication.getDetails()).thenReturn(null) ; 
        when(authentication.isAuthenticated()).thenReturn(false) ; 
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    } 
    
       private void setTestingAuthenticationThrowError() {          
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenThrow(new Exception()) ;
        when(authentication.getDetails()).thenThrow( new Exception()) ;
        when(authentication.isAuthenticated()).thenThrow( new Exception()) ; 
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    } 
}
