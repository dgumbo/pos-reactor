package zw.co.hisolutions.pos.stocks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;
import zw.co.hisolutions.pos.stocks.emuns.SupplierClassification;

/**
 *
 * @author dgumbo
 */
@Entity
@Data
@Table(name="stock_supplier")
public class StockSupplier extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private SupplierClassification classification; 
    
    @Column
    private String address; 
    
    @Override
    public String toString() {
        return "StockSupplier{" + "id=" + id + ", name=" + name + ", activeStatus=" + activeStatus + '}';
    }
    
}