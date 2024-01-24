package ci.yoru.katalightbank.controllers.dto;

import ci.yoru.katalightbank.entities.Operation;
import lombok.Builder;

import java.util.List;

@Builder
public record AccountResponseDto(
        long id,
        double balance,
        List<Operation> operations
) {
}
