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
import zw.co.psmi.hms.authentication.service.UserService;
import zw.co.psmi.hms.bill.entity.DayEnd;
import zw.co.psmi.hms.bill.service.DayEndService;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;

/**
 *
 * @author kinah
 */
@Controller
public class DayEndController {
    
    @Autowired
    private DayEndService dayEndService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/bill/dayend")
    public String dayEnd(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute DayEnd dayEnd) {
        dayEnd = dayEnd == null? new DayEnd():dayEnd;
        dayEnd.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        model.addAttribute("dayEnds", this.dayEndService.getAllBySearch(dayEnd));
        return "bill/dayend";
    }
    @RequestMapping(value = "/bill/dayendaction/{id}", method = RequestMethod.GET)
    public String dayEndAction(@AuthenticationPrincipal UserLogin userLogin,  @PathVariable("id") Long id, Model model) {
         
        DayEnd dayEnd = this.dayEndService.getByID(id);
        if(id > 0)
           {
           dayEnd.setDayEndItems(dayEndService.getByDayEndId(id));
           model.addAttribute("title", "View Day End");
           } 
        else {
            model.addAttribute("title", "Create New Day End");
        }
         model.addAttribute("paymentTypes", paymentTypeService.getActive());
         model.addAttribute("users", userService.findByUnitCode(userLogin.getUnitCode()));
         model.addAttribute("currencys", currencyService.getActive());
        model.addAttribute("dayEnd", dayEnd);
        return "bill/dayendaction";
    }
     
    @RequestMapping(value = "/bill/dayendform", method = RequestMethod.POST)
    public String dayEndform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute DayEnd dayEnd, Model model, RedirectAttributes redirectAttributes) {
        dayEnd.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        String msg = this.dayEndService.save(dayEnd);
        if(msg == null || msg.isEmpty())
           {
            redirectAttributes.addFlashAttribute("msg", "setError('Transaction not saved')");
           }
        else
           {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
           }
        return "redirect:/bill/dayend";
    }  
    
}
