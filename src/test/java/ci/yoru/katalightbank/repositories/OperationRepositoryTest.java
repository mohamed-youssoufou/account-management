package ci.yoru.katalightbank.repositories;

import ci.yoru.katalightbank.Utils.TestUtils;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OperationRepositoryTest {
    @Autowired
    private OperationRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void getShouldSavedOperation() {
        val operation = TestUtils.createOperation(1, 10);
        val given = repository.save(operation);
        Assertions.assertEquals(Boolean.TRUE, repository.findById(1L).isPresent());
        Assertions.assertEquals(given.getId(), repository.findById(1L).get().getId());
    }

    @BeforeEach
    void tearDown() {
        repository.deleteAll();
    }
}