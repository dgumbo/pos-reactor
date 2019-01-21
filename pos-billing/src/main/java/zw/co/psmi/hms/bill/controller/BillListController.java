/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.bill.entity.DayEnd;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;
import zw.co.psmi.hms.util.Utils;

/**
 *
 * @author kinah
 */
@Controller
public class BillListController {
    @Autowired
    private BillService billService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private PatientService patientService;
    @RequestMapping(value = "/bill/billlist", method = RequestMethod.GET)
    public String billlist(Model model,@AuthenticationPrincipal UserLogin userLogin,
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
        return "bill/billlist";
    }
   @RequestMapping(value = "/bill/billlistaction/{id}", method = RequestMethod.GET) 
    public String dayEndAction(@AuthenticationPrincipal UserLogin userLogin,  @PathVariable("id") Long id, Model model) {
         
        Bill bill = this.billService.getByID(id);
        bill.setBillItem(billService.getByBillIdActive(bill.getId()));
        model.addAttribute("bill", bill);
        return "bill/billlistaction";
    }
    @RequestMapping(value = "/bill/billlistform", method = RequestMethod.POST)
    public String dayEndform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute Bill bill, Model model, RedirectAttributes redirectAttributes) {
        String msg = this.billService.correctDates(bill);
        if(msg == null || msg.isEmpty())
           {
            redirectAttributes.addFlashAttribute("msg", "setError('Transaction not saved')");
           }
        else
           {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
           }
        return "redirect:/bill/billlist";
    }  
}
