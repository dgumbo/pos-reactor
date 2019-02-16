package zw.co.hisolutions.pos.pdfprint.service;

import zw.co.hisolutions.pos.common.service.GenericService;
import zw.co.hisolutions.pos.pdfprint.entity.PdfPageSettings; 

public interface PdfPageSettingsService extends GenericService<PdfPageSettings, Long> {
 
    PdfPageSettings findByName(String name);

    void deletePdfPageSettings(PdfPageSettings product); 

}
