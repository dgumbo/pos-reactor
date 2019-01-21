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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.patient.entity.HmsService;
import zw.co.psmi.hms.patient.service.ServiceService;
import zw.co.psmi.hms.psp.entity.PspServiceCharge;
import zw.co.psmi.hms.psp.service.PspServiceChargeService;

/**
 *
 * @author kinah
 */
@Controller
public class PspServiceChargeController {
     @Autowired
    private PspServiceChargeService pspServiceChargeService;
     @Autowired
     private UnitService unitService;
       @Autowired
     private ServiceService serviceService;
    //PspServiceCharge

    @RequestMapping(value = "/psp/pspservicecharge", method = RequestMethod.GET)
    public String pspServiceCharge(@AuthenticationPrincipal UserLogin userLogin, Model model) {

        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        model.addAttribute("pspServiceCharges", this.pspServiceChargeService.getAll(unit.getId()));
        return "psp/pspservicecharge";
    }

    @RequestMapping(value = "/psp/pspservicechargeaction/{Id}", method = RequestMethod.GET)
    public String pspServiceChargeAction(@AuthenticationPrincipal UserLogin userLogin, @PathVariable("Id") Long Id, Model model) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        PspServiceCharge pspServiceCharge = this.pspServiceChargeService.getById(Id);
        pspServiceCharge.setHmsServices(serviceService.findByLobId(unit.getLob().getId()));
        model.addAttribute("pspServiceCharge", pspServiceCharge);
        return "psp/pspservicechargeaction";
    }

    @RequestMapping(value = "/psp/pspservicechargeform", method = RequestMethod.POST)
    public String pspServiceChargeform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute PspServiceCharge pspServiceCharge, Model model, RedirectAttributes redirectAttributes) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        pspServiceCharge.setUnit(unit);
        String msg = this.pspServiceChargeService.save(pspServiceCharge);
        redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        return "redirect:/psp/pspservicecharge";
    }
      @RequestMapping(value = "/psp/pspservicechargewindow", method = RequestMethod.GET)
    @ResponseBody
    public String findAllGenericStockType(@AuthenticationPrincipal UserLogin userLogin) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        return pspServiceChargeService.getPspServiceChargeWindow(unit.getLob().getId());
    }
}
