
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
import zw.co.psmi.hms.stocks.dao.GenericStockTypeDao;
import zw.co.psmi.hms.stocks.dao.StockSubGroupDao;
import zw.co.psmi.hms.stocks.dao.StockTypeDao;
import zw.co.psmi.hms.stocks.entity.GenericStockType;
import zw.co.psmi.hms.stocks.entity.StockGroup;
import zw.co.psmi.hms.stocks.entity.StockManufacturer;
import zw.co.psmi.hms.stocks.entity.StockSubGroup;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.entity.UnitOfMeasure;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.stocks.service.StockManufacturerService;
import zw.co.psmi.hms.stocks.service.UnitOfMeasureService;


public class GenericStockTypeServiceImplTest {

    GenericStockTypeDao genericStockTypeDao;
    StockTypeDao stockTypeDao;
    UnitOfMeasureService unitOfMeasureService;
    StockManufacturerService stockManufacturerService;
    StockSubGroupDao stockSubGroupDao;
    GenericStockTypeService genericStockTypeService;

    @Before
    public void setup() {
        this.genericStockTypeDao = mock(GenericStockTypeDao.class);
        this.stockTypeDao = mock(StockTypeDao.class);
        this.unitOfMeasureService = mock(UnitOfMeasureService.class);
        this.stockManufacturerService = mock(StockManufacturerService.class);
        this.stockSubGroupDao = mock(StockSubGroupDao.class);
        this.genericStockTypeService = new GenericStockTypeServiceImpl(genericStockTypeDao, stockTypeDao, unitOfMeasureService, stockManufacturerService, stockSubGroupDao);
    }

//    @Test
//    public void testFindAllActive() {
//        genericStockTypeService.findAllActive();
//
//        verify(genericStockTypeDao, times(1)).findByActiveStatusTrue();
//    }

    @Test
    public void testGetDao() {
        assertEquals(genericStockTypeDao, genericStockTypeService.getDao());
    }

    @Test
    public void testSave() {
        StockManufacturer stockManufacturer = new StockManufacturer();
        stockManufacturer.setId(1L);
        UnitOfMeasure unitOfMeasure=new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        List<StockType> list = new ArrayList<>();
        StockType stockType = new StockType() ;
        stockType.setId(1L);
        stockType.setStockManufacturer(stockManufacturer);
        stockType.setUnitOfMeasure(unitOfMeasure);
        list.add(stockType);
        GenericStockType genericStockType = new GenericStockType();
        List<StockSubGroup> stockSubGroups = new ArrayList<>();
        StockSubGroup stockSubGroup = new StockSubGroup();
        stockSubGroup.setId(1L);
        stockSubGroups.add(stockSubGroup);
        
        when(stockSubGroupDao.findOne(any(Long.class))).thenReturn(stockSubGroup);
        when(stockManufacturerService.getByID(any(Long.class))).thenReturn(stockManufacturer);
        when(unitOfMeasureService.getByID(any(Long.class))).thenReturn(unitOfMeasure);
        genericStockType.setStockSubGroups(stockSubGroups);
        genericStockTypeService.save(genericStockType, list);

        verify(genericStockTypeDao, times(1)).save(any(GenericStockType.class));
    }

    @Test
    public void testFindStockTypeByGenericStockTypeId() {
        genericStockTypeService.findStockTypeByGenericStockTypeId(1L);
        verify(stockTypeDao, times(1)).findByGenericStockTypeId(any(Long.class)); 
    }

    @Test
    public void testFindByNameAndStockGroup() {
        StockGroup stockGroup = new StockGroup();
        stockGroup.setId(1L);
        GenericStockType genericStockType = new GenericStockType();
        genericStockType.setId(1L);
        genericStockType.setName("Test Name") ;
        
        genericStockTypeService.findByNameAndStockGroup(genericStockType.getName() ,  stockGroup.getId());        
        verify(genericStockTypeDao, times(1)).findByNameContainingAndStockGroupIdAndActiveStatusTrue(any(genericStockType.getName().getClass()) , any(Long.class));

        stockGroup.setId(0);
        genericStockTypeService.findByNameAndStockGroup(genericStockType.getName() ,  stockGroup.getId());  
        verify(genericStockTypeDao, times(1)).findByNameContainingAndActiveStatusTrue(any(genericStockType.getName().getClass()) );
    }

//    @Test
//    public void testGetStockTypeActive() { 
//        genericStockTypeService.getStockTypeActive();
//        verify(stockTypeDao, times(1)).findByActiveStatusTrue( );
//    }

}
