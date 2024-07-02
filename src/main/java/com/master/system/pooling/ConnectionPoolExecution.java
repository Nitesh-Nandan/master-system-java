package com.master.system.pooling;

import com.master.system.ds.queue.BlockingQueue;
import com.master.system.ds.queue.SimpleBlockingQueueImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionPoolExecution implements IQueryExecution {
    private static final int DEFAULT_POOL_SIZE = 10;
    private final BlockingQueue<Connection> connectionPool;

    public ConnectionPoolExecution(int poolSize) throws SQLException, InterruptedException {
        connectionPool = new SimpleBlockingQueueImpl<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            connectionPool.add(DatabaseConnectionUtil.getDatabaseConnection());
        }
    }

    public ConnectionPoolExecution() throws SQLException, InterruptedException {
        this(DEFAULT_POOL_SIZE);
    }

    @Override
    public Connection getConnection() throws InterruptedException {
        return connectionPool.get();
    }

    @Override
    public void closeConnection(Connection connection) throws InterruptedException {
        connectionPool.add(connection);
    }

    @Override
    public void execute(String query) throws InterruptedException, SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println("successfully executed query by pool");
        }
        resultSet.close();
        statement.close();
        closeConnection(conn);
    }
}
