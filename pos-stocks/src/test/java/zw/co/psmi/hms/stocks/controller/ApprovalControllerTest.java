
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.List; 
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
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.CurrentStockView;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.service.ApprovalService;


public class ApprovalControllerTest {

    private CurrentStockView currentStockView;
    private MenuGroupService menuGroupService;
    private ApprovalService approvalService;

    private MockMvc mockMvc;
    private Unit unit;
    private Location location;
    private List<StockGroup> stockGroupList;
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList;
    private List<Location> locations;
    private List<Unit> units;
    private List<CurrentStockView> currentStockViewList;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.menuGroupService = mock(MenuGroupService.class);
        this.approvalService = mock(ApprovalService.class);

        ApprovalController approvalController = new ApprovalController(menuGroupService, approvalService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(approvalController)
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
    @Ignore
    public void testApprovalAction() throws Exception { 
        Approval approval = new Approval();
        when(approvalService.getByID(any(Long.class))).thenReturn(approval);
         
        mockMvc.perform(get("/stocks/approvalaction/1/1/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/approvalaction"))
                .andExpect(model().attribute ("approval", approval)) 
                .andExpect(model().attribute ("action", "Accept Action")) 
                .andExpect(model().attribute ("source", 1))  
                //.andDo(print())
                ;
 
        when(approvalService.getByID(any(Long.class))).thenReturn(null);         
        mockMvc.perform(get("/stocks/approvalaction/1/2/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/approvalaction"))
                .andExpect(model().attributeExists("approval")) 
                .andExpect(model().attribute ("action", "Reject Action")) 
                .andExpect(model().attribute ("source", 5))  
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testApprovalform() throws Exception {
        mockMvc.perform(post("/stocks/approvalform/1")
               .flashAttr("approval", new Approval())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/stockrequest"))
                .andExpect(flash().attributeExists("msg")) //.andDo(print())
                ;
        
        mockMvc.perform(post("/stocks/approvalform/2")
               .flashAttr("approval", new Approval())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/purchaseorder"))
                .andExpect(flash().attributeExists("msg")) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

}
