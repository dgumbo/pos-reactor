/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.psp.controller;

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
import zw.co.psmi.hms.psp.entity.ReturnedDrug;
import zw.co.psmi.hms.psp.service.ReturnedDrugService;

/**
 *
 * @author kinah
 */
@Controller
public class ReturnedDrugController {
     @Autowired
    private ReturnedDrugService returnedDrugService;
    @Autowired
    private UnitService unitService;

    @RequestMapping(value = "/psp/returneddrugaction/{Id}", method = RequestMethod.GET)
    public String returnedDrugAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {

        ReturnedDrug returnedDrug = this.returnedDrugService.getById(Id);
        model.addAttribute("returnedDrug", returnedDrug);
        return "psp/returneddrugaction";
    }
    
    @RequestMapping(value = "/psp/returneddrugform", method = RequestMethod.POST)
    public String returnedDrugform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute ReturnedDrug returnedDrug, Model model, RedirectAttributes redirectAttributes) {
        returnedDrug.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        returnedDrug.setUsername(userLogin.getUsername());
        String msg = this.returnedDrugService.save(returnedDrug);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/psp/returneddrugaction/0";
    }
}
