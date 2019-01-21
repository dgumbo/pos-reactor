
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.stocks.dao.BookStockDao;
import zw.co.psmi.hms.stocks.dao.BookStockLineDao;
import zw.co.psmi.hms.stocks.dao.GenericStockTypeDao;
import zw.co.psmi.hms.stocks.dao.StockTransactionDao;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.BookStockLine;
import zw.co.psmi.hms.stocks.entity.BookStockLockView;
import zw.co.psmi.hms.stocks.entity.GenericStockType;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;


@Ignore
public class BookStockServiceImplTest {

    private GenericStockTypeService genericStockTypeService;
    private LocationDao locationDao;
    private GenericStockTypeDao genericStockTypeDao;
    private BookStockDao bookStockDao;
    private BookStockLineDao bookStockLineDao;
    private BookStockService bookStockService;
    private BookStock bookStock;
    private StockTransactionDao stockTransactionDao;

    @Before
    public void setUp() {
        this.bookStockLineDao = mock(BookStockLineDao.class);
        this.bookStockDao = mock(BookStockDao.class);
        this.genericStockTypeDao = mock(GenericStockTypeDao.class);
        this.locationDao = mock(LocationDao.class);
        this.genericStockTypeService = mock(GenericStockTypeService.class);
        this.stockTransactionDao =mock(StockTransactionDao.class);
        this.bookStockService = new BookStockServiceImpl( locationDao, genericStockTypeDao, bookStockDao, bookStockLineDao, genericStockTypeService, stockTransactionDao);

      //  this.setTestingAuthentication() ;

        bookStock = new BookStock();
    }

//    private void setTestingAuthentication() {
//        List<GrantedAuthority> authorities = new ArrayList<>(); 
//        authorities.add(() -> "USER");
//        
//        UserLogin userLogin = new UserLogin("username", "password", authorities);
//        userLogin.setRoleID(1L);
//        userLogin.setUserID(1L);
//        userLogin.setUnitCode("H01");
//        userLogin.setIp("192.200.0.4");
//        userLogin.setBroswer("firefox");
//        
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(userLogin) ;
//        when(authentication.getDetails()).thenReturn(userLogin) ; 
//        when(authentication.isAuthenticated()).thenReturn(true) ; 
//        
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    } 
    @Test
    public void testFindAllActive() {
        bookStockService.findAllActive();
        verify(bookStockDao, times(1)).findByActiveStatusTrue();
    }

    @Test
    public void testGetDao() {
        assertEquals(bookStockDao, bookStockService.getDao());
    }

    @Test
    public void testGetByID() {
        when(bookStockDao.findOne(any(Long.class))).thenReturn(bookStock);

        bookStockService.getByID(1L);
        verify(bookStockDao, times(1)).findOne(any(Long.class));
    }

    @Test
    public void testFindAll() {
        bookStockService.findAll();

        verify(bookStockDao, times(1)).findAll();
    }

    @Test
    public void testFindByUser() {
        bookStockService.findByUser("user");
        verify(bookStockDao, times(1)).findByCreatedByUser(any(String.class));

        //when(stockSubGroupDao.findByStockGroupIdAndActiveStatusTrue(any(Long.class))).thenReturn(null);
        //assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testFindByBookStockDate() {
        bookStockService.findByBookStockDate(new Date());
        verify(bookStockDao, times(1)).findByBookStockDate(any(Date.class));

        //when(stockSubGroupDao.findByStockGroupIdAndActiveStatusTrue(any(Long.class))).thenReturn(null);
        //assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testSave() {
        List<BookStockLine> list = new ArrayList<>();
        list.add(new BookStockLine());
        bookStockService.save(new BookStock(), list);

        verify(bookStockDao, times(1)).save(any(BookStock.class));
        verify(bookStockLineDao, times(1)).save(any(ArrayList.class));

        try {
            bookStockService.save(null, null);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().equals("No lines Selected for this book stock"));
        }
    }

    @Test
    public void testGetBookStockLinesList() {
        GenericStockType genericStockType = new GenericStockType();
        Location location = new Location();

        when(genericStockTypeService.getByID(any(Long.class))).thenReturn(genericStockType);
        when(locationDao.findOne(any(Long.class))).thenReturn(location);

        List<BookStockLockView> bookStockLockViewList = new ArrayList();
        BookStockLockView bookStockLockView = new BookStockLockView();
        bookStockLockView.setLock(true);
        bookStockLockView.setGenericStockTypeId(1L);
        bookStockLockView.setLocationId(1L);
        bookStockLockViewList.add(bookStockLockView);
        bookStockService.getBookStockLinesList(bookStockLockViewList);
        // verify(genericStockTypeService, times(1)).getByID(any(Long.class));

        //when(stockSubGroupDao.findByStockGroupIdAndActiveStatusTrue(any(Long.class))).thenReturn(null);
        //assertNotNull(stockGroupService.getByID(2L));
    }

}
