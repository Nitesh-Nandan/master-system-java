package com.master.system.pooling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RawConnectionExecution implements IQueryExecution {
    @Override
    public Connection getConnection() throws InterruptedException, SQLException {
        return DatabaseConnectionUtil.getDatabaseConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void execute(String query) throws InterruptedException, SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println("successfully executed query");
        }
        resultSet.close();
        statement.close();
        closeConnection(conn);
    }
}
