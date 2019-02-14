package zw.co.hisolutions.pos.sell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;  
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.masters.entity.Currency;
import zw.co.hisolutions.pos.masters.entity.PaymentType;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "receipt_item")
public class ReceiptItem extends BaseEntity {

    @ManyToOne
    @JsonIgnore
    @NotNull
    private Receipt receipt;
    
    @ManyToOne
    @NotNull
    private Currency currency;
    
    @ManyToOne
    @NotNull
    private PaymentType paymentType; 

    //@NotNull(message = "Receipt Item Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false) 
    private BigDecimal amount = BigDecimal.ZERO;

    //@NotNull(message = "Receipt Item Currency Rate is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal currencyRate = BigDecimal.ZERO;

    //@NotNull(message = "Receipt Item Converted Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal convertedAmount = BigDecimal.ZERO;
    
    private String remarks;
    
    @PrePersist
    @PreUpdate
    public void init1() {     
        if ( amount == null || amount.doubleValue() <= 0.01   ) {
            throw new IllegalArgumentException( "Receipt Item Amount Cannot Be Zero!" );
        }  
        if ( convertedAmount == null || convertedAmount.doubleValue() <= 0.01   ) {
            throw new IllegalArgumentException( "Receipt Item Converted Amount Cannot Be Zero!" );
        }  
    }
}
