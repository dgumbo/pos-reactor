package zw.co.hisolutions.pos.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class MenuRoles extends BaseEntity   {
    
    @NotNull
    @ManyToOne(targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id", referencedColumnName = "id" )  
    private UserRole role;
     
    //@NotNull
    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false )  
    private Menu menu;
    
    @Column(name = "can_view")
    private boolean view ; 
    @Column(name = "can_edit")
    private boolean edit ; 
    @Column(name = "can_create")
    private boolean create ; 
    @Column(name = "can_delete")
    private boolean delete ;   
}
