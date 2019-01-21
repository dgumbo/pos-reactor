
package zw.co.psmi.hms.stocks.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.equalTo;
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
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockSupplier;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;


public class PurchaseReceiptControllerTest {
    private StockTransactionService stockTransactionService;
    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
     
    private UnitService unitService; 
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

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.stockTransactionService = mock(StockTransactionService.class);  
        this.currentStockService = mock(CurrentStockService.class);
        this.stockMovementDescriptionService = mock(StockMovementDescriptionService.class);
        this.unitService = mock(UnitService.class); 
        this.genericStockTypeService = mock(GenericStockTypeService.class);
        this.stockSupplierService = mock(StockSupplierService.class); 
//        PurchaseReceiptController purchaseReceiptController = new PurchaseReceiptController(  stockTransactionService, stockTransactionLineService, currentStockService, stockMovementDescriptionService, stockSupplierService, unitService, genericStockTypeService);
//
//        this.mockMvc = MockMvcBuilders
//                .standaloneSetup(purchaseReceiptController)
//                .setCustomArgumentResolvers(putPrincipal)
//                .setViewResolvers(viewResolver)
//                .build();

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
      //  stockRequestItem.setStockType(stockType);
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
    public void testAdjustmentPurchaseReceipt() throws Exception {
        List<StockTransaction> stockTransactionList = new ArrayList();
        when(stockTransactionService.getFromStockTransactionType(any(StockTransactionType.class))).thenReturn(stockTransactionList); 

        mockMvc.perform(get("/stocks/purchasereceipt/" + StockTransactionType.ADJUSTMENT_RECEIPT ).param("locationID", "1").param("creationDate", new Date().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/purchasereceipt"))
                .andExpect(model().attribute("stockTransactionType", equalTo(StockTransactionType.ADJUSTMENT_RECEIPT)))
                .andExpect(model().attributeExists("title" )) 
                .andExpect(model().attribute("stockTransactionTypes", equalTo(StockTransactionType.values()))) 
                .andExpect(model().attribute("stockTransactions", equalTo(stockTransactionList))) 
                //.andDo(print())
                ;
        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testAdjustmentPurchaseReceiptAction()  throws Exception {  
        List<StockType> stockTypes = new ArrayList();  
        StockTransaction stockTransaction = new StockTransaction();
        List<StockMovementDescription> stockMovementDescriptionList = new ArrayList();
        List<StockTransactionLine> stockTransactionLineList = new ArrayList(); 

        when(stockTransactionService.getByID(any(Long.class))).thenReturn( stockTransaction);
        when(stockMovementDescriptionService.findAllActive( )).thenReturn( stockMovementDescriptionList);
        when(stockTransactionService.findStockTransactionLinesByStockTransaction(any(StockTransaction.class))).thenReturn(  stockTransactionLineList);
        //when(genericStockTypeService.getStockTypeActive()).thenReturn(stockTypes); 
        when(unitService.getByUnitCode(any(String.class))).thenReturn(unit);
        when(unitService.getLocationActive(any(String.class))).thenReturn(locations);

        mockMvc.perform(get("/stocks/purchasereceiptaction/1/1") )
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/purchasereceiptaction"))
                .andExpect(model().attribute("stockTransactionTypes", equalTo(StockTransactionType.values())))
                .andExpect(model().attribute("stockTransactionStatuses", equalTo(StockTransactionStatus.values())))
                .andExpect(model().attribute("stockTransaction", equalTo(stockTransaction)))
                .andExpect(model().attribute("stockMovementDescriptions", equalTo(stockMovementDescriptionList)))
                .andExpect(model().attribute("stockStatuses", equalTo(Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()))))
                .andExpect(model().attribute("stockTypes", equalTo(stockTypes)))
                .andExpect(model().attribute("locations", equalTo(locations)))
                //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    @Ignore
    public void testAdjustmentPurchaseReceiptForm() throws Exception { 
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setStockTransactionType(StockTransactionType.ADJUSTMENT_ISSUE);
        
        mockMvc.perform(post("/stocks/purchasereceiptform")
                .flashAttr("stockTransaction", stockTransaction))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/purchasereceipt/" + StockTransactionType.ADJUSTMENT_ISSUE))
                .andExpect(flash().attributeExists("msg")) 
                .andExpect(flash().attributeExists("stockTransactionType")) 
             //   .andDo(print())
                ;

      //  verify(stockRequestService, times(1)).save(any(StockRequest.class), any(ArrayList.class));
    }
    
}
