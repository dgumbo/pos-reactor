package zw.co.hisolutions.pos.reports.controllers;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import zw.co.hisolutions.pos.common.enums.ContentDisposalType;
import zw.co.hisolutions.pos.common.util.Results;
import zw.co.hisolutions.pos.reports.entity.ColumnMetadata;
import zw.co.hisolutions.pos.reports.entity.ReportInformation;
import zw.co.hisolutions.pos.reports.service.MisService;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisColumn;
import zw.co.hisolutions.pos.reports.entity.ReportAnalysisSheet;
import zw.co.hisolutions.pos.reports.entity.ReportColumnAnalysisSector;
import zw.co.hisolutions.pos.reports.entity.ReportColumnNumberFormat;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;
import zw.co.hisolutions.pos.reports.service.ReportAnalysisService;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;
import zw.co.hisolutions.pos.reports.service.ReportDataService;
import zw.co.hisolutions.pos.sxssf.service.SXSSFReportService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final MisService misService;
    private final ReportConfigService reportConfigService;
    private final ReportAnalysisService reportAnalysisService;
    private final ReportDataService reportResultService;
    private final SXSSFReportService reportService;

    @Autowired
    public ReportsController(MisService misService, ReportConfigService reportConfigService, ReportAnalysisService reportAnalysisService, ReportDataService reportResultService, SXSSFReportService reportService) {
        this.misService = misService;
        this.reportConfigService = reportConfigService;
        this.reportAnalysisService = reportAnalysisService;
        this.reportResultService = reportResultService;
        this.reportService = reportService;
    }

    @GetMapping("/getReportColumns/{reportName}")
    public List<ColumnMetadata> getReportColumns(@PathVariable String reportName, @ModelAttribute ParameterHolder holder) {
        ReportConfig reportConfig = reportConfigService.getByName(reportName);
        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);
        return reportResultService.getReportColumnsProperties(reportConfig, holder, configParameters);
    }

    @GetMapping("/getReportList")
    public List<ReportConfig> getReportList() {
        return misService.getReportList();
    }

    @GetMapping(value = "/getReportConfig/{reportConfigId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<ReportConfig> getReportConfig(@PathVariable Long reportConfigId) {
        ReportConfig reportConfig = new ReportConfig();
        
       if( reportConfigId != null && reportConfigId != 0 ){
            reportConfig= reportConfigService.getByID(reportConfigId);
            
            List<ReportConfigParameter> reportConfigParams = reportConfigService.getParametersByReportConfig(reportConfig);
            reportConfig.setReportConfigParameters(reportConfigParams);
       }

//        System.out.println("\nreportConfig : ");
//        System.out.println(reportConfig);
//        System.out.println(reportConfig.getColumns());
//        System.out.println(reportConfig.getNativeQuery());
//        System.out.println(reportConfig.getReportConfigParameters());
//        System.out.println("\n\n");

        ResponseEntity responseEntity;
        HttpStatus httpStatus;
        try { 
            ReportConfig entity = getReportConfigProperties(reportConfig);
            httpStatus = HttpStatus.CREATED;
            responseEntity = new ResponseEntity<>(entity, httpStatus);
        } catch (Exception ex) {
            httpStatus = HttpStatus.NOT_IMPLEMENTED;
            responseEntity = new ResponseEntity<>(new Results(Results.DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return responseEntity;
    }

    @GetMapping("/getReportConfigByReportName/{reportName}")
    public ReportConfig getReportConfigByReportName(@PathVariable String reportName) {
        ReportConfig reportConfig = reportConfigService.getByName(reportName);
        return getReportConfigProperties(reportConfig);
    }

    private ReportConfig getReportConfigProperties(ReportConfig reportConfig) {
        List<ReportConfigParameter> reportParameters = reportConfigService.getParametersByReportConfig(reportConfig.getId());
        reportParameters.forEach(param -> {
            param.setReportConfig(null);
            if (param.getParameterType() == ReportParameterType.SELECT_STATEMENT) {
                List<String> selectOptions = reportConfigService.getReportConfigParameterOptions(param.getId());
                param.setDropDownSelectOptions(selectOptions);
            }
        });
        reportConfig.setReportConfigParameters(reportParameters);
        return reportConfig;
    }
    
    @PostMapping(value = "/updateReportConfig", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ReportConfig updateReportConfig(@RequestBody ReportConfig reportConfig) {
        System.out.println("zw.co.psmi.hms.mis.controllers.MisController.updateReportConfig()");
        System.out.println("reportConfig : " + reportConfig);
        System.out.println("reportConfig.getReportConfigParameters() : " + reportConfig.getReportConfigParameters());
        System.out.println("reportConfig.getReportConfigParameters().size() : " + reportConfig.getReportConfigParameters().size());
        System.out.println("\n\n");

        reportConfig = this.reportConfigService.save(reportConfig);

        return reportConfig;
    }

    @PostMapping(value = "/updateReportAnalysisSheet", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ResponseEntity<ReportAnalysisSheet> updateReportAnalysisSheet(@RequestBody ReportAnalysisSheet reportAnalysisSheet) {
//        System.out.println("zw.co.psmi.hms.mis.controllers.MisController.updateReportAnalysisSheet()");
//        System.out.println("reportAnalysisSheet : " + reportAnalysisSheet); 
//        System.out.println("reportAnalysisSheet.getReportConfig() : " + reportAnalysisSheet.getReportConfig());  

        reportAnalysisSheet = this.reportAnalysisService.save(reportAnalysisSheet, reportAnalysisSheet.getAnalysisColumnList());

        ReportAnalysisSheet sheet = reportAnalysisService.getSheetByNameAndReportConfig(reportAnalysisSheet.getName(), reportAnalysisSheet.getReportConfig().getId());
        return new ResponseEntity<>(sheet, HttpStatus.OK);
    }


    @GetMapping("/getReportConfigParameters/{reportConfigId}")
    public List<ReportConfigParameter> getReportConfigParameters(@PathVariable Long reportConfigId) {
        return reportConfigService.getParametersByReportConfig(reportConfigId);
    }

    @GetMapping("/getReportConfigParameterOptions/{reportConfigParameterId}")
    public List< String> getReportConfigParameterOptions(@PathVariable Long reportConfigParameterId) {
        return reportConfigService.getReportConfigParameterOptions(reportConfigParameterId);
    }

    @GetMapping("/getAllReportParameterTypes")
    public List<ReportParameterType> getAllReportParameterTypes() {
        List<ReportParameterType> reportParameterType = Arrays.asList(ReportParameterType.values());

        return reportParameterType;
    }

    @GetMapping("/getAllNumberFormatList")
    public List<ReportColumnNumberFormat> getAllNumberFormatList() {
        List<ReportColumnNumberFormat> numberFormatList = Arrays.asList(ReportColumnNumberFormat.values());

        return numberFormatList;
    }

    @GetMapping("/getAllAnalysisSectorList")
    public List<ReportColumnAnalysisSector> getAllAnalysisSectorList() {
        List<ReportColumnAnalysisSector> analysisSectorList = Arrays.asList(ReportColumnAnalysisSector.values());

        return analysisSectorList;
    }

    @GetMapping("/getAllReportAnalysisSheets/{reportConfigId}")
    public List<ReportAnalysisSheet> getAllReportAnalysisSheets(@PathVariable Long reportConfigId) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getAllReportAnalysisSheets()");

        return reportAnalysisService.getReportAnalysisSheetList(reportConfigId);
    }

    @GetMapping("/getReportAnalysisSheet/{reportConfigId}/{reportSheetName}")
    public ReportAnalysisSheet getReportAnalysisSheet(@PathVariable Long reportConfigId, @PathVariable String reportSheetName) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportAnalysisSheet()");

        return reportAnalysisService.getSheetByNameAndReportConfig(reportSheetName, reportConfigId);
    }

    @GetMapping("/getReportAnalysisColumns/{reportConfigId}/{reportSheetName}")
    public List<ReportAnalysisColumn> getReportAnalysisColumns(@PathVariable Long reportConfigId, @PathVariable String reportSheetName) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportAnalysisColumns()");

        return reportAnalysisService.getReportAnalysisColumnsByReportAnalysisSheet(reportConfigId, reportSheetName);
    }

    @GetMapping("/getReportAnalysisColumnsForNewSheet/{reportConfigId}")
    public List<ReportAnalysisColumn> getReportAnalysisColumnsForNewSheet(@PathVariable Long reportConfigId) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportAnalysisColumnsForNewSheet()");

        return reportAnalysisService.getReportAnalysisColumnsForNewSheet(reportConfigId);
    }

    @GetMapping("/getReportInformation/{reportName}")
    public ReportInformation getReportInformation(@PathVariable String reportName, @ModelAttribute ParameterHolder holder) {

        return misService.getReportInformation(reportName, holder);
    }

    @GetMapping("/getReportPreview/{reportName}")
    public ReportInformation getReportPreview(@PathVariable String reportName, @ModelAttribute ParameterHolder holder) {
        //System.out.println("zw.co.psmi.hms.mis.controllers.MisController.getReportPreview()");

        return misService.getReportPreviewInformation(reportName, holder);
    }

    @GetMapping("/downloadExcelReport/{reportName}")
    @ResponseBody
    public void downloadExcelReport(@PathVariable String reportName, @ModelAttribute ParameterHolder holder, HttpServletResponse response) {
        //System.out.println("\n\nzw.co.psmi.hms.mis.controllers.MisController.downloadExcelReport()\n - Updated\n\n"); 

        ReportConfig config = reportConfigService.getByName(reportName);
        holder = holder == null ? new ParameterHolder() : holder;
        boolean parametersSet = holder.getParameter1() != null;

        SXSSFWorkbook wb = reportService.getWorkBook(config, holder, parametersSet);

        try {
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", "Attachment;Filename=\"" + config.getName() + ".xlsx\"");
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.close();
            //wb.close();
        } catch (Exception e) {
        }
    }

    private ResponseEntity<XSSFWorkbook> disposeFileContent(ReportConfig config, XSSFWorkbook wb) {
        //Resource file = storageService.loadAsResource(filename);
        MediaType mediaType;
        long contentLength = 0;

        String mimeType = "APPLICATION/OCTET-STREAM";// storageService.getMimeType(file);
        mediaType = MediaType.valueOf(mimeType);

        return ResponseEntity.ok()
                //.contentLength(contentLength)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposalType.attachment + "; filename=\"" + config.getName() + ".xlsx\"")
                .contentType(mediaType)
                .body(wb);
    }

    @GetMapping("/reportview/{name}")
    public ReportInformation reportPreview(@PathVariable String reportName,
            @ModelAttribute ParameterHolder holder) {
        ReportConfig config = reportConfigService.getByName(reportName);
        boolean parametersSet = holder.getParameter1() != null;
        //System.out.println("holder.getParameter1() :" + holder.getParameter1());

        if (holder != null) {
            List<ReportConfigParameter> parametersByReportConfigId = reportConfigService.getParametersByReportConfig(config, holder);

//            ReportResult report = misService.getReportResultData(config, holder, parametersSet);
//
//            if (report.getData() != null && report.getData().length > 0) {
//                holder.setFiltered(true);
//            }
            ReportInformation reportData = new ReportInformation();

//            model.addAttribute("params", parametersByReportConfigId);
//            model.addAttribute("reportName", config.getName());
//            model.addAttribute("holder", holder);
//            model.addAttribute("columns", report.getColumnNames());
//            model.addAttribute("data", report.getData());
            return reportData;
        }

        return null;
    }

    
//    @PostMapping(value = "/updateReportAnalysisSheet", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
//    public ResponseEntity<T> create(@RequestBody T resourceEntity) throws Exception {
//        System.out.println("\n"+ resourceEntity.getClass() .getName() + "  B4 Save : " + resourceEntity + "\n");
//
//        ResponseEntity responseEntity;
//        HttpStatus httpStatus;
//        try {
//            T entity = (T) getService().create( resourceEntity);
//            httpStatus = HttpStatus.CREATED;
//            responseEntity = new ResponseEntity<>(entity, httpStatus);
//        } catch (Exception ex) {
//            httpStatus = HttpStatus.NOT_IMPLEMENTED;
//            responseEntity = new ResponseEntity<>(new Results(DBActionResult.EncounteredError, "Could not create entity.", "new", this.getClass()), httpStatus);
//
//            //System.out.println(ex.getMessage());
//            Logger.getLogger(getControllerClass().getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return responseEntity;
//    }
}
