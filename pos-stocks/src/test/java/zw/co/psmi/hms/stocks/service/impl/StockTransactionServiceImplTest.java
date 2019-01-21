
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import zw.co.psmi.hms.authentication.dao.LocationDao;
import zw.co.psmi.hms.authentication.dao.UnitDao;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Role;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.stocks.dao.StockTransactionDao;
import zw.co.psmi.hms.stocks.dao.StockTransactionLineDao;
import zw.co.psmi.hms.stocks.dao.StockTypeDao;
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.entity.StockTransaction;
import zw.co.psmi.hms.stocks.entity.StockTransactionLine;
import zw.co.psmi.hms.stocks.entity.StockTransactionType;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.CurrentStockService;
import zw.co.psmi.hms.stocks.service.StockRequestService;
import zw.co.psmi.hms.stocks.service.StockTransactionService;


public class StockTransactionServiceImplTest {

    private StockTransactionDao stockTransactionDao;
    private StockTypeDao stockTypeDao;
    private StockRequestService stockRequestService;
    private StockTransactionLineDao stockTransactionLineDao;
    private CurrentStockService currentStockService;
    private LocationDao locationDao;
    private StockTransactionService stockTransactionService;
    private UnitDao unitDao;

    private StockTransaction stockTransaction;
    private Location location;
    private Unit unit;
    private CurrentStock baseStock;
    private StockType stockType;
    private List<StockTransactionLine> stockTransactionLineList;

    @Before
    public void setUp() {
        this.stockTransactionDao = mock(StockTransactionDao.class);
        this.stockTransactionLineDao = mock(StockTransactionLineDao.class);
        this.stockRequestService = mock(StockRequestService.class);
        this.stockTypeDao = mock(StockTypeDao.class);
        this.currentStockService = mock(CurrentStockService.class);
        this.locationDao = mock(LocationDao.class);
        this.unitDao = mock(UnitDao.class);
//        this.stockTransactionService = new StockTransactionServiceImpl(stockTransactionDao, currentStockService, stockTransactionLineDao, stockRequestService, stockTypeDao, locationDao, unitDao);

        initializeTestVariables();
    }

    public void initializeTestVariables() {
        this.location = new Location();
        location.setId(1L);
        this.stockType = new StockType();
        stockType.setId(1L);
        this.stockTransaction = new StockTransaction();
        stockTransaction.setId(1L);
        stockTransaction.setStockTransactionType(StockTransactionType.ADJUSTMENT_RECEIPT);
        this.unit = new Unit();
        unit.setId(1L);
        stockTransaction.setUnitFrom(unit);
        unit.setId(2L);
        stockTransaction.setUnitTo(unit);
        stockTransaction.setLocationFrom(location);
        stockTransaction.setLocationTo(location);
        this.stockTransactionLineList = new ArrayList<>();
        StockTransactionLine stockTransactionLine = new StockTransactionLine();
        this.baseStock = new CurrentStock();
        baseStock.setId(1L);
        baseStock.setQuantity(1);
        baseStock.setBatchNumber("batchNumber");
        baseStock.setStockType(stockType);
        baseStock.setUnit(unit);
        baseStock.setLocation(location);
        baseStock.setExpiryDate(new Date());
        baseStock.setStockStatus(StockStatus.AVAILABLE);
        baseStock.setCreateTransaction(1L);
        stockTransactionLine.setQuantity(1L);
        stockTransactionLine.setStockTransaction(stockTransaction);
        stockTransactionLine.setStockType(stockType);
        stockTransactionLine.setBaseStock(baseStock);
        stockTransactionLineList.add(stockTransactionLine);
        stockTransaction.setStockTransactionLines(stockTransactionLineList);
    }

    @Test
    @Ignore
    public void testGetDao() {
        assertEquals(stockTransactionDao, stockTransactionService.getDao());
    }

    @Test
    @Ignore
    public void testSave() {
        when(currentStockService.getByID(any(Long.class))).thenReturn(baseStock);
        when(stockTypeDao.findOne(any(Long.class))).thenReturn(stockType);
        when(locationDao.findOne(any(Long.class))).thenReturn(location);
        when(unitDao.findOne(any(Long.class))).thenReturn(unit);

//        stockTransactionService.save(stockTransaction, stockTransactionLineList);
//        verify(stockTransactionDao, times(1)).save(any(StockTransaction.class));
//        verify(stockTransactionLineDao, times(1)).save(any(stockTransactionLineList.getClass()));
//
//        stockTransaction.setStockTransactionType(StockTransactionType.CASH_PURCHASE);
//        stockTransactionService.save(stockTransaction, stockTransactionLineList);
//
//        stockTransaction.setStockTransactionType(StockTransactionType.PURCHASE_RECEIPT);
//        stockTransactionService.save(stockTransaction, stockTransactionLineList);
    }

    @Test
    @Ignore
    public void testFindAll() {
        stockTransactionService.findAll();
        verify(stockTransactionDao, times(1)).findAll();
    }

    @Test
    @Ignore
    public void testCreateStockTransactionFromStockRequest() {
        Approval approval = new Approval();
        List<StockRequestItem> stockRequestItemList = new ArrayList<>();
        StockRequestItem stockRequestItem = new StockRequestItem();
        stockRequestItem.setId(1L);
        StockRequest stockRequest = new StockRequest();
        stockRequest.setStockRequestItems(stockRequestItemList);
        
        assertNull(stockTransactionService.createStockTransactionFromStockRequest(null)); 


        approval.setStockRequest(stockRequest);
        stockRequestItemList.add(stockRequestItem);
        when(stockRequestService.getStockRequestItemsByStockRequestId(any(Long.class))).thenReturn(stockRequestItemList);

        assertNotNull(stockTransactionService.createStockTransactionFromStockRequest(stockRequest));
        verify(stockRequestService, times(1)).getStockRequestItemsByStockRequestId(any(Long.class));
    }

    @Test
    @Ignore
    public void testGetFromStockTransactionType() {
        List<StockTransaction> stockTransactionList = new ArrayList<>();
        stockTransactionList.add(stockTransaction);

        when(stockTransactionDao.findByStockTransactionType(any(StockTransactionType.class))).thenReturn(stockTransactionList);

        assertEquals(stockTransactionList, stockTransactionService.getFromStockTransactionType(StockTransactionType.ADJUSTMENT_ISSUE));
        verify(stockTransactionDao, times(1)).findByStockTransactionType(any(StockTransactionType.class));
    }

    @Test
    @Ignore
    public void testPreProcess() {
//        when(currentStockService.getByID(any(Long.class))).thenReturn(baseStock);
//        List<StockTransaction> stockTransactionList = new ArrayList<>();
//        stockTransactionList.add(stockTransaction);
//
//        when(stockTransactionDao.findByStockTransactionType(any(StockTransactionType.class))).thenReturn(stockTransactionList);
//
//        assertEquals(stockTransaction, stockTransactionService.preProcess(stockTransaction, true));
//
//        stockTransaction.getStockTransactionLines().get(0).getBaseStock().setId(1L);
//        stockTransaction.getStockTransactionLines().get(0).getBaseStock().setQuantity(33);
//        stockTransaction.getStockTransactionLines().get(0).setQuantity(21);
//       
//        stockTransactionService.preProcess(stockTransaction, false);
//        
//       // verify(currentStockService, times(1)).getByID(any(StockTransactionType.class));
//        //verify(currentStockService, times(3)).save(any(StockTransactionType.class)); 
//        
//        try {
//            stockTransactionService.preProcess(stockTransaction, false);
//            stockTransaction.getStockTransactionLines().get(0).getBaseStock().setId(0L);
//            stockTransactionService.preProcess(stockTransaction, false);
//        } catch (final RuntimeException e) {
//            final String msg = "CurrentStock not found:batchNumber";
//            assertEquals(msg, e.getMessage());
//        }
    }
    @Test @Ignore
    public void testFindstockTransactionLineLinesBystockTransactionLine() {
        stockTransactionService.findStockTransactionLinesByStockTransaction(new StockTransaction());
        verify(stockTransactionLineDao, times(1)).findStockTransactionLinesByStockTransaction(any(StockTransaction.class));
    }

}
