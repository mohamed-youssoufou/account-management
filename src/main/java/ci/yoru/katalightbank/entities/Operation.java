package ci.yoru.katalightbank.entities;

import ci.yoru.katalightbank.controllers.dto.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity(name = "operation")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated
    private OperationType operationType;
    private double amount;
    private Date timestamp;
}
