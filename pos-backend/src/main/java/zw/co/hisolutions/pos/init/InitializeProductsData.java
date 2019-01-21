package zw.co.hisolutions.pos.init;

//package zw.co.hisolutions.pos.backend.init;
// 
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component; 
//import zw.co.hisolutions.pos.backend.entity.Product;
//import zw.co.hisolutions.pos.backend.entity.ProductCategory;
//import zw.co.hisolutions.pos.backend.entity.ScheduleOfCharge; 
//import zw.co.hisolutions.pos.backend.service.ProductService;
//import zw.co.hisolutions.pos.backend.service.ScheduleOfChargeService;  
//import zw.co.hisolutions.pos.backend.service.ProductCategoryService;
//
///**
// *
// * @author denzil
// */
//@Component
//public class InitializeProductsData {
// 
//    private final ProductService productService;
//    private final ProductCategoryService productTypeService;
//    private final ScheduleOfChargeService scheduleOfChargeService; 
//    private final ProductCategoryService productCategoryService;
//    private Product product3;
//
//    @Autowired
//    public InitializeProductsData(ProductService productService, ProductCategoryService productTypeService, ScheduleOfChargeService scheduleOfChargeService,  ProductCategoryService productCategoryService) {
//        this.productService = productService;
//        this.productTypeService = productTypeService;
//        this.scheduleOfChargeService = scheduleOfChargeService;
//        this.productCategoryService = productCategoryService;
//        //  this.storageService = storageService;
//    }
//
//     private ProductCategory productType1;
//    private ProductCategory productType2;
//    private Product product1;
//    private ScheduleOfCharge scheduleOfCharge1;
//    private ScheduleOfCharge scheduleOfCharge2;
//    private Product product2;
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
//    public void init() {
//        if (productCategoryService.findAll().size() <= 1) {
////            scanFileServerDirectory(); 
//            initScheduleOfCharge();  
//            initServiceCategories();
//            initProduct();
//            verifyIfDBDependenciesIncludedInPom();
//        }
//    }
//
//    void verifyIfDBDependenciesIncludedInPom() {
//        org.hibernate.dialect.H2Dialect h2Dialect;
//        org.hibernate.dialect.MySQLDialect mySQLDialect;
//        org.hibernate.dialect.MySQL5Dialect mySQL5Dialect;
//        org.hibernate.dialect.SQLServerDialect sQLServer2012Dialect;
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
////    private void scanFileServerDirectory() {
////        if (docs != null) {
////            if (docs.size() >= 1) {
////                documenMetadata1 = docs.get(0);
////            }
////            if (docs.size() >= 2) {
////                documenMetadata2 = docs.get(1);
////            }
////            if (docs.size() >= 3) {
////                documenMetadata3 = docs.get(2);
////            }
////            if (docs.size() >= 4) {
////                documenMetadata4 = docs.get(3);
////            }
////            if (docs.size() >= 5) {
////                documenMetadata5 = docs.get(4);
////            }
////        }
////    } 
//  
//    private void initScheduleOfCharge() {
//
//        String scheduleOfCharge1Name = "Microsoft";
//        this.scheduleOfCharge1 = scheduleOfChargeService.findByName(scheduleOfCharge1Name);
//        if (scheduleOfCharge1 == null) {
//            this.scheduleOfCharge1 = new ScheduleOfCharge();
//            //scheduleOfCharge1.setName(scheduleOfCharge1Name);
//            try {
//                scheduleOfCharge1 = scheduleOfChargeService.create(scheduleOfCharge1);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        String scheduleOfCharge2Name = "Oracle";
//        this.scheduleOfCharge2 = scheduleOfChargeService.findByName(scheduleOfCharge2Name);
//        if (scheduleOfCharge2 == null) {
//            this.scheduleOfCharge2 = new ScheduleOfCharge();
//            try {
//                scheduleOfCharge2 = scheduleOfChargeService.create(scheduleOfCharge2);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
// 
//    private void initProduct() {
//
//        String product1Name = "Web Development";
//        this.product1 = productService.findByName(product1Name);
//        if (product1 == null) {
////            DocumentMetadata d1 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("css3"))) {
////                d1 = docs.stream().filter(p -> p.getFilename().contains("css3")).findAny().get();
////            } else {
////                d1 = documenMetadata1;
////            }
//            product1 = new Product();
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
//        String product2Name = "Office Suite";
//        this.product2 = productService.findByName(product2Name);
//        if (product2 == null) {
////            DocumentMetadata d2 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("html5"))) {
////                d2 = docs.stream().filter(p -> p.getFilename().contains("html5")).findAny().get();
////            } else {
////                d2 = documenMetadata2;
////            }
//
//            product2 = new Product();
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
//        String product3Name = "Web Hosting";
//        this.product3 = productService.findByName(product3Name);
//        if (product3 == null) {
////            DocumentMetadata d2 = null;
////            if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains("html5"))) {
////                d2 = docs.stream().filter(p -> p.getFilename().contains("html5")).findAny().get();
////            } else {
////                d2 = documenMetadata2;
////            }
//
//            product3 = new Product();
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
//    }
//}
