package ci.yoru.katalightbank.exceptions;

public class ClientAccountNotFoundException extends RuntimeException {
    public ClientAccountNotFoundException() {
        super("ACCOUNT_NOT_FOUND");
    }
}
