package study.arotein.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.arotein.base.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TempEmail extends BaseEntity {
    @Id
    @Column(name = "temp_email_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String approvalStr;

    public TempEmail(String email, String approvalStr) {
        this.email = email;
        this.approvalStr = approvalStr;
    }
}
