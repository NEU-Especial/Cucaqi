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

 Date: 03/11/2022 18:58:56
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
INSERT INTO `t_admin` VALUES (1, 'admin', '123456', '11323414', 2, '2131', 1001, '114383', '3578379415@qq.com');
INSERT INTO `t_admin` VALUES (2, '2', '22', '2222', 1, '213', 1001, '111345', '5615621');
INSERT INTO `t_admin` VALUES (3, '3', '33', '3333', 1, '312', 1001, '1112', '41854141');
INSERT INTO `t_admin` VALUES (4, '4', '44', '4444', 2, '123', 1001, '11143', '89652965');
INSERT INTO `t_admin` VALUES (5, '5', '55', '5555', 3, '569', 1001, '11145', '59652');
INSERT INTO `t_admin` VALUES (6, '6', '66', '6666', 3, '48541', 1001, '11165', '9652541785');
INSERT INTO `t_admin` VALUES (7, '7', '77', '7777', 3, '841', 1001, '1122', '85418541');
INSERT INTO `t_admin` VALUES (8, '8', '88', '8888', 2, '8541', 1001, '1113', '59641');
INSERT INTO `t_admin` VALUES (9, '9', '99', '9999', 1, '87541', 1001, '1212', '84124185');
INSERT INTO `t_admin` VALUES (10, '10', '1010', '101010', 1, '596', 1001, '2345', '485415');
INSERT INTO `t_admin` VALUES (11, '11', '1111', '111111', 2, '452', 1001, '3525', '986596');
INSERT INTO `t_admin` VALUES (12, '12', '1212', '121212', 3, '56296', 1001, '2523', '856452141');

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
INSERT INTO `t_answerer` VALUES (12, '1', '123456', '13940131469', NULL, NULL, 1004, 0, 0, 1, 'www204143090@163.com');
INSERT INTO `t_answerer` VALUES (13, '骆桩波', '56+5', '13940131469', NULL, NULL, 1004, 0, 0, 1, 'www204143090@163.com');

-- ----------------------------
-- Table structure for t_answerer_survey
-- ----------------------------
DROP TABLE IF EXISTS `t_answerer_survey`;
CREATE TABLE `t_answerer_survey`  (
  `surveyId` int(11) NOT NULL,
  `answererId` int(11) NOT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `answer` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`surveyId`, `answererId`) USING BTREE,
  INDEX `answerId`(`answererId`) USING BTREE,
  CONSTRAINT `answerId` FOREIGN KEY (`answererId`) REFERENCES `t_answerer` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `surveyId` FOREIGN KEY (`surveyId`) REFERENCES `t_survey` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_answerer_survey
-- ----------------------------

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
INSERT INTO `t_group` VALUES (3, 'c', '呃呃', 1, '2022-11-02 20:37:18', 0);
INSERT INTO `t_group` VALUES (4, 'd', '塔塔', 1, '2022-11-02 20:37:21', 0);
INSERT INTO `t_group` VALUES (5, 'e', '已有', 1, '2022-11-02 20:37:24', 0);
INSERT INTO `t_group` VALUES (6, 'f', '与i', 1, '2022-11-02 20:37:27', 0);
INSERT INTO `t_group` VALUES (7, 'ggg', '库类', 1, '2022-11-02 20:37:29', 1);
INSERT INTO `t_group` VALUES (8, 'h', '和广泛的', 3, '2022-11-02 20:37:32', 0);
INSERT INTO `t_group` VALUES (9, 'i', '肺结核', 3, '2022-11-02 20:37:35', 0);
INSERT INTO `t_group` VALUES (10, 'j', '复活甲', 4, '2022-11-02 20:37:37', 0);
INSERT INTO `t_group` VALUES (11, 'k', '吗v你', 4, '2022-11-02 20:37:41', 0);
INSERT INTO `t_group` VALUES (12, 'l', '美女吧v', 2, '2022-11-02 20:37:43', 0);
INSERT INTO `t_group` VALUES (13, 'f', '多个', 1, '2022-11-03 04:00:54', 1);
INSERT INTO `t_group` VALUES (14, 'f', '萨法', 1, '2022-11-03 04:01:08', 1);
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
INSERT INTO `t_group_answerer` VALUES (3, 1);
INSERT INTO `t_group_answerer` VALUES (2, 2);
INSERT INTO `t_group_answerer` VALUES (6, 2);
INSERT INTO `t_group_answerer` VALUES (2, 3);
INSERT INTO `t_group_answerer` VALUES (1, 4);
INSERT INTO `t_group_answerer` VALUES (3, 4);
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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_lessee
-- ----------------------------
INSERT INTO `t_lessee` VALUES (1, '1', '1', '1111', 1, '5486', 1002, NULL, 0, 1, '14', '51515');
INSERT INTO `t_lessee` VALUES (2, 'admin1', '111111', '2222', 2, '789', 1002, NULL, 0, 1, '124', '5642');
INSERT INTO `t_lessee` VALUES (3, 'admin2', '111111', '3333', 1, '8956', 1002, NULL, 0, 1, '1342', '54638');
INSERT INTO `t_lessee` VALUES (4, '4d', '444d', '4444', 1, '875', 1002, NULL, 0, 2, '2414', '873278');
INSERT INTO `t_lessee` VALUES (5, '5e', '555e', '5555', 1, '2356', 1002, NULL, 0, 2, '144', '38378');
INSERT INTO `t_lessee` VALUES (6, '6f', '666f', '6666', 3, '78', 1002, NULL, 0, 2, '1341', '832785');
INSERT INTO `t_lessee` VALUES (7, '7g', '777g', '7777', 2, '85', 1002, NULL, 0, 3, '134', '3878');
INSERT INTO `t_lessee` VALUES (8, '8h', '888h', '8888', 3, '5689', 1002, NULL, 0, 3, '342423', '8378935');
INSERT INTO `t_lessee` VALUES (9, '9i', '999i', '9999', 2, '856', 1002, NULL, 0, 3, '324', '7328');
INSERT INTO `t_lessee` VALUES (10, '10k', '101010k', '101010', 2, '7856', 1002, NULL, 0, 4, '2342', '7245');
INSERT INTO `t_lessee` VALUES (11, '11l', '111111l', '111111', 3, '78956', 1002, NULL, 0, 4, '1412', '37373');
INSERT INTO `t_lessee` VALUES (12, '12m', '121212m', '121212', 1, NULL, 1002, NULL, 0, 4, '124342', '5278578');

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
  `id` int(11) NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `createdBy` int(11) NULL DEFAULT NULL,
  `startTime` datetime NULL DEFAULT NULL,
  `endTime` datetime NULL DEFAULT NULL,
  `limitCount` int(11) NULL DEFAULT NULL,
  `curCount` int(11) NULL DEFAULT NULL,
  `isPublic` tinyint(1) NULL DEFAULT NULL,
  `style` int(11) NULL DEFAULT NULL,
  `isRecommon` tinyint(1) NULL DEFAULT NULL,
  `state` int(11) NULL DEFAULT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  CONSTRAINT `t_survey_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_survey
-- ----------------------------
INSERT INTO `t_survey` VALUES (1, 'aa', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_survey` VALUES (2, 'bb', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_survey` VALUES (3, 'cc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

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
INSERT INTO `t_user` VALUES (1, '11111', '111111', NULL, 1, '121234', 1003, NULL, 0, 1, '23454', 0, '32545', NULL);
INSERT INTO `t_user` VALUES (2, '2', '2', '22', 2, '14', 1003, NULL, 0, 1, '3245345', 0, '3452', NULL);
INSERT INTO `t_user` VALUES (3, '3', '3', '33', 3, '44', 1003, NULL, 0, 2, '345543', 0, '6443', NULL);
INSERT INTO `t_user` VALUES (4, '4', '4', '44', 1, '421', 1003, NULL, 0, 2, '214', 0, '6456', NULL);
INSERT INTO `t_user` VALUES (5, '5', '5', '55', 3, '42423', 1003, NULL, 0, 3, '234', 0, '43', NULL);
INSERT INTO `t_user` VALUES (6, '6', '6', '66', 2, '23234', 1003, NULL, 0, 3, '53453', 0, '345', NULL);
INSERT INTO `t_user` VALUES (7, '7', '7', '77', 2, '43', 1003, NULL, 0, 4, '34545', 0, '345', NULL);
INSERT INTO `t_user` VALUES (8, '8', '8', '88', 1, '342', 1003, NULL, 0, 4, '34534', 0, '34', NULL);
INSERT INTO `t_user` VALUES (9, '9', '9', '99', 3, '34dsf', 1003, NULL, 0, 1, '34535', 0, '5', NULL);
INSERT INTO `t_user` VALUES (10, '10', '10', '1010', 2, '42', 1003, NULL, 0, 1, '5', 0, '4', NULL);
INSERT INTO `t_user` VALUES (11, '11', '11', '1111', 3, '2342', 1003, NULL, 0, 1, '45345', 0, '3', NULL);
INSERT INTO `t_user` VALUES (12, '12', '12', '1212', 1, '234', 1003, NULL, 0, 2, '5345', 0, '34534', NULL);

SET FOREIGN_KEY_CHECKS = 1;
