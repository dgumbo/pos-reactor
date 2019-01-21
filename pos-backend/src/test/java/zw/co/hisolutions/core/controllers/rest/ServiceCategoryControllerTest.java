//package zw.co.hisolutions.core.controllers.rest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.nio.charset.Charset; 
//import java.util.Date;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner; 
//
//import static org.hamcrest.Matchers.*;
//import org.junit.Ignore;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType; 
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import org.springframework.web.context.WebApplicationContext;
//import zw.co.hisolutions.core.entity.ProductCategory;
//import zw.co.hisolutions.core.service.ProductCategoryService;
//import zw.co.hisolutions.pos.storage.entity.DocumentMetadata;
//import zw.co.hisolutions.pos.storage.entity.Status; 
//import zw.co.hisolutions.pos.storage.service.DocumentMetadataService;
//
///**
// *
// * @author dgumbo
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest 
////@WebAppConfiguration
//@AutoConfigureMockMvc
//public class ProductCategoryControllerTest {
//
//    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(),
//            Charset.forName("utf8"));
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductCategoryService productCategoryService;
//    
//    @Autowired
//    private DocumentMetadataService documentMetadataService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    private ProductCategory productCategory;
//
////    private HttpMessageConverter mappingJackson2HttpMessageConverter;
////    @Autowired
////    void setConverters(HttpMessageConverter<?>[] converters) {
////
////        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
////                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
////                .findAny()
////                .orElse(null);
////
////        assertNotNull("the JSON message converter must not be null",
////                this.mappingJackson2HttpMessageConverter);
////    }
//
//    @Before
//    public void setUp() throws Exception {
//        this.mockMvc = webAppContextSetup(webApplicationContext).build();
//
//        this.productCategory = new ProductCategory();
//        productCategory.setId(1);
//        productCategory.setVersion(1);
//        productCategory.setCreatedByUser("test");
//        productCategory.setCreationTime(new Date());//"2018-03-19T20:59:52.803+0000");
//        productCategory.setModifiedByUser("test");
//        productCategory.setModificationTime(new Date());//("2018-03-19T20:59:52.803+0000");
//        productCategory.setActiveStatus(true);
//        productCategory.setName("Affordable Professional Web Design");
//        productCategory.setDescription("Affordable Professional Web Design");
//        productCategory.setDisplayContent("<p>Affordable Professional Web Design</p>");
//
//        DocumentMetadata imageMetadata = new DocumentMetadata();
//        imageMetadata.setVersion(1);
//        imageMetadata.setCreatedByUser("test");
//        imageMetadata.setCreationTime(new Date());//2018-03-19T21:00:43.100+0000";
//        imageMetadata.setModifiedByUser("test");
//        imageMetadata.setModificationTime(new Date());//2018-03-19T21:00:43.100+0000";
//        imageMetadata.setActiveStatus(true);
//        imageMetadata.setFilename("showcase.jpg");
//        imageMetadata.setMimeType("image/jpeg");
//        imageMetadata.setStatus(Status.Success);
//        imageMetadata.setDescription(null);
//        imageMetadata = documentMetadataService.create(imageMetadata);
//
//        DocumentMetadata thumbnailMetadata = new DocumentMetadata();
//        thumbnailMetadata.setVersion(1);
//        thumbnailMetadata.setCreatedByUser("test");
//        thumbnailMetadata.setCreationTime(new Date());//2018-03-19T21:00:43.100+0000";
//        thumbnailMetadata.setModifiedByUser("test");
//        thumbnailMetadata.setModificationTime(new Date());//2018-03-19T21:00:43.100+0000";
//        thumbnailMetadata.setActiveStatus(true);
//        thumbnailMetadata.setFilename("html5.png");
//        thumbnailMetadata.setMimeType("image/png");
//        thumbnailMetadata.setStatus(Status.Success);
//        thumbnailMetadata.setDescription(null);
//        thumbnailMetadata = documentMetadataService.create(thumbnailMetadata);
//
//        productCategory.setImageMetadata(imageMetadata);
//        productCategory.setThumbnailMetadata(thumbnailMetadata);
//
//    }
//
//    /**
//     * Test of getService method, of class ProductCategoryController.
//     */
//    @Test
//    public void testCreateProductCategory() throws Exception {
//        String productCategoryJson = objectMapper.writeValueAsString(productCategory);
//
//        //System.out.println("\nproductCategoryJson : " + productCategoryJson);
//        //productCategoryJson = "{\"id\":1,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.321+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.321+0000\",\"activeStatus\":true,\"name\":\"Affordable Professional Web Design\",\"description\":\"Affordable Professional Web Design\",\"displayContent\":\"<p>Affordable Professional Web Design</p>\",\"imageMetadata\":{\"id\":1,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.329+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.329+0000\",\"activeStatus\":true,\"filename\":\"showcase.jpg\",\"mimeType\":\"image/jpeg\",\"status\":\"Success\",\"description\":\"null\"},\"thumbnailMetadata\":{\"id\":2,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.332+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.332+0000\",\"activeStatus\":true,\"filename\":\"html5.png\",\"mimeType\":\"image/png\",\"status\":\"Success\",\"description\":\"nulle\"}}";
//        //productCategoryJson = "{\"id\":1,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.321+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.321+0000\",\"activeStatus\":true,\"name\":\"Affordable Professional Web Design\",\"description\":\"Affordable Professional Web Design\",\"displayContent\":\"<p>Affordable Professional Web Design</p>\",\"imageMetadata\":{\"id\":1,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.329+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.329+0000\",\"activeStatus\":true,\"filename\":\"showcase.jpg\",\"mimeType\":\"image/jpeg\",\"status\":\"Success\",\"description\":null},\"thumbnailMetadata\":{\"id\":2,\"version\":1,\"createdByUser\":\"test\",\"creationTime\":\"2018-03-20T11:39:46.332+0000\",\"modifiedByUser\":\"test\",\"modificationTime\":\"2018-03-20T11:39:46.332+0000\",\"activeStatus\":true,\"filename\":\"html5.png\",\"mimeType\":\"image/png\",\"status\":\"Success\",\"description\":null}}";
//        //System.out.println("productCategoryJson : " + productCategoryJson+"\n");
//        this.mockMvc.perform(post("/rest/servicecategories")
//                .contentType(contentType)
//                .content(productCategoryJson))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    /**
//     * Test of getControllerClass method, of class ProductCategoryController.
//     */
//    @Test
//    @Ignore
//    public void testGetControllerClass() {
//        System.out.println("getControllerClass");
//        ProductCategoryController instance = null;
//        Class expResult = null;
//        Class result = instance.getControllerClass();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//}
