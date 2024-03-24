package ci.yoru.katalightbank.services;

import ci.yoru.katalightbank.controllers.dto.AccountResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.entities.Account;
import ci.yoru.katalightbank.exceptions.ClientAccountNotFoundException;
import ci.yoru.katalightbank.exceptions.OperationNotAllowedException;
import ci.yoru.katalightbank.mappers.AccountMapper;
import ci.yoru.katalightbank.mappers.OperationMapper;
import ci.yoru.katalightbank.repositories.AccountRepository;
import ci.yoru.katalightbank.repositories.OperationRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OperationService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public OperationService(
            final AccountRepository accountRepository,
            final OperationRepository operationRepository

    ) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    public OperationResponseDto performOperation(final OperationRequestDto dto, final OperationType operationType) {
        var account = accountRepository.findById(dto.accountId()).orElseThrow(ClientAccountNotFoundException::new);
        account.setBalance(getRetrieveNewBalance(account, dto, operationType));
        val operation = OperationMapper.toEntity(dto, operationType);
        val savedOperation = operationRepository.save(operation);
        account.getOperations().add(savedOperation);
        val savedAccount = accountRepository.save(account);
        return OperationMapper.toDto(savedOperation, savedAccount);
    }

    private double getRetrieveNewBalance(final Account account, final OperationRequestDto dto, final OperationType operationType) {
        if (account.getBalance() < dto.amount() && operationType.equals(OperationType.WITHDRAWAL))
            throw new OperationNotAllowedException();

        return switch (operationType) {
            case WITHDRAWAL -> account.getBalance() - dto.amount();
            case DEPOSIT -> account.getBalance() + dto.amount();
        };
    }

    public AccountResponseDto checkStatus(final Long id) {
        val account = accountRepository.findById(id).orElseThrow(ClientAccountNotFoundException::new);
        return AccountMapper.toDto(account);
    }
}
