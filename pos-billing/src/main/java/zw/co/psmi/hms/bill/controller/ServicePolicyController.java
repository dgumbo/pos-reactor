/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.service.DivisionService;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.bill.entity.ServicePolicy;
import zw.co.psmi.hms.bill.entity.ServicePolicyType;
import zw.co.psmi.hms.bill.service.ServicePolicyService;
import zw.co.psmi.hms.patient.entity.HmsService;

/**
 *
 * @author kinah
 */
@Controller
public class ServicePolicyController {
    @Autowired
    private DivisionService divisionService;
        @Autowired
    private ServicePolicyService servicePolicyService;
    @RequestMapping(value = "/bill/servicepolicyaction", method = {RequestMethod.GET})
    public String priceGetAction(Model model, @RequestParam(value = "lob", required = false) Long lob, @RequestParam(value = "unit", required = false) Long unit, @RequestParam(value = "billCategory", required = false) BillCategory billCategory, @RequestParam(value = "institution", required = false) Long institution
      ) {
     ServicePolicy servicePolicy = new ServicePolicy();    
     servicePolicy.setLobs(divisionService.getLobActive());
     servicePolicy.setBillCategorys(Arrays.asList(BillCategory.values()));
     servicePolicy.setServicePolicyTypes(Arrays.asList(ServicePolicyType.values()));
      servicePolicy.setServicePolicys(servicePolicyService.getAll());      
      model.addAttribute("servicePolicy", servicePolicy); 
     
      return "bill/servicepolicyaction";
    }

    @RequestMapping(value = "/bill/servicepolicyaction", method = RequestMethod.POST)
    public String pricePostAction(Model model, @RequestParam(value = "lob", required = false) Long lob, @RequestParam(value = "unit", required = false) Long unit, @RequestParam(value = "billCategory", required = false) BillCategory billCategory, @RequestParam(value = "institution", required = false) Long institution
      ) {        
     ServicePolicy servicePolicy = new ServicePolicy();   
     servicePolicy.setLobs(divisionService.getLobActive());
     servicePolicy.setLob(divisionService.getLobByLobId(lob));
     servicePolicy.setBillCategorys(Arrays.asList(BillCategory.values())); 
     servicePolicy.setBillCategory(billCategory);
     servicePolicy.setServicePolicyTypes(Arrays.asList(ServicePolicyType.values()));
     servicePolicy.setServicePolicys(servicePolicyService.getBySearch(lob, billCategory, unit, institution));      
      
      model.addAttribute("servicePolicy", servicePolicy); 
      return "bill/servicepolicyaction";
    }
    @RequestMapping(value = "/bill/servicepolicyform", method = RequestMethod.POST)
    public String priceform(@ModelAttribute ServicePolicy servicePolicy, Model model, RedirectAttributes redirectAttributes
   ) {
        String msg = this.servicePolicyService.save(servicePolicy);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
//        return priceAction(price.getName(), model, price.getLob().getId(), 
//                price.getUnit()==null?null:price.getUnit().getId(), price.getBillCategory(), 
//                price.getInstitution()==null?null:price.getInstitution().getId(), 
//                pageSize, page);
        return "redirect:/bill/servicepolicyaction";
    }
    
    @RequestMapping(value = "/bill/servicepolicygetby", method = RequestMethod.GET)
    public String servicepolicygetby( Model model,@RequestParam(value = "unit", required = false) Long unit, 
            @RequestParam(value = "billCategory", required = false) BillCategory billCategory, @RequestParam(value = "institution", required = false) Long institution
    , @RequestParam(value = "patientNumber", required = false) String patientNumber, @RequestParam(value = "date", required = false) String date) 
      {
        HmsService service = servicePolicyService.getByProcess(billCategory, unit, institution, patientNumber);        
        model.addAttribute("service", service);
        model.addAttribute("date", date);
        return "bill/servicepolicygetby";
    }  
}
