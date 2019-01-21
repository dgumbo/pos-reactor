/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.bill.entity.Receipt;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.bill.service.ReceiptService;
import zw.co.psmi.hms.joborder.dao.JobOrderDao;
import zw.co.psmi.hms.patient.entity.PaymentFor;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class ReceiptController {
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private BillService billService;
    @Autowired
    private JobOrderDao jobOrderDao;
    
      @RequestMapping(value = "/bill/receipt", method = RequestMethod.GET)
    public String receipt(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Receipt receipt) {
        receipt = receipt == null? new Receipt(): receipt;
        receipt.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("receipt", receipt);
        model.addAttribute("billCategory", BillCategory.values());
        model.addAttribute("receipts", receiptService.findReceiptBySearchCriteria(receipt));

        return "bill/receipt";
    }  
    
    @RequestMapping(value = "/bill/receiptaction/{id}/{billId}", method = RequestMethod.GET)
    public String receiptAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") long id, @PathVariable("billId") long billId, @RequestParam(name = "jobOrderId", required = false) Long jobOrderId, Model model) {
        Receipt receipt = receiptService.getByReceiptId(id);
        receipt.setCurrencys(currencyService.getActive());
        receipt.setPaymentTypes(paymentTypeService.getActive());
        receipt.setPaymentFors(Arrays.asList(PaymentFor.values()));
      
        if(billId > 0)
         {
         Bill bill = billService.getById(billId);    
         receipt.setBill(bill);
         receipt.setPatient(bill.getPatient());
         }
        
        if(id > 0)
           {
           receipt.setReceiptItem(receiptService.getItemByReceiptId(id));
           model.addAttribute("title", "View Receipt");
           }
        else {
            model.addAttribute("title", "Create New Receipt");
        }
        if(jobOrderId != null && jobOrderId > 0)
           {
           receipt.setJobOrder(jobOrderDao.findOne(jobOrderId));
           }        
        model.addAttribute("receipt", receipt);
        return "bill/receiptaction";
    }
    
    @RequestMapping(value = "/bill/receiptform", method = RequestMethod.POST)
    public String receiptform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute Receipt receipt, Model model, RedirectAttributes redirectAttributes) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        receipt.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        receipt.setUsername(userLogin.getUsername());    
        receipt.setRoleId(userLogin.getRoleID());
        String msg = this.receiptService.saveReceipt(receipt);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        if(unit.getLob().getName().equalsIgnoreCase("psp") || unit.getLob().getName().equalsIgnoreCase("pharmacy"))
           {
            return "redirect:/psp/sell";
           }
        return "redirect:/patient/visit";
    }
}
