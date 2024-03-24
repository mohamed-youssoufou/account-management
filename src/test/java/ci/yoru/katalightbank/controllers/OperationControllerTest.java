package ci.yoru.katalightbank.controllers;

import ci.yoru.katalightbank.Utils.TestUtils;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.exceptions.ClientAccountNotFoundException;
import ci.yoru.katalightbank.services.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OperationController.class)
@AutoConfigureMockMvc
class OperationControllerTest {

    @MockBean
    private OperationService service;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldDepositWhenAccountExist() throws Exception {
        // given
        val dto = TestUtils.createOperationRequestDto(1L, 100L);
        val response = TestUtils.createOperationResponseDto(1L, 1100L, OperationType.DEPOSIT);
        val request = objectMapper.writeValueAsString(dto);
        // when
        when(service.performOperation(dto, OperationType.DEPOSIT)).thenReturn(response);
        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/operations/deposit")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));
    }

    @Test
    void shouldWithdrawOnNotExistingAccountThrowException() throws Exception {
        // given
        val operationRequest = TestUtils.createOperationRequestDto(100L, 1);
        val request = objectMapper.writeValueAsString(operationRequest);
        // when
        when(service.performOperation(operationRequest, OperationType.WITHDRAWAL)).thenThrow(new ClientAccountNotFoundException());
        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/operations/withdrawal")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientAccountNotFoundException));
    }

    @Test
    void shouldDepositOnNotExistingAccountThrowException() throws Exception {
        // given
        val operationRequest = TestUtils.createOperationRequestDto(100L, 1);
        val request = objectMapper.writeValueAsString(operationRequest);

        // when
        when(service.performOperation(operationRequest, OperationType.DEPOSIT)).thenThrow(new ClientAccountNotFoundException());

        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/operations/deposit")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ClientAccountNotFoundException));
    }

    @Test
    void shouldWithdrawWhenAccountExist() throws Exception {
        // given
        val dto = TestUtils.createOperationRequestDto(1L, 100L);
        val response = TestUtils.createOperationResponseDto(1L, 1000L, OperationType.WITHDRAWAL);
        val request = objectMapper.writeValueAsString(dto);
        // when
        when(service.performOperation(dto, OperationType.WITHDRAWAL)).thenReturn(response);
        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/operations/withdrawal")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));
    }

    @Test
    void shouldAccountCheckStatusWhenPassedValidAccount() throws Exception {
        // given
        val accountResponseDto = TestUtils.createAccountResponseDto(1L, 100L);

        //when
        when(service.checkStatus(1L)).thenReturn(accountResponseDto);
        val perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/operations/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }
}