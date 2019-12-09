package se.caglabs.cloudracing.common.persistence.registereduser.exception;

public class UserDaoException extends Exception {

    public UserDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDaoException(String message) {
        super(message);
    }
}
