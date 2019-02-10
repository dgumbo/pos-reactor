package zw.co.hisolutions.pos.masters.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name="currency")
public class Currency  extends BaseEntity { 
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private boolean defaultCurrency;
     
    private boolean displayOnSummary;
    
    private String symbol;
    
    @Column(nullable = false)
    private BigDecimal currencyRate ;
    
    @Column(nullable = false)
    private BigDecimal bondRate ;   
}
