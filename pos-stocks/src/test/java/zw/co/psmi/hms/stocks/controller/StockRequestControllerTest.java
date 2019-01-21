
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
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockSupplier;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockGroupService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


public class StockRequestControllerTest {

    private MenuGroupService menuGroupService;
    private StockRequestService stockRequestService;
    private StockManufacturerService stockManufacturerService;
    private StockGroupService stockGroupService;
    private UnitService unitService;
    private UnitOfMeasureService unitOfMeasureService;
    private GenericStockTypeService genericStockTypeService;
    private StockSupplierService stockSupplierService;

    private MockMvc mockMvc;
    private List<StockRequest> stockRequestList;
    private StockRequest stockRequest;
    private Unit unit;
    private Location location;
    private List<StockRequestItem> stockRequestItemList;
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList;
    private List<Location> locations;
    private List<Unit> units;
    private StockGroup stockGroup;
    private List<StockGroup> stockGroupList;
    private ApprovalService approvalService;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.menuGroupService = mock(MenuGroupService.class);
        this.stockRequestService = mock(StockRequestService.class);
        this.stockManufacturerService = mock(StockManufacturerService.class);
        this.stockGroupService = mock(StockGroupService.class);
        this.unitService = mock(UnitService.class);
        this.unitOfMeasureService = mock(UnitOfMeasureService.class);
        this.genericStockTypeService = mock(GenericStockTypeService.class);
        this.stockSupplierService = mock(StockSupplierService.class);
        this.approvalService = mock(ApprovalService.class);
        RequestController stockRequestController = new RequestController(menuGroupService, stockRequestService, stockManufacturerService, stockGroupService, unitService, unitOfMeasureService, genericStockTypeService, stockSupplierService, approvalService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(stockRequestController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.stockRequestList = new ArrayList<>();
        this.stockRequest = new StockRequest();
        stockRequest.setId(1L);
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit);
        stockRequest.setRequestUnit(unit);
        stockRequestList.add(stockRequest);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);
        StockType stockType = new StockType();
        stockType.setId(1L);
        this.stockRequestItemList = new ArrayList<>();
        StockRequestItem stockRequestItem = new StockRequestItem();
        CurrentStock baseStock = new CurrentStock();
        baseStock.setQuantity(1);
        baseStock.setBatchNumber("batchNumber");
        baseStock.setStockType(stockType);
        baseStock.setUnit(unit);
        baseStock.setLocation(location);
        baseStock.setExpiryDate(new Date());
        baseStock.setStockStatus(StockStatus.AVAILABLE);
        baseStock.setCreateTransaction(1L);
        stockRequestItem.setQuantity(1);
        stockRequestItem.setStockRequest(stockRequest);
        //stockRequestItem.setStockType(stockType);
        stockRequestItemList.add(stockRequestItem);
        stockRequest.setStockRequestItems(stockRequestItemList);
        stockRequest.setIssueUnit(unit);

        this.currentStockList = new ArrayList<>();
        this.currentStock = new CurrentStock();
        currentStock.setId(1L);
        currentStock.setStockType(stockType);
        currentStockList.add(currentStock);

        stockGroupList = new ArrayList();
        stockGroup = new StockGroup();
        stockGroup.setId(1L);
        stockGroupList.add(stockGroup);

        when(stockRequestService.getByID(any(Long.class))).thenReturn(stockRequest);
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
    public void testRequest() throws Exception {
        when(stockGroupService.findAllActive()).thenReturn(stockGroupList);
//        when(stockRequestService.findAllBySearchCriteria(any(WorkFlowType.class), any(StockRequest.class))).thenReturn(stockRequestList);

        mockMvc.perform(get("/stocks/stockrequest").param("locationID", "1").param("creationDate", new Date().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/stockrequest"))
                .andExpect(model().attribute("stockGroups", equalTo(stockGroupList)))
                .andExpect(model().attribute("stockRequests", equalTo(stockRequestList))) //.andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testPurchaseOrder() throws Exception {
        when(stockGroupService.findAllActive()).thenReturn(stockGroupList);
//        when(stockRequestService.findAllBySearchCriteria(any(WorkFlowType.class), any(StockRequest.class))).thenReturn(stockRequestList);

        mockMvc.perform(get("/stocks/purchaseorder").param("locationID", "1").param("creationDate", new Date().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/purchaseorder"))
                .andExpect(model().attribute("stockGroups", equalTo(stockGroupList)))
                .andExpect(model().attribute("stockRequests", equalTo(stockRequestList))) //.andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testPurchaseAction() throws Exception {
        List<StockType> stockTypes = new ArrayList();
        List<StockSupplier> stockSupplierList = new ArrayList();
        StockSupplier stockSupplier = new StockSupplier();
        stockSupplier.setId(1L);
        stockSupplierList.add(stockSupplier);

        when(stockRequestService.getByID(any(Long.class))).thenReturn(stockRequest);
        when(unitService.getLocationActive(any(String.class))).thenReturn(locations);
        when(stockRequestService.getStockRequestItemsByStockRequestId(any(Long.class))).thenReturn(stockRequestItemList);
        //when(genericStockTypeService.getStockTypeActive()).thenReturn(stockTypes);
        when(stockSupplierService.findAllActive()).thenReturn(stockSupplierList);
        when(unitService.getByUnitCode(any(String.class))).thenReturn(unit);

        mockMvc.perform(get("/stocks/purchaseorderaction/1/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/purchaseorderaction"))
                .andExpect(model().attribute("stockRequest", equalTo(stockRequest))) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testRequestAction() throws Exception {
        List<StockType> stockTypes = new ArrayList();
        List<StockSupplier> stockSupplierList = new ArrayList();
        StockSupplier stockSupplier = new StockSupplier();
        stockSupplier.setId(1L);
        stockSupplierList.add(stockSupplier);

        when(stockRequestService.getByID(any(Long.class))).thenReturn(stockRequest);
        when(unitService.getLocationActive(any(String.class))).thenReturn(locations);
        when(stockRequestService.getStockRequestItemsByStockRequestId(any(Long.class))).thenReturn(stockRequestItemList);
        //when(genericStockTypeService.getStockTypeActive()).thenReturn(stockTypes);
        when(stockSupplierService.findAllActive()).thenReturn(stockSupplierList);
        when(unitService.getByUnitCode(any(String.class))).thenReturn(unit);

        mockMvc.perform(get("/stocks/stockrequestaction/1/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/stockrequestaction"))
                .andExpect(model().attribute("stockRequest", equalTo(stockRequest))) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testPurchaseform() throws Exception {

        mockMvc.perform(post("/stocks/purchaseorderform").flashAttr("stockRequest", stockRequest))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/purchaseorder"))
                .andExpect(flash().attributeExists("msg")) // .andDo(print())
                ;

        // verify(stockRequestService, times(1)).save(any(StockRequest.class), any(ArrayList.class));
    }

    @Test
    @Ignore
    public void testRequestform() throws Exception {

        mockMvc.perform(post("/stocks/stockrequestform").requestAttr("request", stockRequest))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/stockrequest"))
                .andExpect(flash().attributeExists("msg")) //   .andDo(print())
                ;

        //  verify(stockRequestService, times(1)).save(any(StockRequest.class), any(ArrayList.class));
    }

    @Test
    public void testFindAllLocations() throws Exception {
        List<Location> locationList = new ArrayList();
        when(unitService.getLocationsByUnitID(any(Long.class))).thenReturn(locationList);

        mockMvc.perform(get("/stocks/locations").param("Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(jsonPath("$[0].id").value("1"))
                .andDo(print());

        //return currentStock;
    }

}
