package zw.co.hisolutions.pos.sell.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.stocks.entity.StockItem;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table
public class StockOut extends BaseEntity {

    @ManyToOne(targetEntity = StockItem.class)
    @NotNull
    private StockItem stockItem;

    @NotNull
    private Integer frequency;

    @Override
    public String toString() {
        return this.getClass().getName() + " {" + "id=" + id + ", stockItem=" + stockItem + ", activeStatus=" + activeStatus + '}';
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
