package zw.co.hisolutions.pos.pdfprint.service.impl;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.pdfprint.dao.PdfPageSettingsDao;
import zw.co.hisolutions.pos.pdfprint.entity.PdfPageSettings;
import zw.co.hisolutions.pos.pdfprint.service.PdfPageSettingsService;
import zw.co.hisolutions.pos.stocks.api.StockItemController;

@Service
@Slf4j
public class PdfPageSettingsServiceImpl implements PdfPageSettingsService { 

    private final PdfPageSettingsDao productDao; 

    public PdfPageSettingsServiceImpl(PdfPageSettingsDao productDao ) {
        this.productDao = productDao; 
    }

    @Override
    public PdfPageSettings findByName(String name) {
        PdfPageSettings p = productDao.getByName(name);
        return p;
    } 

    @Override
    public JpaRepository<PdfPageSettings, Long> getDao() {
        return productDao;
    }

    @Override
    public Class getController() {
        return StockItemController.class;
    }

    @Override
    public PdfPageSettings save(PdfPageSettings stockItem) throws IllegalArgumentException {
        return create(stockItem);
    }

    @Override
    public void deletePdfPageSettings(PdfPageSettings product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
