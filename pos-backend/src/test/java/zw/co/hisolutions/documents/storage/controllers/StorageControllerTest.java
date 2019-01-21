//package zw.co.hisolutions.documents.storage.controllers;
//
//import com.google.gson.JsonArray;
//import java.nio.charset.Charset;
//import java.nio.file.Paths;
//import java.util.stream.Stream;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.Ignore;
//import org.junit.runner.RunWith;
//import static org.mockito.BDDMockito.given;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import zw.co.hisolutions.pos.storage.service.StorageService;
//
//import static org.mockito.BDDMockito.then;
//import org.springframework.http.MediaType;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
//
///**
// *
// * @author denzil
// */
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest
//public class StorageControllerTest {
//    private final MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(),
//            Charset.forName("utf8"));
//
//    @Autowired
//    private MockMvc mvc; 
//
//    @MockBean
//    private StorageService storageService;
//
//    @Before
//    public void setUp() throws Exception {
//        
//    } 
// 
//    @Test
//    public void shouldListUploadedFiles() throws Exception {
//        //System.out.println("listUploadedFiles");
//
//        given(storageService .loadAll())
//                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
//
//        JsonArray jsonArray = new JsonArray();
//        jsonArray.add("http://localhost/storage/view/first.txt");
//        jsonArray.add("http://localhost/storage/view/second.txt");
//
//        mvc.perform(get("/storage/listallfiles"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(jsonContentType))
//                .andExpect(content().json(jsonArray.toString())) //.andDo(print())
//                ;
//    }
// 
//    /**
//     * Test of UploadFile method, of class StorageController.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test 
//    public void testUploadFile() throws Exception {
//        System.out.println("UploadFile");
//
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
//                "text/plain", "Spring Framework".getBytes());
//
//        mvc.perform(multipart("/storage/upload").file(multipartFile)
//                .param("filename", "value.txt")
//        /*.flashAttr("filename", "value.txt")
//                .requestAttr("filename", "value.txt") 
//                .sessionAttr("filename", "value.txt")*/
//        )
//                .andExpect(status().isCreated())
//                //.andExpect(content().contentType(jsonContentType)) 
//                .andDo(print());
//
//        then(storageService).should().store(multipartFile);
//    }
// 
//    /**
//     * Test of index method, of class StorageController.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testStorageIndexHandler() throws Exception {
//
//        given(storageService.getStorageLocation())
//                .willReturn("/upload-path");
//
//        mvc.perform(get("/storage/"))
//                .andExpect(status().isOk()) // .andExpect(content().contentType(MediaType.))
//                // .andExpect(content().string(Matchers.contains("Storage Service Storage Location :"))) 
//                // .andDo(print())
//                ;
//    }
//
//    /**
//     * Test of serveFile method, of class StorageController.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    @Ignore
//    public void testServeFile() throws Exception {
//        System.out.println("serveFile");
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of handleFileUpload method, of class StorageController.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    @Ignore
//    public void testHandleFileUpload() throws Exception {
//        System.out.println("handleFileUpload");
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of handleStorageFileNotFound method, of class StorageController.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    @Ignore
//    public void testHandleStorageFileNotFound() throws Exception {
//        System.out.println("handleStorageFileNotFound");
//        fail("The test case is a prototype.");
//    }
//
//}
