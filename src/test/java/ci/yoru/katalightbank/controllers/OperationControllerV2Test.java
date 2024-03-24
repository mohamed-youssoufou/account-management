package ci.yoru.katalightbank.controllers;

import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.exceptions.ClientAccountNotFoundException;
import ci.yoru.katalightbank.services.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.xmlunit.util.Mapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(OperationController.class)
@AutoConfigureMockMvc
class OperationControllerV2Test {

    @MockBean private OperationService service;

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;


    @Test void when_deposit_throw_an_ClientAccountNotFoundException() throws Exception {
        // given
        val operationDto = OperationRequestDto.builder().accountId(1).amount(100).build();
        val operationType = OperationType.DEPOSIT;

        // when
        Mockito.when(service.performOperation(operationDto, operationType)).thenThrow(ClientAccountNotFoundException.class);

        // exec

        val url = "/api/v1/operations/deposit";
        var payload = mapper.writeValueAsString(operationDto);
        var response = mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(payload));
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientAccountNotFoundException));
    }

}