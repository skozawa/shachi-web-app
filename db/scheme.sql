DROP TABLE IF EXISTS `annotators`;
CREATE TABLE `annotators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `mail` text NOT NULL,
  `organization` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
