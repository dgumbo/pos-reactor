/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.hisolutions.pos.client.printer.rest;

import lombok.extern.slf4j.Slf4j;
import zw.co.hisolutions.pos.client.printer.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.client.printer.model.PrintMsg;

/**
 *
 * @author com4t
 */
@RestController
@RequestMapping("/resource")
@CrossOrigin
@Slf4j
public class RestResource {
    private final PrintService printService;

    public RestResource(PrintService printService) {
        this.printService = printService;
    }

    @PostMapping(value = "/request", consumes = {MediaType.APPLICATION_JSON_VALUE},
             produces = {MediaType.APPLICATION_JSON_VALUE})
    public PrintMsg processRequest(@RequestBody PrintMsg msg) {
        return printService.processRequest(msg);
    }
}
