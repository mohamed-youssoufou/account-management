package ci.yoru.katalightbank.services;

import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.entities.Account;
import ci.yoru.katalightbank.entities.Operation;
import ci.yoru.katalightbank.exceptions.ClientAccountNotFoundException;
import ci.yoru.katalightbank.exceptions.OperationNotAllowedException;
import ci.yoru.katalightbank.repositories.AccountRepository;
import ci.yoru.katalightbank.repositories.OperationRepository;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceV2Test {

    @Mock private AccountRepository accountRepository;
    @Mock private OperationRepository operationRepository;
    @InjectMocks private OperationService service;

    // use case 1:
    // performOperation : params la requete
    // verifie l'existance du compte => si n'existe pas => lancer une exception
    // dans un cas de retrait : il compare le sold courant, du retrait  => si courant < retrait => exception
    //                                                                  => si courant > retrait => faire -
    // dans le cas d'un depot : on additionne
    // ex: if account not exist :
    //          accountId: 1 ; amount 100 ; operation withdraw => throw ClientAccountNotFoundException
    //     if account exist:
    //          accountId: 1 ; amount 100 ; current balance : 0 operation withdraw => throw OperationNotAllowedException
    //     if account exist:
    //          accountId: 1 ; amount 100 ; current balance : 200 operation withdraw => savedOperation and return new balance 100 and getOperations size = 1
    //     if account exist:
    //          accountId: 1 ; amount 100 ; current balance : 200 operation deposit => savedOperation and return new balance 300 and getOperations size = 1


    @Test void shoud_throw_ClientAccountNotFoundException_when_given_not_exist_account(){
        // given
        val operationDto = OperationRequestDto.builder().accountId(1).amount(100).build();
        val operationType = OperationType.WITHDRAWAL;
        //when
        when(accountRepository.findById(operationDto.accountId())).thenReturn(Optional.empty());
        // exec
        Assertions.assertThrows(ClientAccountNotFoundException.class, () -> service.performOperation(operationDto, operationType));
    }

    @Test void shoud_throw_OperationNotAllowedException_when_withdrawal_amount_great_than_current_account_sold(){
        // given
        val operationDto = OperationRequestDto.builder().accountId(1).amount(100).build();
        val operationType = OperationType.WITHDRAWAL;
        // when
        val account = Account.builder().id(operationDto.accountId()).balance(0).operations(List.of()).build();
        when(accountRepository.findById(operationDto.accountId())).thenReturn(Optional.of(account));
        // exec
        Assertions.assertThrows(OperationNotAllowedException.class, () -> service.performOperation(operationDto, operationType));
    }

    @Test void shoud_return_100_when_withdrawal_100_with_balance_200(){
        // given
        val operationDto = OperationRequestDto.builder().accountId(1).amount(100).build();
        val operationType = OperationType.WITHDRAWAL;
        // when
        val account = Account.builder().id(operationDto.accountId()).balance(200).operations(new ArrayList<>()).build();
        when(accountRepository.findById(operationDto.accountId())).thenReturn(Optional.of(account));

        val operationSaved = Operation.builder().id(1L).operationType(operationType).amount(operationDto.amount()).build();
        when(operationRepository.save(any())).thenReturn(operationSaved);

        val accountSaved = Account.builder().id(operationDto.accountId()).balance(100).operations(new ArrayList<>()).build();
        when(accountRepository.save(any())).thenReturn(accountSaved);
        // exec
        val operationResponse = OperationResponseDto
                                                        .builder()
                                                        .balance(100)
                .id(1L)
                .operationType(OperationType.WITHDRAWAL)
                                                        .build();
        Assertions.assertEquals(service.performOperation(operationDto, operationType), operationResponse);
    }

}