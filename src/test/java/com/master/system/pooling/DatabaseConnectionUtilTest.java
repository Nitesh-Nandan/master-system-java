package com.master.system.pooling;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
class DatabaseConnectionUtilTest {

    @Test
    public void testConnection() throws SQLException {
        Connection conn = DatabaseConnectionUtil.getDatabaseConnection();
        Assertions.assertThat(conn).isNotNull();
        Assertions.assertThat(conn.isClosed()).isFalse();
        conn.close();
        Assertions.assertThat(conn.isClosed()).isTrue();
    }


    @Test
    public void testDbUtilGetDatabaseConnectionReturnNewConnectionEveryTime() throws SQLException {
        Connection[] connList = new Connection[5];
        for (int i = 0; i < 5; i++) {
            connList[i] = DatabaseConnectionUtil.getDatabaseConnection();
        }
        Assertions.assertThat(connList).doesNotHaveDuplicates();
    }
}
