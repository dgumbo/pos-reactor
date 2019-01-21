package zw.co.hisolutions.pos.storage.entity;
 
import com.fasterxml.jackson.annotation.JsonIgnore; 
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hisolutions.pos.common.entity.BaseEntity;

/**
 *
 * @author denzil
 */
@Entity//(name = "document_metadata")
@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class DocumentMetadata extends BaseEntity implements Serializable  {
    @Column(unique = true)
    @NotNull
    private String filename; 

    @NotNull 
    private String mimeType; 
    
    @JsonIgnore
    @Column
    //@NotNull
    private String filePath; 
    
    @Column
    @NotNull    
    private Status status;
    
    @Column    
    private String description;
}
