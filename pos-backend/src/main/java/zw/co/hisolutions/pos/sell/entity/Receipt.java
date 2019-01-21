package zw.co.hisolutions.pos.sell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;   
import zw.co.hisolutions.pos.common.entity.BaseEntity;   

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name = "receipt")
public class Receipt extends BaseEntity {  
    
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore 
    private Sell sell; 
     
    @OneToMany(targetEntity=ReceiptItem.class, cascade=CascadeType.ALL ) 
    private List<ReceiptItem> receiptItems; 

    //@NotNull(message = "Receipt Paid Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal tenderedAmount = BigDecimal.ZERO;

    //@NotNull(message = "Receipt Paid Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    //@NotNull(message = "Receipt Payable Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal payableAmount = BigDecimal.ZERO;

    // @NotNull(message = "Receipt Change Amount is mandatory and cannot be Null")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal changeAmount = BigDecimal.ZERO;

    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date receiptDate  ;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date receiptDateTime  ; 

    private String username;    

    public BigDecimal getReceiptValueAmount() {
        return paidAmount.subtract(changeAmount);
    }
 
    public String toString() {
        return "Receipt{" + "id=" + id + ", name=" + ", activeStatus=" + activeStatus + '}';
    }
 
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
 
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final BaseEntity other = (BaseEntity) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @PrePersist
    @PreUpdate
    public void init1() {
        if (receiptDate != null && (receiptDateTime == null || receiptDateTime.compareTo(receiptDate) != 0)) {
            receiptDateTime = receiptDate;
        }
        validation();
    }

    private void validation() {
        if (payableAmount == null || payableAmount.doubleValue() < 0.01) {
            throw new IllegalArgumentException("Receipt Payable Amount [" + payableAmount.doubleValue() + "] Cannot Be Zero!");
        }
        if (paidAmount == null || paidAmount.doubleValue() < 0.01) {
            throw new IllegalArgumentException("Receipt Paid Amount [" + paidAmount.doubleValue() + "] Cannot Be Zero!");
        }
        /* Change can Be Zero but Cannot be null */
        if (changeAmount == null) {
            throw new IllegalArgumentException("Change Amount Cannot Be Null !");
        }
        /* Paid Amount Minus Change Amount Gives Amount Value of Receipt And Should Not be greater than Payable Amount (Total Amount We expecting from Patient) */
        if (getReceiptValueAmount().doubleValue() > payableAmount.doubleValue()) {
            throw new IllegalArgumentException("Receipt Paid Amount [" + paidAmount.doubleValue() + "] Minus Change Amount [" + changeAmount.doubleValue() + "] Cannot Be Greater than Payable Amount [" + payableAmount.doubleValue() + "] !");
        }
    }

}
