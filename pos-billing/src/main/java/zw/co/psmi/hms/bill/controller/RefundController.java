/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.Deposit;
import zw.co.psmi.hms.bill.entity.Refund;
import zw.co.psmi.hms.bill.service.RefundService;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.bill.service.DepositService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class RefundController {
    @Autowired
    private BillService billService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private PaymentTypeService paymentTypeService;
   @RequestMapping(value = "/bill/refund", method = RequestMethod.GET)
    public String refund(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Refund refund) {
        refund = refund == null? new Refund(): refund;
        refund.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("refund", refund);
        model.addAttribute("refunds", refundService.findByAllBYSearchCriteria(refund));

        return "bill/refund";
    }   
    @RequestMapping(value = "/bill/refundaction/{id}", method = RequestMethod.GET)
    public String receiptAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") long id, @RequestParam(value = "billId", required = false) Long billId, @RequestParam(value = "depositId", required = false) Long depositId, Model model) {
        Refund refund = refundService.getById(id);
        if (id > 0) {
            model.addAttribute("title", "View Refund");
        }
        else {
            model.addAttribute("title", "Create New Refund");
        }
      if(billId != null && billId > 0)
         {
         Bill bill = billService.getById(billId);    
         refund.setBill(bill);
         refund.setPatient(bill.getPatient());
         refund.setRefundableAmount(bill.getRefundAvailable());
         }
      if(depositId != null && depositId > 0)
         {
         Deposit deposit = depositService.getById(depositId);    
         refund.setDeposit(deposit);
         refund.setPatient(deposit.getPatient());
         refund.setRefundableAmount(deposit.getAvailableAmount());
         }
        refund.setPaymentTypes(paymentTypeService.getActive());
        model.addAttribute("refund", refund);
        return "bill/refundaction";
    }   
    @RequestMapping(value = "/bill/refundform", method = RequestMethod.POST)
    public String receiptform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute Refund refund, Model model, RedirectAttributes redirectAttributes) {
        refund.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        refund.setUsername(userLogin.getUsername());      
        String msg = this.refundService.saveWithReturn(refund);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/bill/refund";
    }
    
    
}
