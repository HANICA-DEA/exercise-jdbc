package nl.han.aim.oose.dea.datasource;

import java.sql.SQLException;

public class ItemUpdateException extends RuntimeException {
    public ItemUpdateException(String errorMessage, SQLException exc) {
        super(errorMessage, exc);
    }

    public ItemUpdateException(String errorMessage) {
        super(errorMessage);
    }
}
