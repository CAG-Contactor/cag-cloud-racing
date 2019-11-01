package se.caglabs.cloudracing.lambda.registercontestant.exception;

public class UserDaoException extends Exception {

    public UserDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDaoException(String message) {
        super(message);
    }
}
