package ci.yoru.katalightbank.controllers.dto;

import lombok.Builder;


@Builder
public record OperationResponseDto(
        long id,
        OperationType operationType,
        double balance
) {
}
