
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.UnitOfMeasure; 
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


public class UnitOfMeasureControllerTest {

    private MenuGroupService menuGroupService;
    private UnitOfMeasureService unitOfMeasureService;  

    private MockMvc mockMvc;
    private List<UnitOfMeasure> unitOfMeasureList;
    private UnitOfMeasure unitOfMeasure;
    private Unit unit;
    private Location location; 
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList;
    private List<Location> locations;
    private List<Unit> units;
    private StockGroup stockGroup;
    private List<StockGroup> stockGroupList; 

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.menuGroupService = mock(MenuGroupService.class);
        this.unitOfMeasureService = mock(UnitOfMeasureService.class); 
        UnitOfMeasureController unitOfMeasureController = new UnitOfMeasureController(  menuGroupService, unitOfMeasureService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(unitOfMeasureController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.unitOfMeasureList = new ArrayList<>();
        this.unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit); 
        unitOfMeasureList.add(unitOfMeasure);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);
        StockType stockType = new StockType();
        stockType.setId(1L); 
        CurrentStock baseStock = new CurrentStock();
        baseStock.setQuantity(1);
        baseStock.setBatchNumber("batchNumber");
        baseStock.setStockType(stockType);
        baseStock.setUnit(unit);
        baseStock.setLocation(location);
        baseStock.setExpiryDate(new Date());
        baseStock.setStockStatus(StockStatus.AVAILABLE);
        baseStock.setCreateTransaction(1L);  

        this.currentStockList = new ArrayList<>();
        this.currentStock = new CurrentStock();
        currentStock.setId(1L);
        currentStock.setStockType(stockType);
        currentStockList.add(currentStock);

        stockGroupList = new ArrayList();
        stockGroup = new StockGroup();
        stockGroup.setId(1L);
        stockGroupList.add(stockGroup);

        when(unitOfMeasureService.getByID(any(Long.class))).thenReturn(unitOfMeasure);
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
    public void testUnitOfMeasure() throws Exception {
        when(unitOfMeasureService.findAll()).thenReturn(unitOfMeasureList);
         
        mockMvc.perform(get("/stocks/unitofmeasure" ).param("locationID", "1").param("creationDate", new Date().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/unitofmeasure")) 
                .andExpect(model().attribute("unitOfMeasures", equalTo(unitOfMeasureList))) //.andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testUnitOfMeasureAction()  throws Exception {    

        when(unitOfMeasureService.getByID(any(Long.class))).thenReturn(unitOfMeasure); 

        mockMvc.perform(get("/stocks/unitofmeasureaction/1") )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/unitofmeasureaction"))
                .andExpect(model().attribute("unitOfMeasure", equalTo(unitOfMeasure)))
                //.andDo(print())
                ;

        when(unitOfMeasureService.getByID(any(Long.class))).thenReturn(null); 
        mockMvc.perform(get("/stocks/unitofmeasureaction/1") )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/unitofmeasureaction"))
                .andExpect(model().attributeExists("unitOfMeasure"))
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testUnitOfMeasureform()  throws Exception {
 
        mockMvc.perform(post("/stocks/unitofmeasureform").flashAttr("unitOfMeasure", unitOfMeasure))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/unitofmeasure"))
                .andExpect(flash().attributeExists("msg")) 
               // .andDo(print())
                ;

       // verify(unitOfMeasureService, times(1)).save(any(UnitOfMeasure.class), any(ArrayList.class));
    }
    
}
