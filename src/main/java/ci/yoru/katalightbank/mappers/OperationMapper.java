package ci.yoru.katalightbank.mappers;

import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.entities.Account;
import ci.yoru.katalightbank.entities.Operation;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class OperationMapper {
    public Operation toEntity(final OperationRequestDto operationsDto, final OperationType operationType) {
        return Operation.builder()
                .amount(operationsDto.amount())
                .timestamp(new Date())
                .operationType(operationType)
                .build();
    }

    public OperationResponseDto toDto(final Operation operation, final Account account) {
        return OperationResponseDto.builder()
                .id(operation.getId())
                .balance(account.getBalance())
                .operationType(operation.getOperationType())
                .build();
    }
}
