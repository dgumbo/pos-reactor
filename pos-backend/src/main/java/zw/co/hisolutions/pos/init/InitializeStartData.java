//package zw.co.hisolutions.pos.init;
// 
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component; 
//import zw.co.hisolutions.pos.backend.enums.ChargeType; 
//import zw.co.hisolutions.pos.stocks.entity.StockItem;
//import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
//import zw.co.hisolutions.pos.backend.entity.ScheduleOfPrice; 
//import zw.co.hisolutions.pos.stocks.service.ProductService;
//import zw.co.hisolutions.pos.stocks.service.ProductCategoryService;
//import zw.co.hisolutions.pos.backend.service.ScheduleOfPriceService;
//import zw.co.hisolutions.pos.stocks.entity.CurrentStock;
//import zw.co.hisolutions.pos.stocks.entity.StockStatus;
//import zw.co.hisolutions.pos.stocks.service.CurrentStockService;
//
///**
// *
// * @author denzil
// */
//@Component
//public class InitializeStartData {
// 
//    private final ProductService productService; 
//    private final CurrentStockService currentStockService; 
//    private final ScheduleOfPriceService scheduleOfChargeService; 
//    private final ProductCategoryService productCategoryService;
//
//     private ProductCategory productType1;
//    private ProductCategory productType2;
//    private StockItem product1;
//    private StockItem product2;
//    private StockItem product3;
//    private StockItem product4;
//    private ScheduleOfPrice scheduleOfCharge1;
//    private ScheduleOfPrice scheduleOfCharge2;
//    private ScheduleOfPrice scheduleOfCharge3;
//    private CurrentStock currentStock1;
//    private CurrentStock currentStock2;
//    private CurrentStock currentStock3;
//    private ScheduleOfPrice scheduleOfCharge4;
//    private ScheduleOfPrice scheduleOfCharge5;
//    private ScheduleOfPrice scheduleOfCharge6;
//    private ScheduleOfPrice scheduleOfCharge7;
//     private ProductCategory productCategory1;
//    private ProductCategory productCategory2;
//    private ProductCategory productCategory3;
////    private DocumentMetadata documenMetadata1;
////    private DocumentMetadata documenMetadata2;
////    private DocumentMetadata documenMetadata3;
////    private DocumentMetadata documenMetadata4;
////    private DocumentMetadata documenMetadata5;
////    private List<DocumentMetadata> docs;
//
//    @Autowired
//    public InitializeStartData(ProductService productService, ScheduleOfPriceService scheduleOfChargeService,  ProductCategoryService productCategoryService, CurrentStockService currentStockService) {
//        this.productService = productService; 
//        this.scheduleOfChargeService = scheduleOfChargeService;
//        this.productCategoryService = productCategoryService;
//        this.currentStockService = currentStockService ;
//        //  this.storageService = storageService;
//    }
//
//    public void init() {
//        if (productCategoryService.findAll().size() <= 1) {
////            scanFileServerDirectory(); 
//            initServiceCategories();
//            initProduct();
////            initCurrentStock();  
//            initScheduleOfCharge();  
//            verifyIfDBDependenciesIncludedInPom();
//        }
//    }
//
//    void verifyIfDBDependenciesIncludedInPom() {
//        org.hibernate.dialect.H2Dialect h2Dialect;
//        org.hibernate.dialect.MySQLDialect mySQLDialect;
//        org.hibernate.dialect.MySQL5Dialect mySQL5Dialect;
//        org.hibernate.dialect.SQLServerDialect sQLServer2012Dialect;
//        
//        com.microsoft.sqlserver.jdbc.SQLServerDriver sqlDriver ;
//        
//        com.mysql.cj.jdbc.Driver mysqlNewDriver;
//        com.mysql.jdbc.Driver mysqlDriver;
//        //com.microsoft.sqlserver.jdbc.SQLServerDriver sQLServerDriver ;
//        org.h2.Driver h2Driver;
//    }
//
//    private void initServiceCategories() {
//        InitServiceCategories initSC = new InitServiceCategories(productCategoryService/*, docs*/);
//
//        List<ProductCategory> serviceCategories = initSC.init();
//
//        this.productCategory1 = serviceCategories.get(0);
//        this.productCategory2 = serviceCategories.get(1);
//        this.productCategory3 = serviceCategories.get(2);
//    } 
//    
//    private void initProduct() {
//
//        String product1Name = "Coke";
//        this.product1 = productService.findByName(product1Name);
//        if (product1 == null) {
////            DocumentMetadata d1 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("css3"))) {
////                d1 = docs.stream().filter(p -> p.getFilename().contains("css3")).findAny().get();
////            } else {
////                d1 = documenMetadata1;
////            }
//            product1 = new StockItem();
//            product1.setName(product1Name);
//            product1.setProductCategory(productCategory1);
//            product1.setDescription(product1Name);   
//
//            try {
//                this.product1 = productService.create(product1);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        String product2Name = "Sugar";
//        this.product2 = productService.findByName(product2Name);
//        if (product2 == null) {
////            DocumentMetadata d2 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("html5"))) {
////                d2 = docs.stream().filter(p -> p.getFilename().contains("html5")).findAny().get();
////            } else {
////                d2 = documenMetadata2;
////            }
//
//            product2 = new StockItem();
//            product2.setName(product2Name);
//            product2.setProductCategory(productCategory2); 
//            product2.setDescription(product2Name);   
//
//            try {
//                product2 = productService.create(product2);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        String product3Name = "Cooking Oil";
//        this.product3 = productService.findByName(product3Name);
//        if (product3 == null) {
////            DocumentMetadata d2 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("html5"))) {
////                d2 = docs.stream().filter(p -> p.getFilename().contains("html5")).findAny().get();
////            } else {
////                d2 = documenMetadata2;
////            }
//
//            product3 = new StockItem();
//            product3.setName(product3Name);
//            product3.setProductCategory(productCategory1); 
//            product3.setDescription(product3Name);   
//
//            try {
//                product3 = productService.create(product3);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        String product4Name = "Zap Nax";
//        this.product4 = productService.findByName(product4Name);
//        if (product4 == null) {
////            DocumentMetadata d2 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("html5"))) {
////                d2 = docs.stream().filter(p -> p.getFilename().contains("html5")).findAny().get();
////            } else {
////                d2 = documenMetadata2;
////            }
//
//            product4 = new StockItem();
//            product4.setName(product4Name);
//            product4.setProductCategory(productCategory2); 
//            product4.setDescription(product4Name);   
//
//            try {
//                product4 = productService.create(product4);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    
//  
//    private void initScheduleOfCharge() {
// 
////        this.scheduleOfCharge1 = scheduleOfChargeService.findByName(scheduleOfCharge1Name);
//        if (scheduleOfCharge1 == null) {
//            scheduleOfCharge1 = new ScheduleOfPrice();
//            scheduleOfCharge1.setChargeType(ChargeType.PRIMARY);
//            scheduleOfCharge1.setCurrency(Currency.BOND);
//            scheduleOfCharge1.setProduct(product1);
//            scheduleOfCharge1.setPrice(new BigDecimal( 13));
//            try {
//                scheduleOfCharge1 = scheduleOfChargeService.create(scheduleOfCharge1);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge2 == null) {
//            scheduleOfCharge2 = new ScheduleOfPrice();
//            scheduleOfCharge2.setChargeType(ChargeType.FIXED);
//            //scheduleOfCharge2.setCurrency(Currency.USD);
//            scheduleOfCharge2.setProduct(product1);
//            scheduleOfCharge2.setPrice(new BigDecimal(2));
//            try {
//                scheduleOfCharge2 = scheduleOfChargeService.create(scheduleOfCharge2);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge3== null) {
//            scheduleOfCharge3 = new ScheduleOfPrice();
//            scheduleOfCharge3.setChargeType(ChargeType.FIXED);
//            scheduleOfCharge3.setCurrency(Currency.RAND);
//            scheduleOfCharge3.setProduct(product1);
//            scheduleOfCharge3.setPrice(new BigDecimal(260));
//            try {
//                scheduleOfCharge3= scheduleOfChargeService.create(scheduleOfCharge3);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge4== null) {
//            scheduleOfCharge4 = new ScheduleOfPrice();
//            scheduleOfCharge4.setChargeType(ChargeType.PRIMARY);
//            scheduleOfCharge4.setCurrency(Currency.BOND);
//            scheduleOfCharge4.setProduct(product2);
//            scheduleOfCharge4.setPrice(new BigDecimal(4));
//            try {
//                scheduleOfCharge4= scheduleOfChargeService.create(scheduleOfCharge4);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge5 == null) {
//            scheduleOfCharge5 = new ScheduleOfPrice();
//            scheduleOfCharge5.setChargeType(ChargeType.RATED);
//            scheduleOfCharge5.setCurrency(Currency.USD);
//            scheduleOfCharge5.setProduct(product2);
//            scheduleOfCharge5.setRatio(1.6);
//            try {
//                scheduleOfCharge5 = scheduleOfChargeService.create(scheduleOfCharge5);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge6 == null) {
//            scheduleOfCharge6 = new ScheduleOfPrice();
//            scheduleOfCharge6.setChargeType(ChargeType.PRIMARY);
//            scheduleOfCharge6.setCurrency(Currency.BOND);
//            scheduleOfCharge6.setProduct(product3);
//            scheduleOfCharge6.setPrice(new BigDecimal(6.50));
//            try {
//                scheduleOfCharge6 = scheduleOfChargeService.create(scheduleOfCharge6);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
////        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge7 == null) {
//            scheduleOfCharge7 = new ScheduleOfPrice();
//            scheduleOfCharge7.setChargeType(ChargeType.PRIMARY);
//            scheduleOfCharge7.setCurrency(Currency.BOND);
//            scheduleOfCharge7.setProduct(product4);
//            scheduleOfCharge7.setPrice(new BigDecimal(6.50));
//            try {
//                scheduleOfCharge7 = scheduleOfChargeService.create(scheduleOfCharge7);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
// 
//    
//  
//    private void initCurrentStock() {
// 
//        this.currentStock1 = currentStockService.getAllByProductAndBatchNumberAndConsignmentAndStockStatus(product1, "bn", false, StockStatus.AVAILABLE);
//        if (currentStock1 == null) {
//            currentStock1 = new CurrentStock();
//            
//            currentStock1.setProduct(product1);
//            currentStock1.setBatchNumber("bn"); 
//            currentStock1.setCashOnly(false); 
//            currentStock1.setConsignment(false); 
//            currentStock1.setActiveStatus(true);
//            currentStock1.setQuantity(500); 
//            currentStock1.setStockStatus(StockStatus.AVAILABLE); 
//            currentStock1.setUnitCost(new BigDecimal(0.75));   
//            try {
//                currentStock1 = currentStockService.create(currentStock1);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
// 
//        this.currentStock2 = currentStockService.getAllByProductAndBatchNumberAndConsignmentAndStockStatus(product2, "bn", false, StockStatus.AVAILABLE);
//        if (currentStock2 == null) {
//            currentStock2 = new CurrentStock();
//            currentStock2.setProduct(product2);
//            currentStock2.setBatchNumber("bn"); 
//            currentStock2.setCashOnly(false); 
//            currentStock2.setActiveStatus(true);
//            currentStock2.setConsignment(false); 
//            currentStock2.setQuantity(20); 
//            currentStock2.setStockStatus(StockStatus.AVAILABLE); 
//            currentStock2.setUnitCost(new BigDecimal(1.75));   
//            try {
//                currentStock2 = currentStockService.create(currentStock2);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
// 
//        this.currentStock3 = currentStockService.getAllByProductAndBatchNumberAndConsignmentAndStockStatus(product2, "bn", true, StockStatus.AVAILABLE);
//        if (currentStock3 == null) {
//            currentStock3 = new CurrentStock();
//            currentStock3.setProduct(product2);
//            currentStock3.setBatchNumber("bn"); 
//            currentStock3.setCashOnly(false); 
//            currentStock3.setActiveStatus(true);
//            currentStock3.setConsignment(true); 
//            currentStock3.setQuantity(500); 
//            currentStock3.setStockStatus(StockStatus.AVAILABLE); 
//            currentStock3.setUnitCost(new BigDecimal(1.75));  
////            try {
//                currentStock3 = currentStockService.create(currentStock3);
////            } catch (Exception ex) {
////                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
////            }
//        }
//    }
// 
//}
