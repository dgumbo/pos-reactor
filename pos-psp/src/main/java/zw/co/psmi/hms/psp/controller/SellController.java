/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.psmi.hms.psp.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.psmi.hms.authentication.entity.Location;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.entity.UserLogin;
import zw.co.psmi.hms.authentication.service.UnitService;
import zw.co.psmi.hms.authentication.service.UserService;
import zw.co.psmi.hms.bill.dao.DepositDao;
import zw.co.psmi.hms.bill.entity.Bill;
import zw.co.psmi.hms.bill.entity.BillCategory;
import zw.co.psmi.hms.patient.entity.Cypher;
import zw.co.psmi.hms.patient.entity.CypherType;
import zw.co.psmi.hms.patient.entity.InstitutionType;
import zw.co.psmi.hms.patient.entity.Patient;
import zw.co.psmi.hms.patient.entity.PaymentFor;
import zw.co.psmi.hms.patient.entity.Prescription;
import zw.co.psmi.hms.patient.entity.PrescriptionItem;
import zw.co.psmi.hms.patient.entity.TransactionStatus;
import zw.co.psmi.hms.bill.service.BillService;
import zw.co.psmi.hms.emr.doctor.service.api.DoctorEmrService;
import zw.co.psmi.hms.joborder.dao.JobOrderDao;
import zw.co.psmi.hms.joborder.entity.JobOrder;
import zw.co.psmi.hms.mongo.client.doctor.entity.DoctorEmr;
import zw.co.psmi.hms.patient.entity.ServiceClass;
import zw.co.psmi.hms.patient.service.CurrencyService;
import zw.co.psmi.hms.patient.service.CypherService;
import zw.co.psmi.hms.patient.service.InstitutionService;
import zw.co.psmi.hms.patient.service.PatientService;
import zw.co.psmi.hms.patient.service.PaymentTypeService;
import zw.co.psmi.hms.patient.service.PrescriptionService;
import zw.co.psmi.hms.patient.service.PriceService;
import zw.co.psmi.hms.patient.service.PrinterConfigService;
import zw.co.psmi.hms.patient.service.StockOutService;
import zw.co.psmi.hms.psp.entity.Quotation;
import zw.co.psmi.hms.psp.entity.Sell;
import zw.co.psmi.hms.psp.service.QuotationService;
import zw.co.psmi.hms.psp.service.SellService;
import zw.co.psmi.hms.stocks.entity.CurrentStock;
import zw.co.psmi.hms.stocks.entity.StockStatus;
import zw.co.psmi.hms.stocks.service.GenericStockTypeService;
import zw.co.psmi.hms.patient.service.ServiceService;
import zw.co.psmi.hms.psp.service.PspServiceChargeService;
import zw.co.psmi.hms.stocks.entity.StockType;

/**
 *
 * @author kinah
 */
@Controller
@Slf4j
public class SellController {

    @Autowired
    private SellService sellService;
    @Autowired
    private UserService userService;
    //Sell
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private PrinterConfigService printerConfigService;
    @Autowired
    private GenericStockTypeService genericStockTypeService;
    @Autowired
    private CypherService cypherService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private PspServiceChargeService pspServiceChargeService;
    @Autowired
    private JobOrderDao jobOrderDao;
    @Autowired
    private DepositDao depositDao;

    @RequestMapping(value = "/psp/sell", method = RequestMethod.GET)
    public String sell(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Sell sell) {
        sell.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        sell.setBillCategorys(Arrays.asList(BillCategory.values()));
        sell.setTransactionStatuss(Arrays.asList(TransactionStatus.values()));
        List<Sell> sells = this.sellService.getAllBySearch(sell);
        sells
                .stream()
                .forEach(line->{ 
                    line.setDepositAvailable(
                            line.getBill()!=null&&
                                    line.getBill().getPatient()!=null&&
                                    depositDao.countByPatientIdAndUnitIdAndBillTypeAndTransactionStatus(
                            line.getBill().getPatient().getId(), 
                            line.getUnit().getId(), 
                            line.getBill().getBillType(), 
                            TransactionStatus.CREATED)>0
                    );
        });
        model.addAttribute("sells", sells);
        return "psp/sell";
    }

    @RequestMapping(value = "/psp/sellaction/{id}", method = RequestMethod.GET)
    public String sellAction(@AuthenticationPrincipal UserLogin userLogin,RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber, @RequestParam(value = "patientId", required = false) Long patientId,  @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {
        return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, id > 0l, "");
    }

    @RequestMapping(value = "/psp/sellaction/{id}", method = RequestMethod.POST)
    public String sellActionAuth(@AuthenticationPrincipal UserLogin userLogin,RedirectAttributes redirectAttributes,  @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "patientId", required = false) Long patientId, 
             @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {
        if (id == 0l) {
            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, id == 0l, "");
        }
        if (userService.authenticate(username, password)) {
            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, false, "Edit Mode");
        }
        return sellActionLocal(userLogin, redirectAttributes,id, prescriptionId, quotationNumber, patientId, jobOrderId, "", model, true, "Authentication failed");
    }

    @RequestMapping(value = "/psp/sellcancel/{id}", method = RequestMethod.POST)
    public String sellActionCancel(@AuthenticationPrincipal UserLogin userLogin, RedirectAttributes redirectAttributes, @PathVariable("id") long id,
            @RequestParam(value = "prescriptionId", required = false) Long prescriptionId,
            @RequestParam(value = "quotationNumber", required = false) String quotationNumber,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "patientId", required = false) Long patientId, 
            @RequestParam(value = "reason", required = false) String reason, 
            @RequestParam(value = "jobOrderId", required = false) Long jobOrderId, Model model) {
        if (id == 0l) {
            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, id == 0l, "");
        }
        if (userService.authenticate(username, password)) {
            log.info("Login Authorized");
            return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, false, "Cancel Mode");
        }
        log.info("Login failed");
        return sellActionLocal(userLogin,redirectAttributes, id, prescriptionId, quotationNumber, patientId, jobOrderId, reason, model, true, "Authentication failed");
    }

    private String sellActionLocal(UserLogin userLogin, RedirectAttributes redirectAttributes, long id, Long prescriptionId, String quotationNumber, Long patientId, Long jobOrderId, String reason, Model model, boolean sellReadOnly, String errorMsg) {

        Sell sell = this.sellService.getById(id);
        sell = sell == null ? new Sell() : sell;
        sell.setReadOnly(sellReadOnly);
        if (sell.getId() > 0l && sell.getTransactionStatus() != TransactionStatus.CANCELLED && "Cancel Mode".contentEquals(errorMsg)) 
            {
            sellService.cancelSell(sell, reason);
            sellReadOnly = false;
            sell.setReadOnly(sellReadOnly);
            redirectAttributes.addFlashAttribute("msg", "setMsg('This Transaction is cancelled')");
             return "redirect:/psp/sell";
            }
        if (sell.getPatient() != null && sell.getPatient().getId() > 0) 
            {
            sell.setPatientNumber(sell.getPatient().getNumber());
            }
        prescriptionId = prescriptionId == null ? null : prescriptionId;
        quotationNumber = quotationNumber == null ? null : quotationNumber;       
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        if(unit.getLob().getName().toLowerCase().contains("psp") || unit.getLob().getName().toLowerCase().contains("pharm"))
           {
           sell.setPspSell(true);
           }
        Location defaultSellLocation = unitService.getDefaultSellLocation(unit);
        model.addAttribute("location", defaultSellLocation);
        sell.setUnit(unit);
        sell.setLocation(defaultSellLocation);
        sell.setBillCategorys(Arrays.asList(BillCategory.values()));
        sell.setQuotationNumber(quotationNumber);
        sell.setSpecialContracts(institutionService.getActive(InstitutionType.SPECIAL_CONTRACT));
        sell.setCurrencys(currencyService.getActive()); //pspServiceChargeService
        sell.setPaymentTypes(paymentTypeService.getActive());
        sell.setPaymentFors(Arrays.asList(PaymentFor.values()));
        if (patientId != null && patientId > 0L) {

            Patient patient = patientService.getById(patientId);
            sell.setPatientNumber(patient.getNumber());
            sell.setPatient(patient);
            sell.setPatientInsurances(patientService.getPatientInsuranceByIdActive(patient.getId()));
        }
        else if(sell.getId() > 0 && sell.getBill() != null && sell.getBill().getPatient() != null) 
           {
            sell.setPatient(sell.getBill().getPatient());
           }
        else
          {
          sell.setPatient(null);
          }
        if (id == 0) {
            sell.setBill(new Bill());
            if (prescriptionId != null) {
                Prescription prescription = prescriptionService.getById(prescriptionId);
                sell.setPrescriptionNumber(prescription.getPrescriptionNumber());
                List<PrescriptionItem> prescriptionItems = prescriptionService.getByPrescriptionIdActiveAndStatus(prescriptionId);
                 sell.setListItems(new ArrayList<Object>(prescriptionItems));
            } else {
                sell.setBillCategory(BillCategory.CASH);

            }
            if (quotationNumber != null) {
                Quotation quotation = quotationService.getByNumber(quotationNumber);
                sell.setBillCategory(quotation.getBillCategory());
                sell.setDoctor(quotation.getDoctor());
                if (quotation.getPatient() != null) {
                    sell.setPatient(quotation.getPatient());
                    sell.setPatientNumber(sell.getPatient().getNumber());
                }
                sell.setListItems(new ArrayList<>(quotationService.getQuotationItemByIdActive(quotation.getId())));
            }
        } else {
            sell.setSellItem(sellService.getBySellId(id));
            sell.setStockOut(stockOutService.getBySellId(id));

            if (sell.getPrescriptionNumber() != null) {

                sell.setListItems(new ArrayList<>(sellService.getBySellId(id)));
            }
            //Long billId = sell.getBill().getId(); //bill might be null
            //Receipt receipt = billService.getReceiptByBillId(billId);
            //receipt = receipt == null ? new Receipt(): receipt;
            //sell.setReceiptItem(billService.getItemByReceiptId(receipt.getId()));
        }
        if (sell.getPrescriptionNumber() == null || sell.getPrescriptionNumber().length() < 5) {
            sell.setPrescriptionNumber(null);
        }
        if (sell.getPatient() != null && sell.getPatient().getId() > 0) {
            sell.setPatientInsurances(patientService.getPatientInsuranceByIdActive(sell.getPatient().getId()));
        }
        sell.setTableSize((sell.getSellItem() == null? 1:  ( sell.getSellItem().size()) + 2));
        sell.setTableSize1((sell.getStockOut() == null? 1:  ( sell.getStockOut().size()) + 2));
        model.addAttribute("sell", sell);
        if(!errorMsg.isEmpty())
           {
           model.addAttribute("msg", "setError('"+errorMsg+"')");
           }
        model.addAttribute("cyphers", cypherService.replaceCyphers("", CypherType.DOSAGE));
        model.addAttribute("printers", printerConfigService.getPrinterList());
        return "psp/sellaction";
    }

    @RequestMapping(value = "/psp/sellform", method = RequestMethod.POST)
    public String sellform(@AuthenticationPrincipal UserLogin userLogin, @ModelAttribute Sell sell, Model model, RedirectAttributes redirectAttributes) {
        Long availablePrinters = sell.getId();
        sell.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        sell.setUsername(userLogin.getUsername());
        sell.setRoleId(userLogin.getRoleID());
//        for (SellItem sellItem : sell.getSellItem()) {
//            System.out.print(sellItem.getQuantity() + ".......................");
//        }  
        boolean quotation = false;
        if(sell.getQuotation())
              {
               quotation = true;
              }
        sell.setSellDate(new Date());
        sell.setSellDateTime(new Date());
        String msg = this.sellService.save(sell, sell.getSellItem());
        if(availablePrinters == null || availablePrinters == 0)
           {
            if(quotation)
               {
               redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "');setTimeout(function () {window.scrollTo(0, 0);instantQuotation(\""+sell.getQuotationNumber()+"\");}, 5000);");
               }
            else
              {
              redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "');setTimeout(function () {window.scrollTo(0, 0);instantSell(\""+sell.getId()+"\");}, 5000);");
              }
           }
        else
           {
           redirectAttributes.addFlashAttribute("msg", "setMsg('" + msg + "');");
           }
        model.addAttribute("sell", sell);
        if(sell.getReadOnly() != null && sell.getReadOnly())
          {
          return "redirect:/psp/sell/";
          }
        return "redirect:/psp/sellaction/" + sell.getId();
    }

    @RequestMapping(value = "/psp/stocktypesearchlist", method = RequestMethod.GET)
    @ResponseBody
    public List<CurrentStock> findAllStockType(@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "institution", required = true) Long institution,
            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
             @RequestParam(value = "patientNumber", required = true) String patientNumber) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        institution = institution == null?0:institution;
        List<CurrentStock> currentStocks = priceService.findByNameSearch(name, id, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
        return currentStocks;
    }
    @RequestMapping(value = "/psp/stocktypesearchlistwithcost", method = RequestMethod.GET)
    @ResponseBody
    public List<StockType> stocktypesearchlistunavailable(@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "name", required = true) String name) {   
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        List<StockType> stockTypes = priceService.findByStockTypesWithCost(name, unit.getId(), unit.getLob().getId());
        return stockTypes;
    }

    @RequestMapping(value = "/psp/stocktypeidlist", method = RequestMethod.GET)
    @ResponseBody
    public List<CurrentStock> findAllStockTypeId(@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "count", required = true) int count,
            @RequestParam(value = "stockTypeid", required = true) Long stockTypeid,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "institution", required = true) Long institution,
            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
             @RequestParam(value = "patientNumber", required = true) String patientNumber) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        institution = institution == null? 0:institution;
        List<CurrentStock> currentStocks = priceService.findByIdSearch(stockTypeid, id, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
        return currentStocks;
    }

    @RequestMapping(value = "/psp/genericstocktypesearchlist", method = RequestMethod.GET)
    @ResponseBody
    public List<StockType> findAllGenericStockType(@RequestParam(value = "name", required = true) String name) {
        List<StockType> stockTypes = genericStockTypeService.findStockTypeByGenericStockTypeNameSearch(name);
        return stockTypes;
    }

    @RequestMapping(value = "/psp/historysellwindow", method = RequestMethod.GET)
    public String findHistorySell(Model model, @RequestParam(value = "id", required = true) Long id) {
        List<Sell> sells = sellService.getByPatientId(id);
         model.addAttribute("sells", sells); 
        return "psp/sellhistory";
    }

    @RequestMapping(value = "/psp/sellitemhistorylist", method = RequestMethod.GET)
    @ResponseBody
    public String findSellItemHistoryList(@AuthenticationPrincipal UserLogin userLogin, @RequestParam(value = "id", required = true) Long id, @RequestParam(value = "institution", required = true) Long institution, @RequestParam(value = "billCategory", required = true) BillCategory billCategory) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        return this.sellService.getSellItemHistoryList(id, unit.getId(), unit.getLob().getId(), billCategory, institution);
    }

    @RequestMapping(value = "/psp/stockitemfrombarcode", method = RequestMethod.GET)
    @ResponseBody
    public String findSellItemsList(@AuthenticationPrincipal UserLogin userLogin, @RequestParam(value = "location", required = true) Long location, 
            @RequestParam(value = "barcode", required = true) String barcode, 
            @RequestParam(value = "institution", required = true) Long institution, 
            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
             @RequestParam(value = "patientNumber", required = true) String patientNumber) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        CurrentStock currentStock = priceService.findByBarcode(barcode, location, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
        return currentStock.getStockType().getId() + "****" + currentStock.getStockType().getName() + "****" + currentStock.getQuantity() + "****" + currentStock.getUnitCost() + "****" + currentStock.isCashOnly() + "****" + currentStock.getStockType().getId() + "****" + currentStock.getAuthorisationAmount() + "****" + currentStock.getTariffCode() + "****" + currentStock.getDispensingFee();
    }

    @RequestMapping(value = "/psp/genericdrugidlist", method = RequestMethod.GET)
    @ResponseBody
    public List<CurrentStock> findAllGenericDrugId(@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "count", required = true) int count,
            @RequestParam(value = "genericDrug", required = true) Long genericDrug,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "institution", required = true) Long institution,
            @RequestParam(value = "billCategory", required = true) BillCategory billCategory,
             @RequestParam(value = "patientNumber", required = true) String patientNumber) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        List<CurrentStock> currentStocks = priceService.findByGenericDrugSearch(genericDrug, id, unit.getId(), unit.getLob().getId(), billCategory, institution, patientNumber);
        return currentStocks;
    }

    @RequestMapping(value = "/psp/getoptionsoncypherlist", method = RequestMethod.GET)
    @ResponseBody
    public List<Cypher> getoptionsoncypherlist(@AuthenticationPrincipal UserLogin userLogin,
            @RequestParam(value = "text", required = true) String text) {
        return cypherService.findCypherForReplace(text, CypherType.DOSAGE);
    }

    @RequestMapping(value = "/psp/repeatwindow", method = RequestMethod.GET)
    @ResponseBody
    public String repeatwindow(@RequestParam(value = "patientNumber", required = true) String patientNumber) {
        return this.sellService.getRepeats(patientNumber);
    }
    @RequestMapping(value = "/psp/sellitemrepeatlist", method = RequestMethod.GET)
    @ResponseBody
    public String sellitemrepeatlist(@AuthenticationPrincipal UserLogin userLogin, @RequestParam(value = "id", required = true) Long id, @RequestParam(value = "institution", required = true) Long institution, @RequestParam(value = "billCategory", required = true) BillCategory billCategory) {
        Unit unit = unitService.getByUnitCode(userLogin.getUnitCode());
        return this.sellService.getSellItemRepeatList(id, unit.getId(), unit.getLob().getId(), billCategory, institution);
    }
    @RequestMapping(value = "/psp/receiptworkflow", method = RequestMethod.GET)
    public String selllist(@AuthenticationPrincipal UserLogin userLogin, Model model, @ModelAttribute Sell sell) {
        sell.setUnit(unitService.getByUnitCode(userLogin.getUnitCode()));
        sell.setBillCategorys(Arrays.asList(BillCategory.values()));
        List<Sell> sells = this.sellService.getAllBySearch(sell);
        sells = sells.stream().filter(line->line.getBill().getCashOwing().doubleValue() > 0.0 || line.getBill().getShortfallOwing().doubleValue() > 0.0).collect(Collectors.toList());
        model.addAttribute("sells", sells);
        return "psp/receiptworkflow";
    }
}
