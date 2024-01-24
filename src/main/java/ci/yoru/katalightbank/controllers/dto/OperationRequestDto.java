package ci.yoru.katalightbank.controllers.dto;

import lombok.Builder;


@Builder
public record OperationRequestDto(
        long accountId,
        double amount
) {
}
