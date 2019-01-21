package zw.co.hisolutions.pos.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
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
public class Menu extends BaseEntity{
    
    @NotNull
    @ManyToOne(targetEntity = MenuGroup.class)
    @JoinColumn(name = "menu_group_id", referencedColumnName = "id" )  
    private MenuGroup menuGroup;
    
    @Column(unique = true)
    private String name;
    private String uri; 
      
}
