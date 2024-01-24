package ci.yoru.katalightbank;

import ci.yoru.katalightbank.controllers.OperationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KataLightBankApplicationTests {

    @Autowired
    private OperationController operationController;

    @Test
    void contextLoads() {
        Assertions.assertThat(operationController).isNotNull();
    }

}
