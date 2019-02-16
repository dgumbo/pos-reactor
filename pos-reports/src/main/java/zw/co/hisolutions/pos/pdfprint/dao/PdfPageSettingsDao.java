
package zw.co.hisolutions.pos.pdfprint.dao;
 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 
import zw.co.hisolutions.pos.pdfprint.entity.PdfPageSettings;


@Repository
public interface PdfPageSettingsDao extends JpaRepository<PdfPageSettings, Long>{
    PdfPageSettings findByActiveStatusTrue(); 
    public List<PdfPageSettings> findByNameAndActiveStatusTrue(String name);

    public PdfPageSettings getByName(String name);
}
