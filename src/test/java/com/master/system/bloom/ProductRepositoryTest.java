package com.master.system.bloom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ProductRepositoryTest {

    private ProductRepository productRepository;

//    @BeforeEach
    void setup() throws SQLException {
        productRepository = new ProductRepository();
    }

//    @Test
    void testRecordExist() throws SQLException {
        Assertions.assertThat(Boolean.TRUE).isEqualTo(
                productRepository.checkProductExistByProductId(8807131));
    }

//    @Test
    void testRecordDoesNotExist() throws SQLException {
        Assertions.assertThat(Boolean.FALSE).isEqualTo(
                productRepository.checkProductExistByProductId(1));
    }

//    @Test
    void testFetchAllRecords() throws SQLException {
        List<Integer> products = productRepository.fetchAllProductIds();
        System.out.println(products.size());
    }
}