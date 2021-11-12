/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2021-10-28 16:41:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_biz_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_biz_log`;
CREATE TABLE `sys_biz_log` (
  `id` varchar(64) NOT NULL,
  `app_name` varchar(30) DEFAULT NULL,
  `module` varchar(50) DEFAULT NULL,
  `scene_code` varchar(100) DEFAULT NULL,
  `scene_name` varchar(100) DEFAULT NULL,
  `biz_no` varchar(64) DEFAULT NULL,
  `method_event` varchar(20) DEFAULT NULL,
  `class_name` varchar(100) DEFAULT NULL,
  `method` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `request_ip` varchar(50) DEFAULT NULL,
  `params` text,
  `error_message` varchar(100) DEFAULT NULL,
  `success` tinyint(1) DEFAULT NULL,
  `result` text,
  `user_no` varchar(64) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `duration` bigint(20) DEFAULT NULL,
  `begin_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `invoke_before` text,
  `invoke_after` text,
  `trace_id` varchar(255) DEFAULT NULL,
  `span_id` varchar(255) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `biz_change_items` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
