/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 package zw.co.psmi.hms.bill.controller;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam; 
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.bill.service.ReceiptService;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;
import zw.co.psmi.hms.util.Utils;

/**
 *
 * @author kinah
 */
@Controller
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private ReceiptService receiptService;
    
   
    
      @RequestMapping(value = "/bill/receiptoutputpopup", method = RequestMethod.GET)
    public String outputReceipt(Model model,@RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("id", id);
        model.addAttribute("type", "RECEIPT");
        return "bill/singleoutputpopup";
    }  
      @RequestMapping(value = "/bill/outputpopup", method = RequestMethod.GET)
    public String outputBill(@AuthenticationPrincipal UserLogin userLogin,Model model,@RequestParam(value = "billId", required = false) Long billId) {      
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
          if(unit.getLob().getName().toLowerCase().contains("psp") || unit.getLob().getName().toLowerCase().contains("pharm"))
           {
           model.addAttribute("psp", true);
           }
         else
          {
           model.addAttribute("psp", false);
          }
         model.addAttribute("bill", billService.getById(billId));
         model.addAttribute("receipts", receiptService.getReceiptByBillId(billId));
        return "bill/outputpopup";
    }  
     @RequestMapping(value = "/bill/getallbillsbysearch", method = RequestMethod.GET)
    public String getallbillsbysearch(Model model,@RequestParam(value = "startDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(value = "endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam(value = "institution") Long institution, @RequestParam(value = "ahfoz") String ahfoz, @RequestParam(value = "unit", required = false) Long unit) {
        List<Bill> bills = billService.findByAllBYSearchCriteriaForEdi(startDate, endDate, institution, ahfoz, unit);
            bills = bills.stream().filter(line-> line.getGrossInsurancePayable().doubleValue() > 0.0).collect(Collectors.toList());
        model.addAttribute("bills", bills);
        model.addAttribute("startDate", getDate(startDate));
        model.addAttribute("endDate", getDate(endDate));
        model.addAttribute("institution", institution);
        model.addAttribute("ahfoz", ahfoz);
        return "bill/getallbillsbysearch";
    }  
     @RequestMapping(value = "/bill/getallbillsbysearchforflaggedoff", method = RequestMethod.GET)
    public String getallbillsbysearchforflaggedoff(Model model,@RequestParam(value = "startDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(value = "endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam(value = "institution") Long institution, @RequestParam(value = "ahfoz") String ahfoz, @RequestParam(value = "unit", required = false) Long unit) {
        List<Bill> bills = billService.findByAllBYSearchCriteriaForEdi(startDate, endDate, institution, ahfoz, unit);
            bills = bills.stream().filter(line-> line.getGrossInsurancePayable().doubleValue() > 0.0).collect(Collectors.toList());
        model.addAttribute("bills", bills);
        model.addAttribute("startDate", getDate(startDate));
        model.addAttribute("endDate", getDate(endDate));
        model.addAttribute("institution", institution);
        model.addAttribute("ahfoz", ahfoz);
        return "bill/getallbillsbysearchforflaggedoff";
    }  
     @RequestMapping(value = "/bill/getallbillsbysearchforsp", method = RequestMethod.GET)
    public String getallbillsbysearchforsp(Model model,@RequestParam(value = "startDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(value = "endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam(value = "institution") Long institution, @RequestParam(value = "ahfoz") String ahfoz) {
   List<Bill> bills = billService.findByAllBYSearchCriteriaForInstitution(startDate, endDate, institution, ahfoz);
            bills = bills.stream().filter(line-> line.getSpecialContractPayable().doubleValue() > 0.0).collect(Collectors.toList());
            model.addAttribute("bills", bills);
        
        return "bill/getallbillsbysearchforsp";
    }  
     @RequestMapping(value = "/bill/getallbillsbyfullsearch", method = RequestMethod.GET)
    public String getallbillsbyfullsearch(Model model,@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "startDate",required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, 
            @RequestParam(value = "endDate",required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, 
            @RequestParam(value = "name",required = false) String name, 
            @RequestParam(value = "billCategory",required = false) BillCategory billCategory, 
            @RequestParam(value = "billNumber",required = false) String billNumber) {
        startDate=startDate==null?new Date():startDate;
        endDate=endDate==null?new Date():endDate;
        billNumber=billNumber==null?"":billNumber;
        name=name==null?"":name;
        billCategory=billCategory==null?BillCategory.INSURANCE:billCategory;
   List<Bill> bills = billService.findByAllBYPatientSearch(startDate, endDate, Utils.prepairStringForQuery(name), billCategory,Utils.prepairStringForQuery(billNumber),userLogin.getUnitCode());
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("billCategory", billCategory);
            model.addAttribute("billCategorys", BillCategory.values());
            model.addAttribute("name", name);
            model.addAttribute("billNumber", billNumber);
            model.addAttribute("bills", bills);
        
        return "bill/getallbillsbyfullsearch";
    }  
   @RequestMapping(value = "/bill/billform", method = RequestMethod.GET)
    public String billform() {
        return "bill/billform";
    }  
     @RequestMapping(value = "/bill/dayendoutputpopup", method = RequestMethod.GET)
    public String dayendoutputpopup(Model model,@RequestParam(value = "id", required = false) Long id) {
         model.addAttribute("id", id);
        model.addAttribute("type", "DAYEND");
        return "bill/singleoutputpopup";
    } 
private String getDate(Date date)
    {
    DateFormat formatter;
    String str_date = "";
    formatter = new SimpleDateFormat("yyyy-MM-dd");
    try{
     str_date = formatter.format(date);
    }catch(Exception e){}
    
    return str_date;
    }       
}
