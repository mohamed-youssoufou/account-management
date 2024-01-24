package ci.yoru.katalightbank.repositories;

import ci.yoru.katalightbank.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
