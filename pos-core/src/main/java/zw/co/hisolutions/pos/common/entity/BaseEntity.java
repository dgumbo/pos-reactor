package zw.co.hisolutions.pos.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author denzil
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity implements Serializable {

    public static final DateTimeFormatter timeFommarter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter dateFommarter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @JsonIgnore
    public String getAuditDetails() {
        return toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Version
    private long version = 1L;

    @NotNull
    @Column(name = "created_by_user", updatable = false)
    @CreatedBy
    private String createdByUser;

    @NotNull
    @Column(name = "creation_time", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    @NotNull
    @Column(name = "modified_by_user")
    @LastModifiedBy
    private String modifiedByUser;

    @NotNull
    @Column(name = "modification_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;

    @NotNull
    @Column(name = "active_status")
    protected boolean activeStatus = true;

    @PrePersist
    public void prePersist() {
//        this.creationTime = dtf.format(LocalDateTime.now());
//        this.modificationTime = dtf.format(LocalDateTime.now());        

        this.creationTime = new Date();
        this.modificationTime = new Date();

        //   Account account =   accountUserDetails.getAccount();
        this.createdByUser = "test";// account.getUsername();
        this.modifiedByUser = "test";// account.getUsername() ;// usernameAuditorAware.getCurrentAuditor();
    }

    @PreUpdate
    public void preUpdate() {
//        Account account = accountUserDetails.getAccount();

        //this.modificationTime = dtf.format(LocalDateTime.now());
        this.modificationTime = new Date();
        this.modifiedByUser = "test";//account.getUsername() ;// usernameAuditorAware.getCurrentAuditor();
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
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
