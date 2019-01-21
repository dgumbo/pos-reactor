
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
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
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.stocks.entity.GenericStockType;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockSupplier;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


public class GenericStockTypeControllerTest {
    private GenericStockTypeService genericStockTypeService; 
    private MenuGroupService menuGroupService;
    private UnitOfMeasureService unitOfMeasureService;
    private StockGroupService stockGroupService;
    private StockManufacturerService stockManufacturerService;

    private MockMvc mockMvc;
    private List<GenericStockType> genericStockTypeList;
    private GenericStockType genericStockType;
    private Unit unit;
    private Location location; 
    private List<Location> locations;
    private List<Unit> units; 

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.menuGroupService = mock(MenuGroupService.class);
        this.genericStockTypeService = mock(GenericStockTypeService.class);  
        this.stockManufacturerService = mock(StockManufacturerService.class);
        this.stockGroupService = mock(StockGroupService.class);
        this.unitOfMeasureService = mock(UnitOfMeasureService.class);
        GenericStockTypeController genericStockTypeController = new GenericStockTypeController(  menuGroupService, genericStockTypeService, stockManufacturerService, stockGroupService, unitOfMeasureService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(genericStockTypeController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.genericStockTypeList = new ArrayList<>();
        this.genericStockType = new GenericStockType();
        genericStockType.setId(1L);
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit); 
        genericStockTypeList.add(genericStockType);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);    

        when(genericStockTypeService.getByID(any(Long.class))).thenReturn(genericStockType);
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
    public void testGenericStockType()  throws Exception{
        List<StockGroup> stockGroupList = new ArrayList();
        when(stockGroupService.findAllActive()).thenReturn(stockGroupList);
        List<GenericStockType> genericStockTypeList = new ArrayList();;
        when(genericStockTypeService.findByNameAndStockGroup(any(String.class) ,any(Long.class))).thenReturn(genericStockTypeList);

        mockMvc.perform(get("/stocks/genericstocktype" )
                    .param("stockGroupId", "1")
                    .param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/genericstocktype"))
                .andExpect(model().attribute("stockGroups", equalTo(stockGroupList)))
                .andExpect(model().attribute("genericStockTypes", equalTo(genericStockTypeList))) //.andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testGenericStockTypeAction() throws Exception {
        StockGroup stockGroup = new StockGroup();
        stockGroup.setId(1L);
        genericStockType.setStockGroup(stockGroup);

        when(genericStockTypeService.getByID(any(Long.class))).thenReturn(genericStockType); 
        when(genericStockTypeService.findStockTypeByGenericStockTypeId(any(Long.class))).thenReturn(new ArrayList());
        when(stockGroupService.findAllActive( )).thenReturn( new ArrayList());
        when(stockGroupService.getStockSubGroupByID(any(Long.class))).thenReturn(new ArrayList()); 
        when(stockManufacturerService.findAllActive()).thenReturn( new ArrayList());
        when(unitOfMeasureService.findAll( )).thenReturn(new ArrayList());

        mockMvc.perform(get("/stocks/genericstocktypeaction/1/") )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/genericstocktypeaction"))
                .andExpect(model().attribute("genericStockType", equalTo(genericStockType)))
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testGenericStockTypeform() throws Exception {
 
        mockMvc.perform(post("/stocks/genericstocktypeform").flashAttr("genericStockType", genericStockType))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/genericstocktype"))
                .andExpect(flash().attributeExists("msg")) 
               // .andDo(print())
                ;

        verify(genericStockTypeService, times(1)).save(any(GenericStockType.class), any(ArrayList.class));
    }

    @Test
    public void testFindAllAgencies()  throws Exception{ 
        when(stockGroupService.getStockSubGroupByID(any(Long.class))).thenReturn(new ArrayList());
         
        mockMvc.perform(get("/stocks/stocksubgroups").param("Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(jsonPath("$[0].id").value("1"))
                .andDo(print());

        //return currentStock;
    }
    
}
