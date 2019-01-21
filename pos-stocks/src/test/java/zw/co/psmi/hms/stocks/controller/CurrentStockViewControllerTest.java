
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.CurrentStockView;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockManufacturer;
import zw.co.psmi.hms.stocks.entity.StockStatus; 
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService; 
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;


public class CurrentStockViewControllerTest {
    private GenericStockTypeService genericStockTypeService;
    private CurrentStockService currentStockService;
    private StockGroupService stockGroupService;
    private StockManufacturerService stockManufacturerService;
    private LocationDao locationDao;
    private UnitService unitService;

    private MockMvc mockMvc;  
    private Unit unit;
    private Location location;
    private List<StockGroup> stockGroupList;
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList;
    private List<Location> locations;
    private List<Unit> units;
    private List<CurrentStockView> currentStockViewList;
    private CurrentStockView currentStockView;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");
        
        this.genericStockTypeService = mock(   GenericStockTypeService   .class);
        this. unitService = mock(   UnitService   .class);
        this. locationDao = mock(  LocationDao    .class);
        this. currentStockService = mock(  CurrentStockService    .class);
        this. stockManufacturerService = mock(  StockManufacturerService    .class);
        this. stockGroupService = mock(    StockGroupService  .class);
        
        CurrentStockViewController currentStockViewController  = new CurrentStockViewController( genericStockTypeService, unitService, locationDao, currentStockService, stockManufacturerService, stockGroupService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(currentStockViewController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.currentStockViewList = new ArrayList<>();
        this.currentStockView = new CurrentStockView(); 
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit);
        currentStockViewList.add(currentStockView);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);
        StockStatus stockStatus = StockStatus.AVAILABLE; 
        
        this.stockGroupList = new ArrayList<>();
        StockGroup stockGroup = new StockGroup(); 
        stockGroupList.add(stockGroup); 

        this.currentStockList = new ArrayList<>();
        this.currentStock = new CurrentStock();
        currentStock.setId(1L); 
        currentStockList.add(currentStock); 
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
    public void testBookstock()  throws Exception { 
        List<StockManufacturer> manufacturerList = new ArrayList();
        
        when(currentStockService.getCurrentStockView(any(String.class), any(Long.class), any(Long.class), any(Long.class), any(Long.class), any(StockStatus.class))).thenReturn( currentStockViewList);
        when(unitService.getLocationActive(any(String.class))).thenReturn( locations);
        when(stockManufacturerService.findAllActive()).thenReturn(manufacturerList);
        when(stockGroupService.findAllActive()).thenReturn(stockGroupList);
        
        mockMvc.perform(get("/stocks/currentstockview" ) )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/currentstockview"))
                .andExpect(model().attribute("currentStockViewList", equalTo(currentStockViewList)))
                .andExpect(model().attribute("locations", equalTo(locations))) 
                .andExpect(model().attribute("manufacturers", equalTo(manufacturerList))) 
                .andExpect(model().attribute("stockStatus", equalTo(Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList())))) 
                .andExpect(model().attribute("stockGroups", equalTo(stockGroupList))) 
                //.andDo(print())
                ;        
         
        // verify(siteService, times(1)).save(any(Site.class));
    }    
}
