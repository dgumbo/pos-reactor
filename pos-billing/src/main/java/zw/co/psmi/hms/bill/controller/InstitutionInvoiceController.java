/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.InstitutionInvoice;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.bill.service.InstitutionInvoiceService;
import zw.co.psmi.hms.patient.entity.Institution;
import zw.co.psmi.hms.patient.entity.InstitutionType;
import zw.co.psmi.hms.patient.service.InstitutionService;

/**
 *
 * @author kinah
 */
@Controller
public class InstitutionInvoiceController {
    
    @Autowired
    private InstitutionInvoiceService institutionInvoiceService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private UnitService unitService;
    @RequestMapping(value = "/bill/institutioninvoice")
    public String institutionInvoice(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute InstitutionInvoice institutionInvoice) {
        institutionInvoice = institutionInvoice == null? new InstitutionInvoice():institutionInvoice;
        if(institutionInvoice.getInstitution() == null)
           {
           institutionInvoice.setInstitution(new Institution());
           institutionInvoice.getInstitution().setId(0L);
           } 
        model.addAttribute("institutions", institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
        model.addAttribute("ahfozs", unitService.getActiveAhfoz());
        model.addAttribute("institutionInvoices", this.institutionInvoiceService.getAllBySearch(institutionInvoice));
        return "bill/institutioninvoice";
    }
    @RequestMapping(value = "/bill/institutioninvoiceaction/{id}", method = RequestMethod.GET)
    public String institutionInvoiceAction( @PathVariable("id") Long id, Model model) {
         
        InstitutionInvoice institutionInvoice = this.institutionInvoiceService.getByID(id);
        if(id > 0)
           {
           institutionInvoice.setInstitutionInvoiceItems(institutionInvoiceService.getByInstitutionInvoiceId(id));
           model.addAttribute("title", "View Invoice to Institution");
           }
        else {
            model.addAttribute("title", "Create New Invoice to Institution");
        }
        model.addAttribute("institutions", institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
         model.addAttribute("ahfozs", unitService.getActiveAhfoz());
        model.addAttribute("institutionInvoice", institutionInvoice);
        return "bill/institutioninvoiceaction";
    }
     
    @RequestMapping(value = "/bill/institutioninvoiceform", method = RequestMethod.POST)
    public String institutionInvoiceform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute InstitutionInvoice institutionInvoice, Model model, RedirectAttributes redirectAttributes) {
        institutionInvoice.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        String msg = this.institutionInvoiceService.save(institutionInvoice);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/bill/institutioninvoice";
    }  
     @RequestMapping(value = "/bill/getallinstitutioninvoicesbysearch", method = RequestMethod.GET)
    public String getallbillsbysearch(Model model,@RequestParam(value = "startDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam(value = "endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam(value = "institution") Long institution, @RequestParam(value = "ahfoz") String ahfoz) {
     InstitutionInvoice institutionInvoice1 = new InstitutionInvoice();
     institutionInvoice1.setStartDate(startDate);
     institutionInvoice1.setEndDate(endDate);
     institutionInvoice1.setInstitution(institutionService.getById(institution));
     institutionInvoice1.setAhfoz(ahfoz);
      model.addAttribute("institutionInvoices", institutionInvoiceService.getAllBySearchForInvoice(institutionInvoice1));
        return "bill/getallinstitutioninvoicesbysearch";
    } 
   
    @RequestMapping(value = "/bill/institutioninvoicelist")
    public String institutioninvoicelist(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute InstitutionInvoice institutionInvoice) {
        institutionInvoice = institutionInvoice == null? new InstitutionInvoice():institutionInvoice;
        if(institutionInvoice.getInstitution() == null)
           {
           institutionInvoice.setInstitution(new Institution());
           institutionInvoice.getInstitution().setId(0L);
           } 
        model.addAttribute("institutionInvoices", this.institutionInvoiceService.getPendingBySearch(institutionInvoice));
        return "bill/institutioninvoicelist";
    } 
    
    
    
    
}
