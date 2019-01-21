
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 
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
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionStatus;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockMovementDescriptionService;
import zw.co.psmi.hms.stocks.service.StockSupplierService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;



public class ReceiptControllerTest {

    private StockTransactionService stockTransactionService;
    
    private CurrentStockService currentStockService;
    private StockMovementDescriptionService stockMovementDescriptionService;
    private StockSupplierService stockSupplierService;
    private UnitService unitService;
    private GenericStockTypeService genericStockTypeService;

    private MockMvc mockMvc;
    private List<StockTransaction> stockTransactionList;
    private StockTransaction stockTransaction;
    private Unit unit;
    private Location location;
    private List<StockTransactionLine> stockTransactionLineList;
    private CurrentStock currentStock;
    private List<CurrentStock> currentStockList;
    private List<Location> locations;
    private List<Unit> units;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/html/");
        viewResolver.setSuffix(".html");

        this.stockTransactionService = mock(StockTransactionService.class);
        this.currentStockService = mock(CurrentStockService.class);
        this.stockMovementDescriptionService = mock(StockMovementDescriptionService.class);
        this.stockSupplierService = mock(StockSupplierService.class);
        this.unitService = mock(UnitService.class);
        this.genericStockTypeService = mock(GenericStockTypeService.class);
        ReceiptController receiptControllerController = new ReceiptController(stockTransactionService, currentStockService, stockMovementDescriptionService, stockSupplierService, unitService, genericStockTypeService);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(receiptControllerController)
                .setCustomArgumentResolvers(putPrincipal)
                .setViewResolvers(viewResolver)
                .build();

        initializeTestVariables();
    }

    private void initializeTestVariables() {
        this.stockTransactionList = new ArrayList<>();
        this.stockTransaction = new StockTransaction();
        stockTransaction.setId(1L);
        this.units = new ArrayList();
        this.unit = new Unit();
        unit.setId(1L);
        units.add(unit);
        stockTransaction.setUnitFrom(unit);
        stockTransactionList.add(stockTransaction);
        this.locations = new ArrayList();
        this.location = new Location();
        location.setId(1L);
        locations.add(location);
        StockType stockType = new StockType();
        stockType.setId(1L);
        this.stockTransactionLineList = new ArrayList<>();
        StockTransactionLine stockTransactionLine = new StockTransactionLine();
        CurrentStock baseStock = new CurrentStock();
        baseStock.setQuantity(1);
        baseStock.setBatchNumber("batchNumber");
        baseStock.setStockType(stockType);
        baseStock.setUnit(unit);
        baseStock.setLocation(location);
        baseStock.setExpiryDate(new Date());
        baseStock.setStockStatus(StockStatus.AVAILABLE);
        baseStock.setCreateTransaction(1L);
        stockTransactionLine.setQuantity(1L);
        stockTransactionLine.setBaseStock(baseStock);
        stockTransactionLine.setStockTransaction(stockTransaction);
        stockTransactionLine.setStockType(stockType);
        stockTransactionLineList.add(stockTransactionLine);
        stockTransaction.setStockTransactionLines(stockTransactionLineList);

        this.currentStockList = new ArrayList<>();
        this.currentStock = new CurrentStock();
        currentStock.setId(1L);
        currentStock.setStockType(stockType);
        currentStockList.add(currentStock);

        when(stockTransactionService.getByID(any(Long.class))).thenReturn(stockTransaction);
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
    public void testAdjustmentReceipt() throws Exception {
        StockTransactionType stockTransactionType = StockTransactionType.ADJUSTMENT_RECEIPT;

        when(stockTransactionService.getFromStockTransactionType(any(StockTransactionType.class))).thenReturn(stockTransactionList);
        mockMvc.perform(get("/stocks/receipt/" + stockTransactionType).param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/receipt"))
                .andExpect(model().attribute("stockTransactionType", equalTo(stockTransactionType)))
                .andExpect(model().attributeExists("title"))
                .andExpect(model().attribute("stockTransactionTypes", equalTo(StockTransactionType.values())))
                .andExpect(model().attribute("stockTransactions", equalTo(stockTransactionList))) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testAdjustmentReceiptAction() throws Exception {
        StockTransactionType stockTransactionType = StockTransactionType.ADJUSTMENT_RECEIPT;
        List<StockMovementDescription> stockMovementDescriptions = new ArrayList();
        List<StockType> stockTypes = new ArrayList();

        when(stockTransactionService.getByID(any(Long.class))).thenReturn(stockTransaction);
        when(stockMovementDescriptionService.findAllActive()).thenReturn(stockMovementDescriptions);
        when(unitService.getLocationActive(any(String.class))).thenReturn(locations);
        when(stockTransactionService.getFromStockTransactionType(any(StockTransactionType.class))).thenReturn(stockTransactionList);
        when(stockTransactionService.findStockTransactionLinesByStockTransaction(any(StockTransaction.class))).thenReturn(stockTransactionLineList);
        //when(genericStockTypeService.getStockTypeActive()).thenReturn(stockTypes);

        mockMvc.perform(get("/stocks/receiptaction/1/" + stockTransactionType).param("name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("/stocks/receiptaction"))
                .andExpect(model().attribute("stockTransactionStatuses", equalTo(StockTransactionStatus.values())))
                .andExpect(model().attribute("stockTransactionTypes", equalTo(StockTransactionType.values())))
                .andExpect(model().attribute("stockTransactionType", equalTo(stockTransactionType)))
                .andExpect(model().attribute("stockTransaction", equalTo(stockTransaction)))
                .andExpect(model().attribute("stockMovementDescriptions", equalTo(stockMovementDescriptions)))
                .andExpect(model().attribute("stockStatuses", equalTo(Arrays.asList(StockStatus.values()).stream().filter(s->s!=StockStatus.MERGED&&s!=StockStatus.SPLIT).collect(Collectors.toList()))))
                .andExpect(model().attribute("stockTypes", equalTo(stockTypes)))
                .andExpect(model().attribute("locations", equalTo(locations))) //.andDo(print())
                ;

        // verify(siteService, times(1)).save(any(Site.class));
    }

    @Test
    public void testAdjustmentReceiptForm() throws Exception { 
        stockTransaction.setStockTransactionType(StockTransactionType.ADJUSTMENT_ISSUE);
                
        mockMvc.perform(post("/stocks/receiptform")
                            .flashAttr("stockTransaction", stockTransaction) 
                        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/stocks/receipt/" + stockTransaction.getStockTransactionType().name()))
                .andExpect(flash().attributeExists("msg")) 
                .andDo(print());

//        verify(stockTransactionService, times(1)).save(any(StockTransaction.class), any(ArrayList.class));
    }

}
