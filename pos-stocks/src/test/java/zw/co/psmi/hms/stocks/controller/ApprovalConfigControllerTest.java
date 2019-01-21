
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.List;
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
import zw.co.psmi.hms.authentication.entity.Role;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.MenuGroupService;
import zw.co.psmi.hms.authentication.service.RoleService;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.ApprovalConfig;
import zw.co.psmi.hms.stocks.entity.ApprovalTemplate;
import zw.co.psmi.hms.stocks.entity.CurrentStockView;
import zw.co.psmi.hms.stocks.service.ApprovalConfigService;


public class ApprovalConfigControllerTest {

    private CurrentStockView currentStockView;
    private MenuGroupService menuGroupService;
    private ApprovalConfigService approvalConfigService;
    private RoleService roleService;
    private UnitService unitService;

    private MockMvc mockMvc;
    private Unit unit;
    private Location location;
    private List<Location> locations;
    private List<Unit> units;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.menuGroupService = mock(MenuGroupService.class);
        this.approvalConfigService = mock(ApprovalConfigService.class); 
        this.unitService = mock(UnitService.class);
        this.roleService = mock( RoleService.class); 

        ApprovalConfigController approvalConfigController = new ApprovalConfigController( menuGroupService, approvalConfigService, unitService, roleService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(approvalConfigController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);
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
    public void testApprovalConfig() throws Exception { 
        ApprovalConfig approvalConfig = new ApprovalConfig();
        List<ApprovalConfig> approvalConfigList = new ArrayList();
        approvalConfigList.add(approvalConfig);
        when(approvalConfigService.findAll( )).thenReturn(approvalConfigList);
         
        mockMvc.perform(get("/stocks/approvalconfig"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/approvalconfig"))
                .andExpect(model().attribute ("approvalConfigs", approvalConfigList)) 
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testApprovalConfigAction() throws Exception { 
        ApprovalConfig approvalConfig = new ApprovalConfig();
        List<ApprovalTemplate> ApprovalTemplateList = new ArrayList();
        
        when(approvalConfigService.getByID(any(Long.class))).thenReturn(approvalConfig);
        when(approvalConfigService.getApprovalTemplateByApprovalConfigId(any(Long.class))).thenReturn( ApprovalTemplateList);
        when(unitService.getActive( )).thenReturn( units);
        List<Role> roles = new ArrayList();
        when(roleService.getActive( )).thenReturn(  roles);
         
        mockMvc.perform(get("/stocks/approvalconfigaction/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/approvalconfigaction"))
                .andExpect(model().attribute ("approvalConfig", approvalConfig)) 
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testApprovalConfigForm() throws Exception {
        mockMvc.perform(post("/stocks/approvalconfigform")
               .flashAttr("approvalConfig", new ApprovalConfig())
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/approvalconfig"))
                .andExpect(flash().attributeExists("msg")) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }
    
}
