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

 Date: 02/11/2022 19:10:53
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
  CONSTRAINT `t_admin_ibfk_1` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES (1, 'admin', '123456', '11323414', 2, '2131', 1001, '114383', '3578379415@qq.com');

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
  CONSTRAINT `t_answerer_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `t_answerer_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_answerer
-- ----------------------------
INSERT INTO `t_answerer` VALUES (1, '1', '1', NULL, NULL, NULL, 1004, NULL, 0, NULL, NULL);

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
  `createdBy` int(11) NULL DEFAULT NULL,
  `createdTime` datetime NULL DEFAULT NULL,
  `deleted` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_group
-- ----------------------------

-- ----------------------------
-- Table structure for t_group_answerer
-- ----------------------------
DROP TABLE IF EXISTS `t_group_answerer`;
CREATE TABLE `t_group_answerer`  (
  `groupId` int(11) NOT NULL,
  `answererId` int(11) NOT NULL,
  PRIMARY KEY (`groupId`, `answererId`) USING BTREE,
  INDEX `userId`(`answererId`) USING BTREE,
  CONSTRAINT `t_group_answerer_ibfk_2` FOREIGN KEY (`answererId`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `t_group_answerer_ibfk_3` FOREIGN KEY (`groupId`) REFERENCES `t_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_group_answerer
-- ----------------------------

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
  CONSTRAINT `t_lessee_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `t_lessee_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_lessee
-- ----------------------------
INSERT INTO `t_lessee` VALUES (1, '1', '1', NULL, NULL, NULL, 1002, NULL, 0, 1, NULL, NULL);
INSERT INTO `t_lessee` VALUES (2, 'admin1', '111111', NULL, NULL, NULL, 1002, NULL, 0, 1, NULL, NULL);
INSERT INTO `t_lessee` VALUES (3, 'admin2', '111111', NULL, NULL, NULL, 1002, NULL, 0, 1, NULL, NULL);

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
  CONSTRAINT `t_survey_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_survey
-- ----------------------------

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
  `limit` int(11) NULL DEFAULT 0,
  `inviteCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createdBy`(`createdBy`) USING BTREE,
  INDEX `securityQuestion`(`securityQuestion`) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`createdBy`) REFERENCES `t_lessee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`securityQuestion`) REFERENCES `t_security_question` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '1', '1', NULL, NULL, NULL, 1003, NULL, 0, NULL, NULL, 0, NULL);

SET FOREIGN_KEY_CHECKS = 1;
