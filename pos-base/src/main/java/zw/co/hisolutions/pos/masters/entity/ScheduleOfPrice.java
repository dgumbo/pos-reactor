package zw.co.hisolutions.pos.masters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 
import zw.co.hisolutions.pos.backend.enums.ChargeType;
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author denzil
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
    @Index(unique = true, name = "idx_nc_schedule_of_price_charge_type_product_id_currency", columnList = "charge_type, product_id, currency_id")})
public class ScheduleOfPrice extends BaseEntity {

    @Column(name = "charge_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(targetEntity = StockItem.class)
    private StockItem product;

    @JoinColumn(name = "currency_id", nullable = false)
    @ManyToOne(targetEntity = Currency.class)
    private Currency currency;

    @Column(name = "description")
    private String description;

    @Min(0)
    @Column(nullable = false)
    private BigDecimal price;

    @Min(0)
    private double ratio;

    @Min(0)
    private BigDecimal additionalCharge; 
}
