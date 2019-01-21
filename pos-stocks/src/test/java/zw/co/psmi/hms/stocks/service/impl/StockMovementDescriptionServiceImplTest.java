
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
import zw.co.psmi.hms.stocks.dao.StockMovementDescriptionDao;
import zw.co.psmi.hms.stocks.entity.StockMovementDescription;


public class StockMovementDescriptionServiceImplTest {
 
    private StockMovementDescriptionDao stockMovementDescriptionDao; 
    private StockMovementDescriptionServiceImpl stockMovementDescriptionService;

    @Before
    public void setup() {
        this.stockMovementDescriptionDao = mock(StockMovementDescriptionDao.class); 
        this.stockMovementDescriptionService = new StockMovementDescriptionServiceImpl(stockMovementDescriptionDao);
    }

    @Test
    public void testSave() { 
        stockMovementDescriptionService.save(new StockMovementDescription());

        verify(stockMovementDescriptionDao, times(1)).save(any(StockMovementDescription.class)); 
    }

    @Test
    public void testGetByID() { 
        stockMovementDescriptionService.getByID(1L);
        verify(stockMovementDescriptionDao, times(1)).findOne(any(Long.class));

        when(stockMovementDescriptionDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockMovementDescriptionService.getByID(2L));
    }

    @Test
    public void testFindAll() { 
        stockMovementDescriptionService.findAll();

        verify(stockMovementDescriptionDao, times(1)).findAllByOrderByIdDesc();
    }

    @Test
    public void testFindAllActive() { 
        stockMovementDescriptionService.findAllActive();

        verify(stockMovementDescriptionDao, times(1)).findByActiveStatusTrue();
    }

    @Test
    public void testGetDao() { 
        assertEquals(stockMovementDescriptionDao, stockMovementDescriptionService.getDao());
    }
    
}
