package ci.yoru.katalightbank.repositories;

import ci.yoru.katalightbank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
