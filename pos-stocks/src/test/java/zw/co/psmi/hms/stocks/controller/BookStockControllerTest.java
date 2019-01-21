
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.BookStockLine;
import zw.co.psmi.hms.stocks.entity.BookStockLockView;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService; 
import zw.co.psmi.hms.stocks.service.StockCountService;


public class BookStockControllerTest {
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService; 
    private CurrentStockService currentStockService;
    private BookStockService bookStockService;
    private LocationDao locationDao;

    private MockMvc mockMvc;
    private List<BookStock> bookStockList;
    private BookStock bookStock;
    private Unit unit;
    private Location location; 
    private List<Location> locations;
    private List<Unit> units; 
    private StockCountService stockCountService;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.unitService = mock(UnitService.class);
        this.genericStockTypeService = mock(GenericStockTypeService.class); 
        this.currentStockService = mock(CurrentStockService.class);
        this.bookStockService = mock(BookStockService.class);
        this.locationDao = mock(LocationDao.class);
        this.stockCountService=mock(StockCountService.class);;
        BookStockController receiptControllerController = new BookStockController(stockCountService, bookStockService, genericStockTypeService, unitService, locationDao, currentStockService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(receiptControllerController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.bookStockList = new ArrayList<>();
        this.bookStock = new BookStock();
        bookStock.setId(1L);
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit); 
        bookStockList.add(bookStock);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);    

        when(bookStockService.getByID(any(Long.class))).thenReturn(bookStock);
    }

    private final HandlerMethodArgumentResolver putPrincipal = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserLogin.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            List<GrantedAuthority> authorities = new ArrayList<>();
            GrantedAuthority grantedAuthority = () -> "USER";
            authorities.add(grantedAuthority);
            UserLogin loggedUser = new UserLogin("username", "password", authorities);
            loggedUser.setRoleID(1L);
            loggedUser.setUserID(1L);
            loggedUser.setUnitCode("H01");
            loggedUser.setIp("192.200.0.4");
            loggedUser.setBroswer("firefox");

            return loggedUser;
        }
    };

    @Test
    @Ignore
    public void testBookstock() throws Exception {
        List<BookStock>  bookStocks = new ArrayList(); 
        when(bookStockService.findAllActive()).thenReturn(bookStocks);
        
        mockMvc.perform(get("/stocks/bookstock" )
                    .param("bookStockId", "1")
                    .param("name", "name")
                        )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/bookstock"))
                .andExpect(model().attribute("bookStocks", bookStocks))
                .andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testGenericStockTypeform() throws Exception {
        List<BookStockLockView> bookStockLockViewList = new ArrayList();
        when(currentStockService.getBookStockLockView()).thenReturn(bookStockLockViewList);
        List<BookStockLine> bookStockLines= new ArrayList();
        when(bookStockService.getBookStockLinesList(any(ArrayList.class))).thenReturn(bookStockLockViewList);
        
        bookStock.setBookStockLines(bookStockLines);
        
        mockMvc.perform(post("/stocks/bookstockform" )
                    .flashAttr("bookStock", bookStock)
                        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/bookstock"))
                .andExpect(flash().attributeExists("msg")) 
                .andDo(print())
                ;
        
        //verify(bookStockService, times(1)).save(any(BookStock.class),any(ArrayList.class));
    }
    
}
