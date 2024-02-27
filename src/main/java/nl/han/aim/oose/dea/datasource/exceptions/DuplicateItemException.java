package nl.han.aim.oose.dea.datasource.exceptions;

import java.sql.SQLException;

public class DuplicateItemException extends RuntimeException {
    public DuplicateItemException(String s, SQLException e) {
        super(s, e);
    }
}
