package study.arotein.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import study.arotein.enumeration.Status;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createTime;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status = Status.ENABLED;
}
