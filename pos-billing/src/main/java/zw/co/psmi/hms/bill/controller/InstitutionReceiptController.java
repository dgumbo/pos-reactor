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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.InstitutionReceipt;
import zw.co.psmi.hms.bill.service.InstitutionReceiptService;
import zw.co.psmi.hms.patient.entity.Institution;
import zw.co.psmi.hms.patient.entity.InstitutionType;
import zw.co.psmi.hms.patient.service.InstitutionService;

/**
 *
 * @author kinah
 */
@Controller
public class InstitutionReceiptController {
    @Autowired
    private InstitutionReceiptService institutionReceiptService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private UnitService unitService;
    @RequestMapping(value = "/bill/institutionreceipt")
    public String institutionReceipt(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute InstitutionReceipt institutionReceipt) {
        institutionReceipt = institutionReceipt == null? new InstitutionReceipt():institutionReceipt;
        if(institutionReceipt.getInstitution() == null)
           {
           institutionReceipt.setInstitution(new Institution());
           institutionReceipt.getInstitution().setId(0L);
           }
        model.addAttribute("institutions", institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
         model.addAttribute("ahfozs", unitService.getActiveAhfoz());
        model.addAttribute("institutionReceipts", this.institutionReceiptService.getAllBySearch(institutionReceipt));
        return "bill/institutionreceipt";
    }
    @RequestMapping(value = "/bill/institutionreceiptaction/{id}", method = RequestMethod.GET)
    public String institutionReceiptAction( @PathVariable("id") Long id, Model model) {
         
        InstitutionReceipt institutionReceipt = this.institutionReceiptService.getById(id);
        if(id > 0)
           {
           institutionReceipt.setInstitutionReceiptItems(institutionReceiptService.getInstitutionReceiptItemByInstitutionReceiptId(id));
           model.addAttribute("title", "View Receipt to Institution");
           }
        else {
            model.addAttribute("title", "Create New Receipt to Institution");
        }
        model.addAttribute("institutions", institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
         model.addAttribute("ahfozs", unitService.getActiveAhfoz());
        model.addAttribute("institutionReceipt", institutionReceipt);
        return "bill/institutionreceiptaction";
    }
     
    @RequestMapping(value = "/bill/institutionreceiptform", method = RequestMethod.POST)
    public String institutionReceiptform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute InstitutionReceipt institutionReceipt, Model model, RedirectAttributes redirectAttributes) {
       String msg = this.institutionReceiptService.save(institutionReceipt);
       if(msg.equals("2"))
            {
            redirectAttributes.addFlashAttribute("msg", "setError('Please tick at least one invoice before submission')");
            }
        else
            {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
            }        
        return "redirect:/bill/institutionreceipt";
    }  
     
    @RequestMapping(value = "/bill/institutionreceiptcancel/{id}", method = RequestMethod.GET)
    public String institutionReceiptCancel( @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, @ModelAttribute InstitutionReceipt institutionReceipt) {
        institutionReceipt = institutionReceipt == null? new InstitutionReceipt():institutionReceipt;
        if(institutionReceipt.getInstitution() == null)
           {
           institutionReceipt.setInstitution(new Institution());
           institutionReceipt.getInstitution().setId(0L);
           }
         String msg = institutionReceiptService.cancelInstitutionReceipt(id);
         model.addAttribute("institutions", institutionService.getActive());
         model.addAttribute("ahfozs", unitService.getActiveAhfoz());
         redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
         return "redirect:/bill/institutionreceipt";
    }   
    
}
