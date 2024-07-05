package com.master.system.bloom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProductRepository {
    BlockingQueue<Connection> pools = new ArrayBlockingQueue<>(5);

    public ProductRepository() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/bloomcheck";
        String uname = "root";
        String pass = "password";

        for (int i = 0; i < 5; i++) {
            pools.add(DriverManager.getConnection(url, uname, pass));
        }
    }

    public List<Integer> fetchAllProductIds() throws SQLException {
        String query = "SELECT id, product_id FROM orders WHERE id > ? limit ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Integer> allProducts = new ArrayList<>();
        try {
            connection = pools.poll(1, TimeUnit.SECONDS);
            if (connection == null) {
                throw new SQLException("Failed to obtain a connection from the pool");
            }
            int id = 0;
            boolean hasRecord = true;
            while (hasRecord) {
                List<Integer> pids = new ArrayList<>();
                statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                statement.setInt(2, 5000);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    id = resultSet.getInt(1);
                    pids.add(resultSet.getInt(2));
                }
                if (pids.size() < 5000) {
                    hasRecord = false;
                }
                allProducts.addAll(pids);
            }
            return allProducts;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                pools.add(connection);
            }
        }
    }

    public boolean checkProductExistByProductId(int productId) throws SQLException {
        String query = "SELECT 1 FROM orders WHERE product_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = pools.poll(1, TimeUnit.SECONDS);
            if (connection == null) {
                throw new SQLException("Failed to obtain a connection from the pool");
            }

            statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                pools.add(connection);
            }
        }
    }
}
