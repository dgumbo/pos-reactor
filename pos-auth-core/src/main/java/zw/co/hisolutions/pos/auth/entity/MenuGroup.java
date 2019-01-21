package zw.co.hisolutions.pos.auth.entity;

import java.util.List; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author dgumbo
 */
@Entity 
@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class MenuGroup extends BaseEntity { 
    
    @Column(unique = true)
    private String name;
    private String uri; 
      
    @Transient
    @OneToMany( fetch = FetchType.LAZY ) 
    private List<UserRole> roles; 
     
}
