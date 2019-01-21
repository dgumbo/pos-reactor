//package zw.co.hisolutions.pos.init.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import zw.co.hisolutions.pos.init.InitializeStartData;
//
///**
// *
// * @author dgumbo
// */
//@Controller 
//@RequestMapping("/init")
//public class InitController { 
//
//    private final InitializeStartData initializeStartData;
//
//    @Autowired
//    public InitController(InitializeStartData initializeStartData) {
//        this.initializeStartData = initializeStartData;
//    }
//    
//    @GetMapping("/services")
//    @ResponseBody
//    public String initData(){
//        initializeStartData.init();
//        return "Data Succesifully initialiazed !!";
//    }    
//}
