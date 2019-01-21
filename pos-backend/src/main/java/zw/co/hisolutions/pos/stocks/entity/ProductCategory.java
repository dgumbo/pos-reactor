 package zw.co.hisolutions.pos.stocks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; 
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author denzil
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ProductCategory extends BaseEntity {
    
    @Column(unique = true, nullable = false) 
    private String name;    
    
    private String description ;
    
    private String imageUrl;
}
