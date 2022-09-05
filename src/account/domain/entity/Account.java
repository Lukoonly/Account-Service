package account.domain.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @OneToMany(mappedBy = "account")
    private List<Payroll> payrollsList = new ArrayList<>();

    public void addRole(String role) {
        if (this.role == null) {
            this.role = role + " ";
        } else {
            this.role = "ROLE_" + role + " " + this.role;
        }

    }

    public void removeRole(String role) {
        this.role = this.role.replaceAll("ROLE_" + role, "").trim();
    }

    public List<String> getRoles() {
        return List.of(this.role.split(" "));
    }
}