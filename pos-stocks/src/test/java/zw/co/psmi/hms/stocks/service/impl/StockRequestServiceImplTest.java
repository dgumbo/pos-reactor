
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.stocks.dao.StockRequestDao;
import zw.co.psmi.hms.stocks.dao.StockRequestItemDao;
import zw.co.psmi.hms.stocks.dao.StockTypeDao;
import zw.co.psmi.hms.stocks.entity.Approval;
import zw.co.psmi.hms.stocks.entity.StockRequest;
import zw.co.psmi.hms.stocks.entity.StockRequestItem;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalConfigService;
import zw.co.psmi.hms.stocks.service.ApprovalService;


    @Ignore
public class StockRequestServiceImplTest {

    private StockRequestDao stockRequestDao;
    private StockRequestItemDao stockRequestItemDao;
    private ApprovalService approvalService;
    private ApprovalConfigService approvalConfigService;
    private UnitService unitService;
    private StockRequestServiceImpl stockRequestService;
    private StockTypeDao stockTypeDao;
    
    @Before
    public void setUp(){
        this. stockRequestDao = mock(StockRequestDao.class);
         this. stockRequestItemDao = mock(StockRequestItemDao.class);
         this. unitService = mock(UnitService.class);
         this. approvalConfigService = mock(ApprovalConfigService.class);
         this. approvalService = mock(ApprovalService.class);
         this. stockRequestService = new StockRequestServiceImpl( stockRequestDao, stockRequestItemDao, unitService, approvalConfigService, approvalService, stockTypeDao);        

    }

    @Test
    public void testGetDao() {
        assertEquals(stockRequestDao, stockRequestService.getDao());
    }

    @Test
    public void testGetByID() { 
        StockRequest stockRequest = new StockRequest();
    
        when(stockRequestDao.findOne(any(Long.class))).thenReturn(stockRequest);
        assertEquals(stockRequest, stockRequestService.getByID(any(Long.class)));        
        verify(stockRequestDao, times(1)).findOne(any(Long.class));

        when(stockRequestDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockRequestService.getByID(2L));
    }

    @Test
    public void testSave_2args() {      
        List<Approval> approvals = new ArrayList();
        Approval approval=new Approval();
        approval.setId(1L);
        approvals.add(approval);
        
        List<StockRequestItem> list = new ArrayList<>();
        list.add(new StockRequestItem());

        Location location = new Location();
        location.setId(1L);
        when(unitService.getLocationById(any(Long.class))).thenReturn(location);
        when(approvalService.findByStockRequest(any(StockRequest.class))).thenReturn(approvals);
        when(approvalConfigService.getApprovalList(any(WorkFlowType.class),any(Unit.class))).thenReturn(approvals);
                
        StockRequest stockRequest = new StockRequest();
        stockRequest.setId(0L);
        stockRequest.setIssueLocation(location);
        stockRequest.setLocation(location);
        stockRequest.setRequestUnit(new Unit());
        
        stockRequestService.save(stockRequest, list);
        verify(stockRequestDao, times(1)).save(any(StockRequest.class));
        verify(stockRequestItemDao, times(1)).save(any(list.getClass()));
    }

    @Test
    public void testFindByTypeAndNameAndStockGroup() {
        
        WorkFlowType workFlowType = WorkFlowType.PURCHASEORDER;
        Long locationID = 1L;
        Date requestDateStart = null;
        Date requestDateEnd = new Date();

        when(unitService.getLocationById(any(Long.class))).thenReturn(new Location());
//        stockRequestService.findByTypeAndDateAndLocationAndApprovalStatus(workFlowType, locationID, requestDateStart, requestDateEnd)  ;
//        verify(stockRequestDao, times(1))
//                .findByWorkFlowTypeAndLocationIdAndRequestDateBetween(any(workFlowType.getClass()), any(Location.class), any(Date.class), any(Date.class));

        locationID = 0L;
//        stockRequestService.findByTypeAndDateAndLocationAndApprovalStatus(workFlowType, locationID, requestDateStart, requestDateEnd);
        verify(stockRequestDao, times(1))
                .findByWorkFlowTypeAndRequestDateBetween(any(workFlowType.getClass()), any(Date.class), any(Date.class));
    }

    @Test
    public void testGetStockRequestItemsByStockRequestId() {
        
        stockRequestService.getStockRequestItemsByStockRequestId(1L);
        verify(stockRequestItemDao, times(1)).findByStockRequestId(any(Long.class));
    }

}
