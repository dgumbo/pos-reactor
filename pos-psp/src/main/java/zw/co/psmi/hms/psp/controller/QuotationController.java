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
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.psp.entity.Quotation;
import zw.co.psmi.hms.psp.service.QuotationService;

/**
 *
 * @author kinah
 */
@Controller
public class QuotationController {
    @Autowired
    private UnitService unitService;
    @Autowired
    private QuotationService quotationService;
    @RequestMapping(value = "/psp/quotation", method = RequestMethod.GET)
    public String sell(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Quotation quotation) {
        quotation.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("quotations", this.quotationService.getAllBySearch(quotation));
        return "psp/quotation";
    }
}
