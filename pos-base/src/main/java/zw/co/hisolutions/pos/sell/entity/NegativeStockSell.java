package zw.co.hisolutions.pos.sell.entity;

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
@Table
public class NegativeStockSell extends BaseEntity {

    @NotNull
    private boolean allowAll;

    @Override
    public String toString() {
        return this.getClass().getName() + " {" + "id=" + id + ", allowAll=" + allowAll + ", activeStatus=" + activeStatus + '}';
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

}
