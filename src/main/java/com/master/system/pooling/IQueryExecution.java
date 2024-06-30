package com.master.system.pooling;

import java.sql.Connection;
import java.sql.SQLException;

public interface IQueryExecution {
    Connection getConnection() throws InterruptedException, SQLException;

    void execute(String query) throws InterruptedException, SQLException;
}
