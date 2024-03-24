package ci.yoru.katalightbank.exceptions;

public class ClientAccountNotFoundException extends RuntimeException {
    public ClientAccountNotFoundException() {
        super("ACCOUNT_NOT_FOUND");
    }

    @Override
    public String getMessage() {
        return "Operation non permis";
    }
}
