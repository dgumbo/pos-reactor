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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.BillType;
import zw.co.psmi.hms.bill.entity.Deposit;
import zw.co.psmi.hms.bill.service.DepositService;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class DepositController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private CurrencyService currencyService;
    
   @RequestMapping(value = "/bill/deposit", method = RequestMethod.GET)
    public String deposit(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Deposit deposit) {
        deposit = deposit == null? new Deposit(): deposit;
        deposit.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("deposit", deposit);
        model.addAttribute("deposits", depositService.findByAllBYSearchCriteria(deposit));
        return "bill/deposit";
    }   
    @RequestMapping(value = "/bill/depositaction/{id}", method = RequestMethod.GET)
    public String depositAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") long id, @RequestParam(value = "patientId", required = false) Long patientId, Model model) {
        Deposit deposit = depositService.getById(id);
      if(id > 0)
         {
          deposit.setDepositItem(depositService.getByDepositId(id));
          model.addAttribute("title", "View Deposit");
         }
      else {
          model.addAttribute("title", "Create New Deposit");
      }
      if(patientId != null && patientId > 0)
         {
         deposit.setPatient(patientService.getById(patientId));
         }
        deposit.setCurrencys(currencyService.getActive());
        deposit.setPaymentTypes(paymentTypeService.getActive());
        deposit.setBillTypes(Arrays.asList(BillType.values()));
        model.addAttribute("deposit", deposit);
        return "bill/depositaction";
    }   
    @RequestMapping(value = "/bill/depositform", method = RequestMethod.POST)
    public String depositform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute Deposit deposit, Model model, RedirectAttributes redirectAttributes) {
        deposit.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        deposit.setUsername(userLogin.getUsername());      
        String msg = this.depositService.save(deposit);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/bill/deposit";
    } 
    @RequestMapping(value = "/bill/depositoutputpopup", method = RequestMethod.GET)
    public String outputReceipt(Model model,@RequestParam(value = "id", required = false) Long id) {
         model.addAttribute("id", id);
        model.addAttribute("type", "DEPOSIT");
        return "bill/singleoutputpopup";
    }  
    @RequestMapping(value = "/bill/institutioninvoiceoutputpopup", method = RequestMethod.GET)
    public String institutioninvoiceoutputpopup(Model model,@RequestParam(value = "id", required = false) Long id) { 
        model.addAttribute("id", id);
        model.addAttribute("type", "INSTITUTION_INVOICE");
        return "bill/singleoutputpopup";
    }  
}
