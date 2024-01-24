package ci.yoru.katalightbank.Utils;

import ci.yoru.katalightbank.controllers.dto.AccountResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.entities.Account;
import ci.yoru.katalightbank.entities.Operation;

import java.util.ArrayList;
import java.util.Date;

public class TestUtils {
    private TestUtils() {
    }

    public static OperationRequestDto createOperationRequestDto(long amount, long account) {
        return OperationRequestDto.builder().amount(amount).accountId(account).build();
    }

    public static Account createAccount(long id, long balance) {
        return Account
                .builder()
                .id(id)
                .balance(balance)
                .operations(new ArrayList<>())
                .build();
    }

    public static Operation createOperation(long operationId, long amount) {
        return Operation
                .builder()
                .id(operationId)
                .amount(amount)
                .timestamp(new Date())
                .build();
    }

    public static OperationResponseDto createOperationResponseDto(long idOperation, long balance, OperationType operationType) {
        return OperationResponseDto.builder()
                .id(idOperation)
                .balance(balance)
                .operationType(operationType)
                .build();
    }

    public static AccountResponseDto createAccountResponseDto(long idOperation, long balance) {
        return AccountResponseDto.builder()
                .id(idOperation)
                .balance(balance)
                .operations(new ArrayList<>())
                .build();
    }
}
