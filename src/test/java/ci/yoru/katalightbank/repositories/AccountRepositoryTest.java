package ci.yoru.katalightbank.repositories;

import ci.yoru.katalightbank.Utils.TestUtils;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSavedOperationAndGetCorrectInformation() {
        val account = TestUtils.createAccount(1, 10);
        val given = repository.save(account);
        Assertions.assertEquals(Boolean.TRUE, repository.findById(1L).isPresent());
        Assertions.assertEquals(given.getBalance(), repository.findById(1L).get().getBalance());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}