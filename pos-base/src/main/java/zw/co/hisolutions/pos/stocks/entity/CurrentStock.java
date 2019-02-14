package zw.co.hisolutions.pos.stocks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.common.exceptions.PosException;

/**
 *
 * @author dgumbo
 */
@Entity
@Table(name = "current_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrentStock extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "stock_item_id", referencedColumnName = "id", nullable = false, updatable = false)
    private StockItem stockItem;

    @Column(name = "batch_number", length = 20, nullable = false)
    private String batchNumber;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "expiry_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StockStatus stockStatus = StockStatus.AVAILABLE;

//    @XmlTransient
//    @JsonIgnore
//    @JoinColumn(name = "create_transaction_id", updatable = false, nullable = false)
//    private StockTransaction createTransaction;
    @Column(updatable = false, nullable = false)
    private long quantity;

    @Column(updatable = false, nullable = false)
    private boolean consignment = false;

    @Column(nullable = false)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean cashOnly = false;

    @Transient
    private Long bookStockId = 0L;

    @Transient
    private BigDecimal vat = BigDecimal.ZERO;

    public BigDecimal getTotalCost() {
        // stockStatus.name();
        return unitCost.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "CurrentStock{product="+ stockItem.getName() + ", batchNumber=" + batchNumber + ", quantity = " + quantity + ", consignment = " + consignment + /*" , createTransaction = " + createTransaction + */ ", activeStatus=" + activeStatus + '}';
    }

    public CurrentStock(StockItem product, String batchNumber, Date expiryDate, StockStatus stockStatus, /*StockTransaction createTransaction,*/ Long quantity, Boolean consignment,  Boolean cashOnly) {
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.stockStatus = stockStatus;
//        this.createTransaction = createTransaction;
        this.quantity = quantity;
        this.consignment = consignment;
        this.stockItem = product; 
        this.cashOnly = cashOnly;
    }

    public CurrentStock cloneSplit() {
        return new CurrentStock(
                stockItem, batchNumber, expiryDate, stockStatus, /*createTransaction,*/ quantity, consignment,  cashOnly
        );
    }

    public long getStockItemId() {
        return stockItem != null ? stockItem.getId() : -1;
    }

    @PrePersist
    public void expiryDateUpdate() {
        if (stockItem.isExpirable()) {
            expiryDate = DateTime.now().plusYears(30).toDate();
        }

        if (quantity < 0) {
            throw new PosException("cannot save negative stock quantity!" + quantity + "::" + this);
        }
    }
}
