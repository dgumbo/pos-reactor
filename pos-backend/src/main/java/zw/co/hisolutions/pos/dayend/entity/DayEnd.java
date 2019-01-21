package zw.co.hisolutions.pos.dayend.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author kinah
 */
@Entity
@Data
@Table(name = "day_end")
public class DayEnd extends BaseEntity {

    @OneToMany(targetEntity = DayEndItem.class, cascade = CascadeType.ALL)
    private List<DayEndItem> dayEndItems; 

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayStartDateTime;

    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dayEndDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayEndDateTime;

    @Override
    public String toString() {
        return this.getClass().getName() + " {" + "id=" + id + ", name=" + ", activeStatus=" + activeStatus + '}';
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

    @PrePersist
    @PreUpdate
    public void init1() {
        if (dayEndDate != null && dayEndDateTime == null) {
            dayEndDateTime = dayEndDate;
        }
    }

}
