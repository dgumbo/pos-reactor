package zw.co.hisolutions.pos.sell.api_controller;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.hisolutions.pos.backend.entity.CartHolder;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.sell.entity.Sell;
import zw.co.hisolutions.pos.sell.entity.TransactionStatus;
import zw.co.hisolutions.pos.sell.services.SellService;

/**
 *
 * @author dgumbo
 */
@Controller
@Slf4j
@RequestMapping("/rest/sell")
public class SellController {

    @Autowired
    private SellService sellService;

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<List<Sell>> getAllSells() throws Exception {
        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            List<Sell> entity = sellService.findAll();
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Sell> getSell(@PathVariable("id") long id) throws Exception {

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        Sell entity = null;
        try {
            entity = sellService.find(id);
            System.out.println("entity");
            System.out.println(entity);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @PutMapping(value = "/sell", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<Sell> sell(@RequestBody CartHolder cartHolder) throws Exception {
        System.out.println("\n" + cartHolder.getClass().getName() + "  B4 Save : " + cartHolder + "\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try {
            Sell entity = sellService.saveCart(cartHolder);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/sellaction/{id}", method = RequestMethod.GET)
    public String sellAction(RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {

        return sellActionLocal(redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, id > 0l, "");
    }

    @RequestMapping(value = "/sellaction/{id}", method = RequestMethod.POST)
    public String sellActionAuth(RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {
        if (id == 0l) {
            return sellActionLocal(redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, id == 0l, "");
        }
//        if (userService.authenticate(username, password)) {
//            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, false, "Edit Mode");
//        }
        return sellActionLocal(redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, true, "Authentication failed");
    }

    @RequestMapping(value = "/sellcancel/{id}", method = RequestMethod.POST)
    public String sellActionCancel(RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "reason", required = false) String reason,
            @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {
        if (id == 0l) {
            return sellActionLocal(redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, id == 0l, "");
        }
//        if (userService.authenticate(username, password)) {
//            log.info("Login Authorized");
//            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, false, "Cancel Mode");
//        }
        log.info("Login failed");
        return sellActionLocal(redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, true, "Authentication failed");
    }

    private String sellActionLocal(RedirectAttributes redirectAttributes, long id, Long prescriptionId, String quotationNumber, Long patientId, Long jobOrderId, String reason, Model model, boolean sellReadOnly, String errorMsg) {

        Sell sell = this.sellService.find(id);
        sell = sell == null ? new Sell() : sell;

        if (sell.getId() > 0l && sell.getTransactionStatus() != TransactionStatus.CANCELLED && "Cancel Mode".contentEquals(errorMsg)) {
            sellService.cancelSell(sell, reason);
            sellReadOnly = false;
            redirectAttributes.addFlashAttribute("msg", "setMsg('This Transaction is cancelled')");
            return "redirect:/sell";
        }

//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        sell.setUnit(unit);
        if (id == 0) {
        } else {
            sell.setSellItems(sellService.getBySellId(id));
//            sell.setStockOut(stockOutService.getBySellId(id)); 
        }

        model.addAttribute("sell", sell);
        if (!errorMsg.isEmpty()) {
            model.addAttribute("msg", "setError('" + errorMsg + "')");
        }
//        model.addAttribute("cyphers", cypherService.replaceCyphers("", CypherType.DOSAGE));
//        model.addAttribute("printers", printerConfigService.getPrinterList());
        return "psp/sellaction";
    }

    @RequestMapping(value = "/sellform", method = RequestMethod.POST)
    public String sellform(@ModelAttribute Sell sell, Model model, RedirectAttributes redirectAttributes) {
        Long availablePrinters = sell.getId();
        boolean quotation = false;

        sell.setSellDate(new Date());
        sell.setSellDateTime(new Date());

        String msg = "";
        this.sellService.save(sell);

        if (availablePrinters == null || availablePrinters == 0) {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "');setTimeout(function () {window.scrollTo(0, 0);instantSell(\"" + sell.getId() + "\");}, 5000);");
        } else {
            redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "');");
        }
        model.addAttribute("sell", sell);

        return "redirect:/sellaction/" + sell.getId();
    }

//    @RequestMapping(value = "/stocktypesearchlist", method = RequestMethod.GET)
//    @ResponseBody
//    public List<CurrentStock> findAllStockType(
//            @RequestParam(value = "name", required = true) String name,
//            @RequestParam(value = "id", required = true) Long id,
//            @RequestParam(value = "institution", required = true) Long institution,
//            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
//            @RequestParam(value = "patientNumber", required = true) String patientNumber) {
//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        institution = institution == null ? 0 : institution;
//        List<CurrentStock> currentStocks = priceService.findByNameSearch(name, id, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
//        return currentStocks;
//    }
//
//    @RequestMapping(value = "/stocktypesearchlistwithcost", method = RequestMethod.GET)
//    @ResponseBody
//    public List<StockType> stocktypesearchlistunavailable(
//            @RequestParam(value = "name", required = true) String name) {
//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        List<StockType> stockTypes = priceService.findByStockTypesWithCost(name, unit.getId(), unit.getLob().getId());
//        return stockTypes;
//    }
//
//    @RequestMapping(value = "/stocktypeidlist", method = RequestMethod.GET)
//    @ResponseBody
//    public List<CurrentStock> findAllStockTypeId(
//            @RequestParam(value = "count", required = true) int count,
//            @RequestParam(value = "stockTypeid", required = true) Long stockTypeid,
//            @RequestParam(value = "id", required = true) Long id,
//            @RequestParam(value = "institution", required = true) Long institution,
//            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
//            @RequestParam(value = "patientNumber", required = true) String patientNumber) {
//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        institution = institution == null ? 0 : institution;
//        List<CurrentStock> currentStocks = priceService.findByIdSearch(stockTypeid, id, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
//        return currentStocks;
//    }
//
//    @RequestMapping(value = "/genericstocktypesearchlist", method = RequestMethod.GET)
//    @ResponseBody
//    public List<StockType> findAllGenericStockType(@RequestParam(value = "name", required = true) String name) {
//        List<StockType> stockTypes = genericStockTypeService.findStockTypeByGenericStockTypeNameSearch(name);
//        return stockTypes;
//    }
//
//    @RequestMapping(value = "/historysellwindow", method = RequestMethod.GET)
//    public String findHistorySell(Model model, @RequestParam(value = "id", required = true) Long id) {
//        List<Sell> sells = sellService.getByPatientId(id);
//        model.addAttribute("sells", sells);
//        return "psp/sellhistory";
//    }
//
//    @RequestMapping(value = "/sellitemhistorylist", method = RequestMethod.GET)
//    @ResponseBody
//    public String findSellItemHistoryList(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "institution", required = true) Long institution, @RequestParam(value = "billCategory", required = true) BillCategory billCategory) {
//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        return this.sellService.getSellItemHistoryList(id, unit.getId(), unit.getLob().getId(), billCategory, institution);
//    }
//
//    @RequestMapping(value = "/stockitemfrombarcode", method = RequestMethod.GET)
//    @ResponseBody
//    public String findSellItemsList(@RequestParam(value = "location", required = true) Long location,
//            @RequestParam(value = "barcode", required = true) String barcode,
//            @RequestParam(value = "institution", required = true) Long institution,
//            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
//            @RequestParam(value = "patientNumber", required = true) String patientNumber) {
//        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
//        CurrentStock currentStock = priceService.findByBarcode(barcode, location, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
//        return currentStock.getProduct().getId() + "****" + currentStock.getStockType().getName() + "****" + currentStock.getQuantity() + "****" + currentStock.getUnitCost() + "****" + currentStock.isCashOnly() + "****" + currentStock.getStockType().getId() + "****" + currentStock.getAuthorisationAmount() + "****" + currentStock.getTariffCode() + "****" + currentStock.getDispensingFee();
//    } 
    @RequestMapping(value = "/receiptworkflow", method = RequestMethod.GET)
    public String selllist(Model model, @ModelAttribute Sell sell) {
//        sell.setUnit(unitService.getByUnitCode(userLogin.getUnitCode())); 
        List<Sell> sells = this.sellService.getAllBySearch(sell);

        model.addAttribute("sells", sells);
        return "psp/receiptworkflow";
    }
}
