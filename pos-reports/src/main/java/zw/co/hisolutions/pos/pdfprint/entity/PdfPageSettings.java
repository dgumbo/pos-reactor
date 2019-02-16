package zw.co.hisolutions.pos.pdfprint.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "pdf_page_settings")
public class PdfPageSettings extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private int thermalPaperHeight;

    @Column(unique = true)
    private int thermalPaperWidth;

}
