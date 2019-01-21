package zw.co.hisolutions.pos.sell.entity; 

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany; 
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;  
import zw.co.hisolutions.pos.stocks.entity.StockTransaction;
import zw.co.hisolutions.pos.stocks.entity.StockTransactionType;

/**
 *
 * @author dgumbo
 */ 
@Entity
@Data
@Table(name = "sell")
public class Sell extends BaseEntity {  
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus transactionStatus;   
    
    @Column(nullable = false)
    private BigDecimal sellAmount  ; 
    
    @Column(nullable = false)
    private Long itemCount; 
    
    @Transient
    @OneToOne ( targetEntity = StockTransaction.class )
    private StockTransaction stockTransaction; 
     
    @OneToMany(targetEntity=SellItem.class, cascade=CascadeType.ALL ) 
    private List<SellItem> sellItems; 
     
    @OneToOne 
    @NotNull
    private Receipt receipt ;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date sellDate  ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date sellDateTime ;

    private String username; 
     
    @Enumerated(EnumType.STRING)
    private StockTransactionType stockTransactionType = StockTransactionType.STOCK_SELL; 
        
    @Transient
    private Boolean printDirectly;
    
    @Transient
    private String availablePrinters;
     
    @Transient
    private boolean depositAvailable = false;   
       
    @Override
    public String toString() {
        return "Sell{" + "id=" + id + ", activeStatus=" + activeStatus + '}';
    }
 
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
 
    @Override
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

    @PostLoad
    public void init1() {  
        transactionStatus = transactionStatus == null ? transactionStatus.CREATED : transactionStatus;
    }

    @PrePersist
    @PreUpdate
    public void init2() {
        if (sellDate != null && (sellDateTime == null || sellDateTime.compareTo(sellDate) < 0)) {
            sellDateTime = sellDate;
        }
        transactionStatus = transactionStatus == null ? transactionStatus.CREATED : transactionStatus;
    }

}
