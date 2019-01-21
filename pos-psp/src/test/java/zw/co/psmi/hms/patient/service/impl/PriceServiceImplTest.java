/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.patient.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import zw.co.psmi.hms.patient.dao.PriceDao;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.patient.entity.Price;
import zw.co.psmi.hms.stocks.entity.StockType;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;

/**
 *
 * @author com4t
 */
public class PriceServiceImplTest {
    
    
    public PriceServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of save method, of class PriceServiceImpl.
     */
    @Test @Ignore
    public void testSave() {
        System.out.println("save");
        List<Price> price = null;
        PriceServiceImpl instance = new PriceServiceImpl();
        String expResult = "";
        String result = instance.save(price);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getById method, of class PriceServiceImpl.
     */
    @Test @Ignore
    public void testGetById() {
        System.out.println("getById");
        Long id = null;
        PriceServiceImpl instance = new PriceServiceImpl();
        Price expResult = null;
        Price result = instance.getById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAll method, of class PriceServiceImpl.
     */
    @Test @Ignore
    public void testGetAll() {
        System.out.println("getAll");
        PriceServiceImpl instance = new PriceServiceImpl();
        List<Price> expResult = null;
        List<Price> result = instance.getAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getActive method, of class PriceServiceImpl.
     */
    @Test @Ignore
    public void testGetActive() {
        System.out.println("getActive");
        PriceServiceImpl instance = new PriceServiceImpl();
        List<Price> expResult = null;
        List<Price> result = instance.getActive();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findBySearch method, of class PriceServiceImpl.
     */
    @Test
    public void testFindBySearch() {
        System.out.println("findBySearch");
        GenericStockTypeService genericStockTypeService=Mockito.mock(GenericStockTypeService.class);
        PriceDao priceDao=Mockito.mock(PriceDao.class);
        Long lob = null;
        Long unit = null;
        BillCategory billCategory = null;
        Long institution = null;
        PriceServiceImpl instance = new PriceServiceImpl();
        instance.setGenericStockTypeService(genericStockTypeService);
        instance.setPriceDao(priceDao);
        StockType s1=new StockType();
        StockType s2=new StockType();
        StockType s3=new StockType();
        s1.setId(1);
        s2.setId(2);
        s3.setId(3);
        Price p1=new Price();
        p1.setId(4);
        p1.setStockType(s1);
        List<Price> prices=new ArrayList<>();
        List<StockType> types=new ArrayList<>();
        types.add(s3);
        types.add(s2);
        types.add(s1);
        prices.add(p1);
       // Mockito.when(priceDao.findByLobIdAndBillCategoryAndActiveStatusTrue(
        //        Matchers.any(Long.class), Matchers.any(BillCategory.class))).thenReturn(prices);
//        Mockito.when(genericStockTypeService.getStockTypeActive()).thenReturn(types);
//        int expResult =3;
//        List<Price> result = instance.findBySearch(lob, unit, billCategory, institution);
//        assertEquals(expResult, result.size());
    }
    
}
