package ci.yoru.katalightbank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double balance;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Operation> operations;

    @Override
    public String toString() {
        return String.format("id: %s | balance :%s", id, balance);
    }
}
