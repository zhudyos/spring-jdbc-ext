package io.zhudy.spring.jdbc;

/**
 * @author Kevin Zou (kevinz@weghst.com)
 */
public class PageQueryException extends RuntimeException {

    /**
     *
     * @param message
     * @param cause
     */
    public PageQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
