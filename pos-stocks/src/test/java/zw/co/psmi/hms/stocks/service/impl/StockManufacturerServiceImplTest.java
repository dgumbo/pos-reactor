
package zw.co.psmi.hms.stocks.service.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import zw.co.psmi.hms.stocks.dao.StockManufacturerDao;
import zw.co.psmi.hms.stocks.entity.StockManufacturer;


public class StockManufacturerServiceImplTest {
    
    private StockManufacturerDao stockManufacturerDao;
    private StockManufacturerServiceImpl stockManufacturerService;

    @Before
    public void setup() {
        this.stockManufacturerDao = mock(StockManufacturerDao.class); 
        this.stockManufacturerService = new StockManufacturerServiceImpl( stockManufacturerDao);
    }

    @Test
    public void testSave() {  
        stockManufacturerService.save(new StockManufacturer());

        verify(stockManufacturerDao, times(1)).save(any(StockManufacturer.class)); 
    }

    @Test
    public void testGetByID() { 
        stockManufacturerService.getByID(1L);
        verify(stockManufacturerDao, times(1)).findOne(any(Long.class));

        when(stockManufacturerDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockManufacturerService.getByID(2L));
    }

    @Test
    public void testFindAll() { 
        stockManufacturerService.findAll();

        verify(stockManufacturerDao, times(1)).findAll();
    }

    @Test
    public void testFindAllActive() { 
        stockManufacturerService.findAllActive();

        verify(stockManufacturerDao, times(1)).findByActiveStatusTrue();
    }

    @Test
    public void testGetDao() { 
        assertEquals(stockManufacturerDao, stockManufacturerService.getDao());
    }
    
}
