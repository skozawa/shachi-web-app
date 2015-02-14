DROP TABLE IF EXISTS `annotator`;
CREATE TABLE `annotator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `mail` text NOT NULL,
  `organization` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `language`;
CREATE TABLE `language` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(3) NOT NULL,
  `name` varchar(100) NOT NULL,
  `area` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `metadata`;
CREATE TABLE `metadata` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `label` varchar(100) NOT NULL,
  `order_num` int NOT NULL,
  `shown` boolean NOT NULL DEFAULT true,
  `multi_value` boolean NOT NULL,
  `input_type` int NOT NULL,
  `value_type` varchar(100) NOT NULL, -- metadata_value.value_type
  `color` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  KEY `idx_shown_order` (`shown`, `order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `metadata_value`;
CREATE TABLE `metadata_value` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `value_type` varchar(100) NOT NULL, -- metadata.value_type
  `value` varchar(100) NOT NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY `idx_type_value` (`value_type`, `value`),
  KEY `idx_value` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shachi_id` varchar(20) NOT NULL,
  `title` text NOT NULL,
  `is_public` boolean NOT NULL DEFAULT true,
  `annotator_id` bigint NOT NULL DEFAULT 1,
  `status` int NOT NULL DEFAULT 1,
  `created` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modified` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `idx_shachi_id` (`shachi_id`),
  KEY `idx_public_created` (`is_public`, `created`),
  KEY `idx_created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `resources_metadata`;
CREATE TABLE `resources_metadata` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resource_id` bigint NOT NULL,
  `metadata_id` bigint NOT NULL,
  `language_id` bigint NOT NULL,
  `value_id` bigint NOT NULL DEFAULT 0, -- metadata_value_id or language_id
  `content` text,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `idx_resource` (`resource_id`),
  KEY `idx_metadata_value` (`metadata_id`, `value_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `title_list`;
CREATE TABLE `title_list` (
  `resource_id` bigint NOT NULL,
  `title` text NOT NULL,
  `mid` varchar(3) NOT NULL,
  `common_title` text NOT NULL,
  `candidate1` text NOT NULL,
  `candidate2` text NOT NULL,
  `candidate3` text NOT NULL,
  KEY `idx_resource` (`resource_id`),
  KEY `idx_mid` (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
