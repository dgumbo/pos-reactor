package zw.co.hisolutions.pos.stocks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hisolutions.pos.masters.entity.ScheduleOfPrice;
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.common.entity.EntityWithName;

/**
 *
 * @author denzil
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockItem extends BaseEntity implements EntityWithName {

    @OneToMany(mappedBy = "product")
    private List<ScheduleOfPrice> scheduleOfPrices = new ArrayList<>();

    @Transient
    private List<CurrentStock> currentStockList;

    @Column(unique = true, nullable = false)
    private String name;

    // @Column(unique = true)
    private String barcode;

    private String shortcode;

    private String description;

    private String faIcon;
    private String imageName;
    private String imageUrl;

    @NotNull
    @ManyToOne(targetEntity = ProductCategory.class)
    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    private ProductCategory productCategory;

    @Column(nullable = false)
    private boolean expirable = false;

    @ManyToOne
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "weighted_average_cost", precision = 12, scale = 4)
    private BigDecimal weightedAverageCost = BigDecimal.ZERO;

    @Column(name = "last_receipt_cost_rate", precision = 12, scale = 4)
    private BigDecimal lastReceiptCostRate = BigDecimal.ZERO;

    public BigDecimal getBondPrice() {
        for (ScheduleOfPrice sop : scheduleOfPrices) {
            if (sop.getCurrency().isDefaultCurrency()) {
                return sop.getPrice();
            }
        }
        return BigDecimal.ZERO;
    }

    @Transient
    private static BigDecimal ecocashRate;

    @Transient
    private static BigDecimal usdRate;

    @JsonIgnore
    public void setEcocashRate(BigDecimal rate) {
        ecocashRate = rate;
    }

    public BigDecimal getEcocashRate() {
        return ecocashRate;
    }

    @JsonIgnore
    public void setUSDRate(BigDecimal rate) {
        usdRate = rate;
    }

    public BigDecimal getUsdRate() {
        return usdRate;
    }

    public BigDecimal getEcocashPrice() {
        for (ScheduleOfPrice sop : scheduleOfPrices) {
            if (sop.getCurrency().getName().equalsIgnoreCase("ecocash")) {
                return sop.getPrice();
            }
        }

        if (ecocashRate != null && ecocashRate != BigDecimal.ZERO) {
            return getBondPrice().multiply(ecocashRate);
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getUsdPrice() {
        for (ScheduleOfPrice sop : scheduleOfPrices) {
            if (sop.getCurrency().getName().equalsIgnoreCase("USD")) {
                return sop.getPrice();
            }
        }

        if (usdRate != null && usdRate != BigDecimal.ZERO) {
            return getBondPrice().multiply(usdRate);
        }

        return BigDecimal.ZERO;
    }

    public CurrentStock getCurrentStock() {
        return getAvailableStockByConsignementStatus(false);
    }

    public CurrentStock getConsignmentStock() {
        return getAvailableStockByConsignementStatus(true);
    }

    private CurrentStock getAvailableStockByConsignementStatus(boolean consignment_status) {
        currentStockList = currentStockList == null ? new ArrayList() : currentStockList;
        Optional<CurrentStock> acs = currentStockList.stream()
                .filter(ccss -> ccss.isConsignment() == consignment_status && ccss.getStockStatus() == StockStatus.AVAILABLE)
                .findAny();
        if (acs.isPresent()) {
            return acs.get();
        } else {
            CurrentStock cs = new CurrentStock();
            cs.setQuantity(0);
            cs.setConsignment(consignment_status);
            cs.setStockStatus(StockStatus.AVAILABLE);
            cs.setStockItem(this);
            cs.setId(-1L);
            currentStockList.add(cs);
            return cs;
        }
    }

    @Override
    public String toString() {
        return "Product { name:" + name + ", id: +" + id + " }";
    }
}
