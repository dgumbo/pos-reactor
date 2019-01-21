//package zw.co.hisolutions.pos.init;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import zw.co.hisolutions.pos.stocks.entity.ProductCategory;
//import zw.co.hisolutions.pos.stocks.service.ProductCategoryService; 
//
///**
// *
// * @author dgumbo
// */
//public class InitServiceCategories {
//
//    final ProductCategoryService productCategoryService;
////    final List<DocumentMetadata> docs;
//
//    public InitServiceCategories(ProductCategoryService productCategoryService/*, List<DocumentMetadata> docs*/) {
//        this.productCategoryService = productCategoryService;
////        this.docs = docs;
//    }
//
//    public List<ProductCategory> init() {
//        List<ProductCategory> serviceCategories = new ArrayList();
//
//        serviceCategories.add(initWebDesignSC());
//        serviceCategories.add(initTrainingSC());
//        serviceCategories.add(initHardwareSC());
//        serviceCategories.add(initConsultancySC());
//
//        return serviceCategories;
//    }
//
//    public ProductCategory initWebDesignSC() {
//        String productCategoryName = "Drinks";
//        String scShortDescription = "Drinks";
//
//        ProductCategory productCategory = productCategoryService.findByName(productCategoryName);
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productCategoryName); 
//            productCategory.setDescription("drinks"); 
//
//            try {
//                productCategory = productCategoryService.create(productCategory);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return productCategory;
//    }
//
//    public ProductCategory initTrainingSC() {
//        String productCategoryName = "Food";
//        String scShortDescription = "Foods";
//
//        ProductCategory productCategory = productCategoryService.findByName(productCategoryName);
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productCategoryName); 
//            productCategory.setDescription(productCategoryName);  
//
//            try {
//                productCategory = productCategoryService.create(productCategory);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return productCategory;
//    }
//
//    public ProductCategory initHardwareSC() {
//        String productCategoryName = "Soaps";
//        String scShortDescription = "Soaps";
//
//        ProductCategory productCategory = productCategoryService.findByName(productCategoryName);
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productCategoryName); 
//            productCategory.setDescription(productCategoryName);  
//            try {
//                productCategory = productCategoryService.create(productCategory);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return productCategory;
//    }
//
//    public ProductCategory initConsultancySC() {
//        String productCategoryName = "Confectionery";
//        String scShortDescription = "Confectionery";
//
//        ProductCategory productCategory = productCategoryService.findByName(productCategoryName);
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productCategoryName); 
//            productCategory.setDescription(productCategoryName);  
//            try {
//                productCategory = productCategoryService.create(productCategory);
//            } catch (Exception ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return productCategory;
//    }
//
////    DocumentMetadata getDocumentMetadata(String DocumentName) {
////        DocumentMetadata documentMetadata = null;
////        if (docs != null && docs.stream().anyMatch(p -> p.getFilename().contains(DocumentName))) {
////            documentMetadata = docs.stream().filter(p -> p.getFilename().contains(DocumentName)).findAny().get();
////        }
////        return documentMetadata;
////    }
//}
