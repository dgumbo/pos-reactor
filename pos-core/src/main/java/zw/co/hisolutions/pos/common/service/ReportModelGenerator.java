package zw.co.hisolutions.pos.common.service;

import zw.co.hisolutions.pos.common.models.PdfOutputModel;

/**
 *
 * @author com4t
 */
public interface ReportModelGenerator {    
    public PdfOutputModel findReportModelById(Long id);
    //@PostConstruct
    public void init();
}
