/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : cucaqi

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 17/11/2022 14:14:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `securityQuestion` int(11) NULL DEFAULT NULL,
  `securityAnswer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT 1001,
  `inviteCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `securityQuestion`(`securityQuestion`) USING BTREE,
  CONSTRAINT `t_admin_ibfk_1` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES (1, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (2, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (3, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (4, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (5, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (6, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (7, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (8, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (9, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (10, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (11, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');
INSERT INTO `t_admin` VALUES (12, 'admin', '123456', '11323414', 2, '2131', 1001, '356931', 'shimaring@qq.com');

-- ----------------------------
-- Table structure for t_answerer
-- ----------------------------
DROP TABLE IF EXISTS `t_answerer`;
CREATE TABLE `t_answerer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `securityQuestion` int(11) NULL DEFAULT NULL,
  `securityAnswer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT 1004,
  `payment` float NULL DEFAULT NULL,
  `deleted` int(11) UNSIGNED NULL DEFAULT 0,
  `createdBy` int(11) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  INDEX `securityQuestion`(`securityQuestion`) USING BTREE,
  CONSTRAINT `t_answerer_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_answerer_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_answerer
-- ----------------------------
INSERT INTO `t_answerer` VALUES (1, '123', '11', '584158', 1, '5278', 1004, NULL, 1, 1, '465');
INSERT INTO `t_answerer` VALUES (2, '2', '22', '527', 2, '52', 1004, NULL, 0, 1, '653');
INSERT INTO `t_answerer` VALUES (3, '3', '33', '4752', 1, '725', 1004, NULL, 0, 2, '467');
INSERT INTO `t_answerer` VALUES (4, '4', '44', '277852', 1, '7524', 1004, NULL, 0, 2, '8659');
INSERT INTO `t_answerer` VALUES (5, '5', '55', '45275', 2, '578', 1004, NULL, 0, 2, '87956');
INSERT INTO `t_answerer` VALUES (6, '6', '66', '78575', 3, '837', 1004, NULL, 0, 3, '6958');
INSERT INTO `t_answerer` VALUES (7, '7', '77', '27425', 3, '827527', 1004, NULL, 0, 3, '5967');
INSERT INTO `t_answerer` VALUES (8, '8', '88', '78257857', 3, '87373', 1004, NULL, 0, 3, '69587');
INSERT INTO `t_answerer` VALUES (9, '9', '99', '875785', 1, '2378327', 1004, NULL, 0, 4, '698');
INSERT INTO `t_answerer` VALUES (10, '10', '1010', '7857852', 2, '837373', 1004, NULL, 0, 4, '546');
INSERT INTO `t_answerer` VALUES (11, '11', '1111', '85', 2, '4525', 1004, NULL, 0, 4, NULL);
INSERT INTO `t_answerer` VALUES (12, 'admin', '123456', '13940131469', NULL, NULL, 1004, 0, 0, 1, 'www204143090@163.com');
INSERT INTO `t_answerer` VALUES (13, '骆桩波', '56+5', '13940131469', NULL, NULL, 1004, 0, 0, 1, 'www204143090@163.com');

-- ----------------------------
-- Table structure for t_answerer_survey
-- ----------------------------
DROP TABLE IF EXISTS `t_answerer_survey`;
CREATE TABLE `t_answerer_survey`  (
  `surveyId` int(11) NOT NULL,
  `answererId` int(11) NULL DEFAULT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `answer` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  INDEX `answerId`(`answererId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_answerer_survey
-- ----------------------------
INSERT INTO `t_answerer_survey` VALUES (4, 12, '2022-11-16 20:39:00', '{\"问题1\":\"item1\"}');
INSERT INTO `t_answerer_survey` VALUES (15, 4, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (15, 5, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (15, 7, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (15, 12, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (15, 2, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (15, 3, '2022-11-17 12:59:36', NULL);
INSERT INTO `t_answerer_survey` VALUES (14, 2, '2022-11-17 12:59:50', NULL);
INSERT INTO `t_answerer_survey` VALUES (14, 12, '2022-11-17 12:59:50', '{\"问题1\":\"item2\"}');
INSERT INTO `t_answerer_survey` VALUES (14, 13, '2022-11-17 12:59:50', NULL);

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createdBy` int(11) NULL DEFAULT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_group
-- ----------------------------
INSERT INTO `t_group` VALUES (1, 'a', '啊啊啊', 1, '2022-11-02 20:37:12', 0);
INSERT INTO `t_group` VALUES (2, 'b', '啊啊', 1, '2022-11-02 20:37:14', 0);
INSERT INTO `t_group` VALUES (3, 'c', '呃呃', 1, '2022-11-02 20:37:18', 1);
INSERT INTO `t_group` VALUES (4, 'd', '塔塔', 1, '2022-11-02 20:37:21', 0);
INSERT INTO `t_group` VALUES (5, 'e', '已有', 1, '2022-11-02 20:37:24', 0);
INSERT INTO `t_group` VALUES (6, 'f', '与i', 1, '2022-11-02 20:37:27', 0);
INSERT INTO `t_group` VALUES (7, 'ggg', '库类', 1, '2022-11-02 20:37:29', 0);
INSERT INTO `t_group` VALUES (8, 'h', '和广泛的', 3, '2022-11-02 20:37:32', 0);
INSERT INTO `t_group` VALUES (9, 'i', '肺结核', 3, '2022-11-02 20:37:35', 0);
INSERT INTO `t_group` VALUES (10, 'j', '复活甲', 4, '2022-11-02 20:37:37', 0);
INSERT INTO `t_group` VALUES (11, 'k', '吗v你', 4, '2022-11-02 20:37:41', 0);
INSERT INTO `t_group` VALUES (12, 'l', '美女吧v', 2, '2022-11-02 20:37:43', 0);
INSERT INTO `t_group` VALUES (13, 'f', '多个', 1, '2022-11-03 04:00:54', 0);
INSERT INTO `t_group` VALUES (14, 'f', '萨法', 1, '2022-11-03 04:01:08', 0);
INSERT INTO `t_group` VALUES (15, NULL, NULL, 11, NULL, 0);

-- ----------------------------
-- Table structure for t_group_answerer
-- ----------------------------
DROP TABLE IF EXISTS `t_group_answerer`;
CREATE TABLE `t_group_answerer`  (
  `groupId` int(11) NOT NULL,
  `answererId` int(11) NOT NULL,
  PRIMARY KEY (`groupId`, `answererId`) USING BTREE,
  INDEX `userId`(`answererId`) USING BTREE,
  CONSTRAINT `t_group_answerer_ibfk_2` FOREIGN KEY (`answererId`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_group_answerer_ibfk_3` FOREIGN KEY (`groupId`) REFERENCES `t_group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_group_answerer
-- ----------------------------
INSERT INTO `t_group_answerer` VALUES (2, 2);
INSERT INTO `t_group_answerer` VALUES (2, 3);
INSERT INTO `t_group_answerer` VALUES (1, 4);
INSERT INTO `t_group_answerer` VALUES (1, 5);
INSERT INTO `t_group_answerer` VALUES (1, 7);
INSERT INTO `t_group_answerer` VALUES (1, 12);

-- ----------------------------
-- Table structure for t_group_user
-- ----------------------------
DROP TABLE IF EXISTS `t_group_user`;
CREATE TABLE `t_group_user`  (
  `groupId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`groupId`, `userId`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  CONSTRAINT `t_group_user_ibfk_1` FOREIGN KEY (`groupId`) REFERENCES `t_group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_group_user_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_group_user
-- ----------------------------
INSERT INTO `t_group_user` VALUES (1, 1);
INSERT INTO `t_group_user` VALUES (2, 2);
INSERT INTO `t_group_user` VALUES (4, 2);
INSERT INTO `t_group_user` VALUES (3, 3);

-- ----------------------------
-- Table structure for t_lessee
-- ----------------------------
DROP TABLE IF EXISTS `t_lessee`;
CREATE TABLE `t_lessee`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `securityQuestion` int(11) NULL DEFAULT NULL,
  `securityAnswer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT 1002,
  `payment` float NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT 0,
  `createdBy` int(11) NULL DEFAULT NULL,
  `inviteCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  INDEX `securityQuestion`(`securityQuestion`) USING BTREE,
  CONSTRAINT `t_lessee_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_lessee_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_lessee
-- ----------------------------
INSERT INTO `t_lessee` VALUES (1, 'test25', '1234588', '1111', 1, '123', 1002, 5.2, 0, 1, '8872599', '');
INSERT INTO `t_lessee` VALUES (2, 'test1', '123456', '2222', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (3, 'test2', '123456', '3333', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (4, 'test3', '123456', '4444', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (5, 'test4', '123456', '5555', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (6, 'test5', '123456', '6666', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (7, 'test6', '123456', '7777', 1, '123', 1002, 6, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (8, 'test7', '123456', '8888', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (9, 'test8', '123456', '9999', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (10, 'test9', '123456', '101010', 1, '123', 1002, 0, 1, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (11, 'test0', '123456', '111111', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (12, 'test11', '123456', '121212', 1, '123', 1002, 0, 0, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (13, 'test12', '123456', NULL, 1, '123', 1002, 0, 1, 1, '887253', '');
INSERT INTO `t_lessee` VALUES (15, 'test13', '111111', NULL, 1, '123', 1002, 0, 1, 1, NULL, '1150840779@qq.com');
INSERT INTO `t_lessee` VALUES (16, 'a', 'a', 'aa', NULL, NULL, 1002, 0, 0, 1, 'a', 'aa');

-- ----------------------------
-- Table structure for t_security_question
-- ----------------------------
DROP TABLE IF EXISTS `t_security_question`;
CREATE TABLE `t_security_question`  (
  `id` int(11) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_security_question
-- ----------------------------
INSERT INTO `t_security_question` VALUES (1, '问题一');
INSERT INTO `t_security_question` VALUES (2, '问题二');
INSERT INTO `t_security_question` VALUES (3, '问题三');

-- ----------------------------
-- Table structure for t_survey
-- ----------------------------
DROP TABLE IF EXISTS `t_survey`;
CREATE TABLE `t_survey`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `createdBy` int(11) NULL DEFAULT NULL,
  `startTime` datetime NULL DEFAULT NULL,
  `endTime` datetime NULL DEFAULT NULL,
  `limitCount` int(11) NULL DEFAULT NULL,
  `curCount` int(11) NULL DEFAULT 0,
  `isPublic` tinyint(1) NULL DEFAULT NULL,
  `style` int(11) NULL DEFAULT NULL,
  `state` int(11) NULL DEFAULT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT 0,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  CONSTRAINT `t_survey_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_survey
-- ----------------------------
INSERT INTO `t_survey` VALUES (1, 'aa', 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, 0, 'aa');
INSERT INTO `t_survey` VALUES (2, 'bb', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `t_survey` VALUES (3, 'cc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL);
INSERT INTO `t_survey` VALUES (4, '{\"title\":\"eq\",\"logoPosition\":\"right\",\"pages\":[{\"name\":\"页面1\",\"elements\":[{\"type\":\"radiogroup\",\"name\":\"问题1\",\"choices\":[\"item1\",\"item2\",\"item3\"]}]}]}', 1, '2022-11-08 16:00:00', '2022-11-30 16:00:00', 4, 4, 1, NULL, 3, '2022-11-30 16:00:00', 1, 'eq');
INSERT INTO `t_survey` VALUES (5, '{\"title\":\"point\",\"logoPosition\":\"right\",\"pages\":[{\"name\":\"页面1\",\"elements\":[{\"type\":\"rating\",\"name\":\"问题1\"}]}]}', 1, '2022-11-01 16:00:00', '2022-12-20 16:00:00', 0, 5, 0, NULL, 3, '2022-11-16 16:33:56', 1, 'point');
INSERT INTO `t_survey` VALUES (14, '{\"title\":\"eq\",\"logoPosition\":\"right\",\"pages\":[{\"name\":\"页面1\",\"elements\":[{\"type\":\"radiogroup\",\"name\":\"问题1\",\"choices\":[\"item1\",\"item2\",\"item3\"]}]}]}', 1, '2022-11-08 16:00:00', '2022-11-30 16:00:00', 4, 1, 1, NULL, 1, '2022-11-30 16:00:00', 0, 'eq');
INSERT INTO `t_survey` VALUES (15, '{\"title\":\"point\",\"logoPosition\":\"right\",\"pages\":[{\"name\":\"页面1\",\"elements\":[{\"type\":\"rating\",\"name\":\"问题1\"}]}]}', 1, '2022-11-01 16:00:00', '2022-12-20 16:00:00', 0, 0, 0, NULL, 0, '2022-11-16 16:33:56', 0, 'point');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `securityQuestion` int(11) NULL DEFAULT NULL,
  `securityAnswer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT 1003,
  `payment` float NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT 0,
  `createdBy` int(11) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `limitCount` int(11) NULL DEFAULT 0,
  `inviteCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `curCount` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  INDEX `securityQuestion`(`securityQuestion`) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_lessee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'admin', '123456', NULL, 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (2, 'admin1', '123456', '22', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (3, 'admin2', '123456', '33', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (4, 'admin3', '123456', '44', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (5, 'admin4', '123456', '55', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (6, 'admin5', '123456', '66', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (7, 'admin6', '123456', '77', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (8, 'admin7', '123456', '88', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (9, 'admin8', '123456', '99', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (10, 'admin9', '123456', '1010', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (11, 'admin0', '123456', '1111', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);
INSERT INTO `t_user` VALUES (12, 'admin00', '123456', '1212', 1, '121234', 1003, NULL, 0, 1, '23454', 0, '498584', NULL);

SET FOREIGN_KEY_CHECKS = 1;
