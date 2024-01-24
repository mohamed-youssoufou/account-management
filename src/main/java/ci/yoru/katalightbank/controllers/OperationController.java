package ci.yoru.katalightbank.controllers;


import ci.yoru.katalightbank.controllers.dto.AccountResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationRequestDto;
import ci.yoru.katalightbank.controllers.dto.OperationResponseDto;
import ci.yoru.katalightbank.controllers.dto.OperationType;
import ci.yoru.katalightbank.services.OperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/operations", produces = {"application/json"})
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping(value = "/deposit", consumes = {"application/json"})
    public ResponseEntity<OperationResponseDto> deposit(@RequestBody OperationRequestDto dto) {
        return ResponseEntity.ok(operationService.performOperation(dto, OperationType.DEPOSIT));
    }

    @PostMapping(value = "/withdrawal", consumes = {"application/json"})
    public ResponseEntity<OperationResponseDto> withdraw(@RequestBody OperationRequestDto dto) {
        return ResponseEntity.ok(operationService.performOperation(dto, OperationType.WITHDRAWAL));
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<AccountResponseDto> checkStatus(@PathVariable long id) {
        return ResponseEntity.ok(operationService.checkStatus(id));
    }

}
