
package zw.co.psmi.hms.stocks.service.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.*; 
import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.psmi.hms.stocks.dao.UnitOfMeasureDao;
import zw.co.psmi.hms.stocks.entity.UnitOfMeasure;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


public class UnitOfMeasureServiceImplTest { 

    @Test
    public void testFindByActiveStatusTrue() {
        UnitOfMeasureDao unitOfMeasureDao=mock(UnitOfMeasureDao.class);
        
        UnitOfMeasureService measureService = new UnitOfMeasureServiceImpl(unitOfMeasureDao);
        measureService.findByActiveStatusTrue();
        verify(unitOfMeasureDao,times(1)).findByActiveStatusTrue();
    }


    @Test
    public void testdFindByActiveStatusTrue() {
        UnitOfMeasureDao unitOfMeasureDao=mock(UnitOfMeasureDao.class);        
        UnitOfMeasureService measureService = new UnitOfMeasureServiceImpl(unitOfMeasureDao);
        
        measureService.findByActiveStatusTrue();
        verify(unitOfMeasureDao,times(1)).findByActiveStatusTrue();
    }

    
    
    @Test
    public void testGetDao() {
        UnitOfMeasureDao unitOfMeasureDao=mock(UnitOfMeasureDao.class);        
        UnitOfMeasureService measureService = new UnitOfMeasureServiceImpl(unitOfMeasureDao);
        
        assertEquals(unitOfMeasureDao,measureService.getDao());
    }
            
    @Test
    public void testSave() {
        UnitOfMeasureDao unitOfMeasureDao=mock(UnitOfMeasureDao.class);        
        UnitOfMeasureService measureService = new UnitOfMeasureServiceImpl(unitOfMeasureDao);
          
        measureService.save(new UnitOfMeasure());
        
        verify(unitOfMeasureDao,times(1)).save(any(UnitOfMeasure.class));
    }
}
