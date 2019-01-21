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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.patient.entity.ServiceClass;
import zw.co.psmi.hms.psp.entity.Quotation;
import zw.co.psmi.hms.psp.service.PspServiceChargeService;

/**
 *
 * @author kinah
 */
@Controller
public class DrugPreparationController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private PspServiceChargeService pspServiceChargeService;
    
    @RequestMapping(value = "/psp/drugpreparationwindow", method = RequestMethod.GET)
    public String drugpreparationwindow(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Quotation quotation) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        model.addAttribute("drugPreparations", pspServiceChargeService.getActiveAndServiceClassAndUnit(ServiceClass.DRUG_PREPARATION, unit.getId()));
        return "psp/drugpreparation";
    }
}
