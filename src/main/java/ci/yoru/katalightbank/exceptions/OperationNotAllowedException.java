package ci.yoru.katalightbank.exceptions;

public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException() {
        super("OPERATION_NOT_ALLOWED");
    }
}
