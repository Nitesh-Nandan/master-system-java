CREATE DATABASE IF NOT EXISTS bloomcheck;

DROP TABLE IF EXISTS bloomcheck.`orders`;

CREATE TABLE `orders` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int unsigned NOT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

--table_config = {
--    'table_name': 'orders',
--    'database': 'bloomcheck',
--    'rows': 5,
--    'columns': ['product_id', 'price'],
--    'values': [lambda: faker.random_int(min=100, max=10000000),
--               lambda: faker.pyfloat(left_digits=3, right_digits=2, positive=True)]
--}