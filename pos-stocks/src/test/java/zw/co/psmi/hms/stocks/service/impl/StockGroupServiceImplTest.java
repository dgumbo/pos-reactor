
package zw.co.psmi.hms.stocks.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import zw.co.psmi.hms.stocks.dao.StockGroupDao;
import zw.co.psmi.hms.stocks.dao.StockSubGroupDao;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockSubGroup;
import zw.co.psmi.hms.stocks.service.StockGroupService;


public class StockGroupServiceImplTest { 

    private StockGroupService stockGroupService;
    private StockGroupDao stockGroupDao;
    private StockSubGroupDao stockSubGroupDao;

    @Before
    public void setup() {
        this.stockGroupDao = mock(StockGroupDao.class);
        this.stockSubGroupDao = mock(StockSubGroupDao.class);
        this.stockGroupService = new StockGroupServiceImpl(stockGroupDao, stockSubGroupDao);
    }

    @Test
    public void testSave() {
        List<StockSubGroup> list = new ArrayList<>();
        list.add(new StockSubGroup());
        stockGroupService.save(new StockGroup(), list);

        verify(stockGroupDao, times(1)).save(any(StockGroup.class)); 
    }

    @Test
    public void testGetByID() { 
        stockGroupService.getByID(1L);
        verify(stockGroupDao, times(1)).findOne(any(Long.class));

        when(stockGroupDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testFindAll() { 
        stockGroupService.findAll();

        verify(stockGroupDao, times(1)).findAll();
    }

    @Test
    public void testFindAllActive() { 
        stockGroupService.findAllActive();

        verify(stockGroupDao, times(1)).findByActiveStatusTrue();
    }

    @Test
    public void testGetStockSubGroupByID() { 
        stockGroupService.getStockSubGroupByID(1L);
        verify(stockSubGroupDao, times(1)).findByStockGroupIdAndActiveStatusTrue(any(Long.class));

        //when(stockSubGroupDao.findByStockGroupIdAndActiveStatusTrue(any(Long.class))).thenReturn(null);
        //assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testGetStockSubGroupByStockSubGroupID() { 
        stockGroupService.getStockSubGroupByStockSubGroupID(1L);
        verify(stockSubGroupDao, times(1)).findOne(any(Long.class));

        when(stockGroupDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testGetStockSubGroupActive() { 
        stockGroupService.getStockSubGroupActive();
        verify(stockSubGroupDao, times(1)).findByActiveStatusTrue();

        when(stockGroupDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockGroupService.getByID(2L));
    }

    @Test
    public void testGetDao() { 
        assertEquals(stockGroupDao, stockGroupService.getDao());
    }    
}
