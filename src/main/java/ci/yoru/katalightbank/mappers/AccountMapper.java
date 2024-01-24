package ci.yoru.katalightbank.mappers;

import ci.yoru.katalightbank.controllers.dto.AccountResponseDto;
import ci.yoru.katalightbank.entities.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {
    public AccountResponseDto toDto(final Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .operations(account.getOperations())
                .build();
    }
}
