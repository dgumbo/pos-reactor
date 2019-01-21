
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.BookStock;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockCount;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.BookStockService;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.StockCountService;


    @Ignore
public class StockCountControllerTest { 
    private BookStockService bookStockService;
    private MenuGroupService menuGroupService;
    private StockCountService stockCountService;
    private CurrentStockService currentStockService;

    private MockMvc mockMvc;
    private List<StockCount> stockCountList;
    private StockCount stockCount;
//    private Unit unit;
//    private Location location;
//    private List<StockCountLine> stockCountLineList;
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList; 
//    private List<Location> locations;
//    private List<Unit> units; 
    private UnitService unitService;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.bookStockService = mock(BookStockService.class);
        this.stockCountService = mock(StockCountService.class);
        this.menuGroupService = mock(MenuGroupService.class);
        this.currentStockService = mock(CurrentStockService.class); 
        this.unitService =  mock(UnitService.class);
     
        StockCountController stockCountController  = new StockCountController(menuGroupService, stockCountService, bookStockService, currentStockService, unitService);
 

//        this.mockMvc = MockMvcBuilders
//                .standaloneSetup(stockCountController)
//                .setCustomArgumentResolvers(putPrincipal)
//                .setViewResolvers(viewResolver)
//                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
//        this.stockCountList = new ArrayList<>();
//        this.stockCount = new StockCount();
//        stockCount.setId(1L);
//        this.units = new ArrayList();
//        this.unit = new Unit();
//        unit.setId(1L);
//        units.add(unit);
//        stockCountList.add(stockCount);
//        this.locations = new ArrayList();
//        this.location = new Location();
//        location.setId(1L);
//        locations.add(location);
        StockType stockType = new StockType();
        stockType.setId(1L);
//        this.stockCountLineList = new ArrayList<>();
//        StockCountLine stockCountLine = new StockCountLine();
//        CurrentStock baseStock = new CurrentStock();
//        baseStock.setQuantity(1);
//        baseStock.setBatchNumber("batchNumber");
//        baseStock.setStockType(stockType);
//        baseStock.setUnit(unit);
//        baseStock.setLocation(location);
//        baseStock.setExpiryDate(new Date());
//        baseStock.setStockStatus(StockStatus.AVAILABLE);
//        baseStock.setCreateTransaction(1L);
//        stockCountLine.setCount(1L);
//        stockCountLine.setCurrentStock(baseStock);
//        stockCountLine.setStockCount(stockCount); 
//        stockCountLineList.add(stockCountLine);
//        stockCount.setStockCountLines(stockCountLineList);

        this.currentStockList = new ArrayList<>();
        this.currentStock = new CurrentStock();
        currentStock.setId(1L);
        currentStock.setStockType(stockType);
        currentStockList.add(currentStock);

        when(stockCountService.getByID(any(Long.class))).thenReturn(stockCount);
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
    public void testStockCount()  throws Exception { 
        when(stockCountService.findAll( )).thenReturn(stockCountList);
        
        mockMvc.perform(get("/stocks/stockcount"  ) )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/stockcount"))
                .andExpect(model().attribute("stockCounts", equalTo(stockCountList)))
                //.andDo(print())
                ; 

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test 
    public void testStockCountAction()  throws Exception {
        BookStock bookStock = new BookStock();
        bookStock.setId(1L);
        List<BookStock> bookStockList = new ArrayList();
        bookStockList.add(bookStock);

        when(stockCountService.getByID(any(Long.class))).thenReturn(stockCount);
        when(bookStockService.findAllActive()).thenReturn(bookStockList);
        
        mockMvc.perform(get("/stocks/stockcountaction/1" ) )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/stockcountaction"))
                .andExpect(model().attribute("bookStocks", equalTo(bookStockList)))
                .andExpect(model().attribute("stockCount", equalTo(stockCount))) 
                //.andDo(print())
                ;        
        
        when(stockCountService.getByID(any(Long.class))).thenReturn(null);
        mockMvc.perform(get("/stocks/stockcountaction/1" ) )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/stockcountaction"))
                .andExpect(model().attribute("bookStocks", equalTo(bookStockList)))
                .andExpect(model().attributeExists("stockCount")) 
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test 
    public void testStockCountform()  throws Exception {  
                
        mockMvc.perform(post("/stocks/stockcountform")
                            .flashAttr("stockCount", stockCount) 
                        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/stockcount" ))
                .andExpect(flash().attributeExists("msg")) 
               // .andDo(print())
                ;

        verify(stockCountService, times(1)).save(any(StockCount.class));
    }

    @Test
    public void testFindCurrentStockInLocation()  throws Exception {
        BookStock bookStock = new BookStock();
        bookStock.setId(1L);
        when(bookStockService.getByID(any(Long.class))).thenReturn(bookStock);
        when(currentStockService.findByBookStock(any(BookStock.class))).thenReturn(currentStockList);
         
        mockMvc.perform(get("/stocks/bookstocklist").param("Id", "1"))
                //.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(jsonPath("$[0].id").value("1"))
                //.andDo(print())
                ;

        //return currentStock;
    }
    
}
