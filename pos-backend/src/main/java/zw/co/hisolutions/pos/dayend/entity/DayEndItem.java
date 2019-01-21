package zw.co.hisolutions.pos.dayend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity; 
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.masters.entity.PaymentType;

/**
 *
 * @author kinah
 */
@Entity
@Data
@Table(name = "day_end_item")
public class DayEndItem extends BaseEntity {

    @ManyToOne
    @NotNull
    @JsonIgnore
    private DayEnd dayEnd;
    
    @ManyToOne
    @NotNull
    private PaymentType paymentType;
     
    @ManyToOne
    @NotNull
    private Currency currency;
    
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;
}
