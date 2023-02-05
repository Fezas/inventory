/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.exception;

public class DaoException extends RuntimeException {
    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
