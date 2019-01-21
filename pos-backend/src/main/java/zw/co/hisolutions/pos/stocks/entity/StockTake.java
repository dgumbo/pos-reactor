package zw.co.hisolutions.pos.stocks.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.hisolutions.pos.common.entity.BaseEntity; 

@Entity
@Table(name = "stock_take")
@Data
public class StockTake extends BaseEntity {

    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stockTakeDate = new Date();
    
    private String remarks;

    @OneToMany(targetEntity = StockTakeLine.class, cascade = CascadeType.ALL)
    private List<StockTakeLine> stockTakeLines;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private StockTransactionStatus stockTakeStatus ;
      
    @OneToOne ( targetEntity = StockTransaction.class )
    private StockTransaction stockTransaction ;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "StockCount{" + "id=" + id + ", stockTakeStatus = " + stockTakeStatus+", stockTakeDate = " + stockTakeDate + " , activeStatus=" + activeStatus + '}';
    }

}
