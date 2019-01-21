/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.util.Arrays;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.Deposit;
import zw.co.psmi.hms.bill.entity.DepositAllocation;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.bill.service.DepositAllocationService;
import zw.co.psmi.hms.bill.service.DepositService;
import zw.co.psmi.hms.patient.entity.PaymentFor;
import zw.co.psmi.hms.patient.entity.TransactionStatus;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class DepositAllocationController {
    @Autowired
    private BillService billService;
    @Autowired
    private DepositAllocationService depositAllocationService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private DepositService depositService;
    
   @RequestMapping(value = "/bill/depositallocation", method = RequestMethod.GET)
    public String depositAllocation(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute DepositAllocation depositAllocation) {
        depositAllocation = depositAllocation == null? new DepositAllocation(): depositAllocation;
        depositAllocation.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("depositAllocation", depositAllocation);
        model.addAttribute("depositAllocations", depositAllocationService.findByAllBYSearchCriteria(depositAllocation));
        return "bill/depositallocation";
    }   
    @RequestMapping(value = "/bill/depositallocationaction/{id}", method = RequestMethod.GET)
    public String depositAllocationAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("id") long id, @RequestParam(value = "billId", required = false) Long billId, Model model) {
        Unit unit  = unitService.getByUnitCode(userLogin.getUnitCode());
        DepositAllocation depositAllocation = depositAllocationService.getById(id);
      if(id > 0)
         {
             model.addAttribute("title", "View Deposit Allocation");
         }
      else {
          model.addAttribute("title", "Create New Deposit Allocation");
      }
      if(billId != null && billId > 0)
         {
         Bill bill = billService.getById(billId);
         depositAllocation.setBill(bill);
         List<Deposit> deposit = depositService.findByPatientIdAndUnitIdAndBillTypeAndTransactionStatus(bill.getPatient().getId() , unit.getId(), bill.getBillType(), TransactionStatus.CREATED);
         depositAllocation.setDeposits(deposit);
         }
        depositAllocation.setPaymentFors(Arrays.asList(PaymentFor.values()));
        model.addAttribute("depositAllocation", depositAllocation);
        return "bill/depositallocationaction";
    }   
    @RequestMapping(value = "/bill/depositallocationform", method = RequestMethod.POST)
    public String depositAllocationform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute DepositAllocation depositAllocation, Model model, RedirectAttributes redirectAttributes) {
        depositAllocation.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        String msg = this.depositAllocationService.save(depositAllocation);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/bill/depositallocation";
    } 
    
    @RequestMapping(value = "/bill/depositallocationoutputpopup", method = RequestMethod.GET)
    public String depositallocationoutputpopup(Model model,@RequestParam(value = "id", required = false) Long id) {
         model.addAttribute("id", id);
        model.addAttribute("type", "DEPOSIT_ALLOCATION");
        return "bill/singleoutputpopup";
    }  
}
