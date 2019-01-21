package zw.co.hisolutions.pos.dayend.api_controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.hisolutions.pos.dayend.entity.DayEnd;
import zw.co.hisolutions.pos.dayend.service.DayEndService;

/**
 *
 * @author kinah
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
@RequestMapping(value = "/api/dayend")
public class DayEndController {

    @Autowired
    private DayEndService dayEndService;
 
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})    
    public ResponseEntity<List<DayEnd>> dayEndList( ) {  
        return new ResponseEntity<>(dayEndService.findAll(), HttpStatus.OK);
    }
  
    @RequestMapping(value = "/sell")
    public List<DayEnd> dayEnd(@ModelAttribute DayEnd dayEnd) {
        dayEnd = dayEnd == null ? new DayEnd() : dayEnd;
        
        List<DayEnd> dayEnds = this.dayEndService.getAllBySearch(dayEnd);
        return dayEnds;
    }

//    @RequestMapping(value = "/dayendaction/{id}", method = RequestMethod.GET)
//    public String dayEndAction(@PathVariable("id") Long id) {
//
//        DayEnd dayEnd = this.dayEndService.getByID(id);
//        if (id > 0) {
//            dayEnd.setDayEndItems(dayEndService.getByDayEndId(id));
//            model.addAttribute("title", "View Day End");
//        } else {
//            model.addAttribute("title", "Create New Day End");
//        }
//        
//        model.addAttribute("paymentTypes", paymentTypeService.getActive());
//        model.addAttribute("users", userService.findByUnitCode(userLogin.getUnitCode()));
//        model.addAttribute("currencys", currencyService.getActive());
//        model.addAttribute("dayEnd", dayEnd);
//        return "bill/dayendaction";
//    }

    @RequestMapping(value = "/dayendform", method = RequestMethod.POST)
    public String dayEndform(@ModelAttribute DayEnd dayEnd, Model model, RedirectAttributes redirectAttributes) {
        DayEnd msg = this.dayEndService.save(dayEnd);
        if (msg == null ) {
            redirectAttributes.addFlashAttribute("msg", "setError('Transaction not saved')");
        } else {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "')");
        }
        return "redirect:/bill/dayend";
    }

}
