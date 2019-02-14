package zw.co.hisolutions.pos.masters.entity;

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
@Table(name="payment_type")
public class PaymentType  extends BaseEntity{
    @NotNull
    private String name;
    
    @NotNull
    private boolean defaultPaymentType ;
}