/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.bill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author kinah
 */
@Controller
public class DiagnosisController {
    @RequestMapping(value = "/bill/diagnosisform", method = RequestMethod.GET)
    public String diagnosisform() {
        return "bill/diagnosisform";
    }  
}
