//package zw.co.hisolutions.pos.common.basic;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import zw.co.hisolutions.pos.common.util.Utils;
//
//import java.awt.*;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.Map;
///**
// *
// * @author com4t
// */
//@Service
//@Slf4j
//public class BasePdfOutputService {
//    protected PdfOutputDao getPdfOutputDao() {return pdfOutputDao;}
//
//    @Value("${hms.watermark}")
//    private String watermark;
//    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//    
//    private String getWatermark(){
//        return watermark;
//    }
//    
//    @Autowired
//    private PdfOutputDao pdfOutputDao;
//    
//    private Logger getLogger() {return log;}
//    
//    private Map<OutputType,ReportModelGenerator> generators;
//    
//    private final ReportModelGenerator getGenerator(OutputType outputType){
//        return generators.get(outputType);
//    }
//    public void addGenerator(OutputType outputType,ReportModelGenerator generator){
//        if(generators==null){
//            generators=new HashMap<>();
//        }
//        log.info("adding outputModelGenerator for:{}, bean:{}",outputType,generator);
//        generators.put(outputType, generator);
//    }
//    
//    //@Override
//    public byte[] outputReport(Long id, OutputType outputType) {
//        log.info("outputReport");
//        log.info("getGenerator(outputType):{}"+getGenerator(outputType));
//        return outputReport(getGenerator(outputType).findReportModelById(id));
//    }
//
//    public String getJsonData(long id, OutputType outputType) {
//        log.info(".............outputjson {}", outputType);
//        PdfOutputModel data = getGenerator(outputType).findReportModelById(id);
//        if(data == null || data.getFileldslist() == null || data.getFileldslist().isEmpty()){
//            return null;
//        }
//        data.getParameter().put("waterMark", getWatermark());
//        return getJsonData(data);
//    }
//
//    private String getJsonData(PdfOutputModel element) {
//        return gson.toJson(element);
//    }
//    protected final byte[] outputReport(PdfOutputModel reportModel) {
//        if(reportModel==null||reportModel.getReportName()==null||reportModel.getReportName().isEmpty()){
//            return "".getBytes();
//        }
//        try {
//            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
//            jasperReportsContext.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
//            jasperReportsContext.setProperty("net.sf.jasperreports.default.font.name", "Sans Serif");
//
//            PdfOutput report = getPdfOutputDao().findByNameAndActiveStatusTrue(reportModel.getReportName());
//            if (reportModel.getParameter().containsKey("barcode")) {
//                Object val = reportModel.getParameter().get("barcode");
//                if (val instanceof String) {
//                    Image barcodeImage = Utils.getBarcode((String) val);
//                    reportModel.getParameter().put("barcode", barcodeImage);
//                }
//            }
//            reportModel.getParameter().put("waterMark", getWatermark());
//            JasperPrint jprint = (JasperPrint) JasperFillManager
//                    .fillReport(new ByteArrayInputStream(report
//                            .getJasperData()), reportModel
//                            .getParameter(), new JRBeanCollectionDataSource(reportModel
//                            .getFileldslist()));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            JasperExportManager.exportReportToPdfStream(jprint, baos);
//            return baos.toByteArray();
//        } catch (JRException ex) {
//            log.info("Error:"+ex);
//            //getLogger().info("Error:", ex);
//        }
//        return new byte[1];
//    }
//}
