package ci.yoru.katalightbank.services;


import ci.yoru.katalightbank.Utils.TestUtils;
import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.exceptions.ClientAccountNotFoundException;
import ci.yoru.katalightbank.repositories.AccountRepository;
import ci.yoru.katalightbank.repositories.OperationRepository;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        operationRepository.deleteAll();
    }

    @Test
    void shouldCheckStatusThrowExceptionWhenAccountNotExist() {
        Assertions.assertThrows(ClientAccountNotFoundException.class, () -> operationService.checkStatus(1L), "Le compte n'existe pas");
    }

    @Test
    void shouldThrowExceptionWhenTryDepositOnNotFoundAccount() {
        val operationRequestDto = OperationRequestDto.builder().amount(10L).accountId(1L).build();
        Assertions.assertThrows(ClientAccountNotFoundException.class, () -> operationService.performOperation(operationRequestDto, OperationType.DEPOSIT), "Le compte n'existe pas");
    }

    @Test
    void shouldThrowExceptionWhenTryWithDrawlOnNotFoundAccount() {
        val operationRequestDto = TestUtils.createOperationRequestDto(1, 10l);
        Assertions.assertThrows(ClientAccountNotFoundException.class, () -> operationService.performOperation(operationRequestDto, OperationType.WITHDRAWAL), "Le compte n'existe pas");
    }

    @Test
    void shouldDepositWithSuccessAndMakeSureItCallExternalComponentCorrectly() {
        // given
        val account = TestUtils.createAccount(1, 10);
        val operationRequestDto = TestUtils.createOperationRequestDto(100, 1);
        val operation = TestUtils.createOperation(110L, 1);
        // when
        when(accountRepository.findById(operationRequestDto.accountId())).thenReturn(Optional.of(account));
        when(operationRepository.save(any())).thenReturn(operation);
        when(accountRepository.save(any())).thenReturn(account);
        // exec
        val dto = operationService.performOperation(operationRequestDto, OperationType.DEPOSIT);
        // verify
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(accountRepository).findById(argumentCaptor.capture());
        verify(accountRepository).save(any());
        verify(operationRepository).save(any());
        Assertions.assertEquals(110L, dto.balance());
    }

    @Test
    void shouldWithdrawalWithSuccessAndMakeSureItCallExternalComponentCorrectly() {
        // given
        val account = TestUtils.createAccount(1, 10);
        val operation = TestUtils.createOperation(1, 10);
        val operationRequestDto = TestUtils.createOperationRequestDto(10, 1);

        // when
        when(accountRepository.save(any())).thenReturn(account);
        when(operationRepository.save(any())).thenReturn(operation);
        when(accountRepository.findById(operationRequestDto.accountId())).thenReturn(Optional.of(account));
        // exec
        val dto = operationService.performOperation(operationRequestDto, OperationType.WITHDRAWAL);

        // very
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(accountRepository).findById(argumentCaptor.capture());
        verify(accountRepository).save(any());
        verify(operationRepository).save(any());

        Assertions.assertEquals(0L, dto.balance());


    }

    @Test
    void shouldCheckStatusAndMakeSureThatItCallMethodFindByIdWithCorrectArgument() {
        // given
        val account = TestUtils.createAccount(1, 10l);
        //when
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        operationService.checkStatus(1L);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        //then
        verify(accountRepository).findById(argumentCaptor.capture());
    }

    @BeforeEach
    void tearDown() {
        accountRepository.deleteAll();
        operationRepository.deleteAll();
    }
}