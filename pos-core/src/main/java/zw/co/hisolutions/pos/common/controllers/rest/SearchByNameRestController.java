package zw.co.hisolutions.pos.common.controllers.rest;

//package zw.co.hisolutions.common.controllers.rest;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import zw.co.hisolutions.common.entity.BaseEntity; 
//import zw.co.hisolutions.common.service.GenericService;
//
///**
// *
// * @author dgumbo
// */
//public interface SearchByNameRestController<T extends BaseEntity> {  
//    
//    GenericService getService() ;
//
//    @GetMapping(value = "/getByName/{name}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
//    default ResponseEntity<?> getByName (@PathVariable String name) {
//        T t = (T) getService().find(1L) ; //name);
//        //Resource resource = getService().buildResource(entity);
//        //System.out.println("\n T B4 Save : " + resource.getContent() + "\n");
//        return new ResponseEntity<>(t, HttpStatus.OK);
//    }
//}
