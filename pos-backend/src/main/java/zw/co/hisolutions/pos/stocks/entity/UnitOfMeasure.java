package zw.co.hisolutions.pos.stocks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity
@Table(name="unit_of_measure")
@Data
public class UnitOfMeasure extends BaseEntity{
    @Column(unique = true)
    private String name;
    
    @Column(name = "is_base", nullable = false)
    private boolean base;
    
    @ManyToOne(optional = true)
    @XmlTransient
    @JsonIgnore
    private UnitOfMeasure baseUnitOfMeasure;
    
    private int baseMultiple;
    
    @Transient
    private List<UnitOfMeasure> unitOfMeasures;
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
    
    @Override
    public String toString() {
        return "UnitOfMeasure{" + "id=" + id + ", name=" + name + ", base=" + base + ", baseMultiple=" + baseMultiple + ", activeStatus=" + activeStatus + '}';
    }
}
