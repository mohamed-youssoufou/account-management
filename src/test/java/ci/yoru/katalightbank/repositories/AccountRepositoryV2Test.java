package ci.yoru.katalightbank.repositories;

import ci.yoru.katalightbank.entities.Account;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryV2Test {
    
    @Autowired private AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void should_return_empty_list_when_get_all() {
        var accounts = repository.findAll();
        Assertions.assertEquals( 0, accounts.size());
    }

    @Test
    void should_return_account_when_given_new_account() {
        var newAccount = repository.save(
                Account.builder()
                        .id(1L)
                        .balance(1_000_000_000d)
                        .build()
        );

        var findAccount = repository.findById(1L);

        assertEquals(newAccount, findAccount.get());
        assertEquals( 1L, newAccount.getId());
        assertEquals( 1000000000, newAccount.getBalance());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}
