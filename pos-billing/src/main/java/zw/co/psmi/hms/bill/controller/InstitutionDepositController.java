/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.util.List;
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
import zw.co.psmi.hms.bill.entity.InstitutionDeposit;
import zw.co.psmi.hms.bill.entity.InstitutionInvoice;
import zw.co.psmi.hms.bill.service.InstitutionDepositService;
import zw.co.psmi.hms.patient.entity.InstitutionType;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.InstitutionService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class InstitutionDepositController {
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private InstitutionDepositService institutionDepositService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private CurrencyService currencyService;
   @RequestMapping(value = "/bill/institutiondeposit", method = RequestMethod.GET)
    public String institutionDeposit(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute InstitutionDeposit institutionDeposit) {
        institutionDeposit = institutionDeposit == null? new InstitutionDeposit(): institutionDeposit;
        institutionDeposit.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("institutionDeposit", institutionDeposit);
        model.addAttribute("institutionDeposits", institutionDepositService.findByAllBYSearchCriteria(institutionDeposit));
        return "bill/institutiondeposit";
    }   
    @RequestMapping(value = "/bill/institutiondepositaction/{id}", method = RequestMethod.GET)
    public String institutionDepositAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") long id, Model model) {
        InstitutionDeposit institutionDeposit = institutionDepositService.getById(id);
      if(id > 0)
         {
          institutionDeposit.setInstitutionDepositItem(institutionDepositService.getByInstitutionDepositId(id));
          model.addAttribute("title", "View Deposit to Institution");
         }
      else {
          model.addAttribute("title", "Creat New Deposit to Institution");
      }
        institutionDeposit.setCurrencys(currencyService.getActive());
        institutionDeposit.setPaymentTypes(paymentTypeService.getActive());
        model.addAttribute("institutions", institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
        model.addAttribute("institutionDeposit", institutionDeposit);
        return "bill/institutiondepositaction";
    }   
    @RequestMapping(value = "/bill/institutiondepositform", method = RequestMethod.POST)
    public String institutionDepositform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute InstitutionDeposit institutionDeposit, Model model, RedirectAttributes redirectAttributes) {
        institutionDeposit.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        institutionDeposit.setUsername(userLogin.getUsername());      
        String msg = this.institutionDepositService.save(institutionDeposit);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/bill/institutiondeposit";
    } 
    @RequestMapping(value = "/bill/institutiondepositoutputpopup", method = RequestMethod.GET)
    public String outputReceipt(Model model,@RequestParam(value = "id", required = false) Long id) {
        model.addAttribute("id", id);
        model.addAttribute("type", "INSTITUTION_DEPOSIT");
        return "bill/singleoutputpopup";
    }  
    
    @RequestMapping(value = "/bill/institutiondepositslist", method = RequestMethod.GET)
    public @ResponseBody
    String institutiondepositslist(@RequestParam(value = "institution", required = false) Long id) {
     String   html = "<option value=\"\">Choose Deposit</option>\n";
     List<InstitutionDeposit> institutionDeposits = institutionDepositService.getByInstitutionId(id);
     for(InstitutionDeposit institutionDeposit:institutionDeposits)
               {
               html += "<option value=\""+institutionDeposit.getId()+"****"+institutionDeposit.getAvailableAmount()+"\">"+institutionDeposit.getInstitutionDepositDate()+" - "+institutionDeposit.getAvailableAmount()+"</option>\n";    
               }
        return html;
    }  
}
