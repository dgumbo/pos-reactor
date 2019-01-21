
package zw.co.psmi.hms.stocks.service.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import zw.co.psmi.hms.stocks.dao.StockSupplierDao;
import zw.co.psmi.hms.stocks.entity.StockSupplier;
import zw.co.psmi.hms.stocks.service.StockSupplierService;


public class StockSupplierServiceImplTest { 

    @Test
    public void testGetByID() {
        StockSupplierDao stockSupplierDao = mock(StockSupplierDao.class);
        StockSupplierService stockSupplierService = new StockSupplierServiceImpl(stockSupplierDao);
          
        stockSupplierService.getByID(1L);        
        verify(stockSupplierDao,times(1)).findOne(any(Long.class));
        
        when(stockSupplierDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(stockSupplierService.getByID(2L));  
    }

    @Test
    public void testFindAllActive() {
        StockSupplierDao stockSupplierDao = mock(StockSupplierDao.class);
        StockSupplierService stockSupplierService = new StockSupplierServiceImpl(stockSupplierDao);
          
        stockSupplierService.findAllActive();        
        verify(stockSupplierDao,times(1)).findByActiveStatusTrue();
    }

    @Test
    public void testGetDao() {
        StockSupplierDao stockSupplierDao = mock(StockSupplierDao.class);
        StockSupplierService stockSupplierService = new StockSupplierServiceImpl(stockSupplierDao);

        assertEquals(stockSupplierDao, stockSupplierService.getDao());
    }

    @Test
    public void testSave() {
        StockSupplierDao stockSupplierDao = mock(StockSupplierDao.class);
        StockSupplierService stockSupplierService = new StockSupplierServiceImpl(stockSupplierDao);
          
        stockSupplierService.save(new StockSupplier());
        
        verify(stockSupplierDao,times(1)).save(any(StockSupplier.class));
    }

    @Test
    public void testFindAll() {
        StockSupplierDao stockSupplierDao = mock(StockSupplierDao.class);
        StockSupplierService stockSupplierService = new StockSupplierServiceImpl(stockSupplierDao);
          
        stockSupplierService.findAll();        
        verify(stockSupplierDao,times(1)).findAll();
    }
    
}
