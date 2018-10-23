/*
 Navicat Premium Data Transfer

 Source Server         : 53
 Source Server Type    : MySQL
 Source Server Version : 50545
 Source Host           : 192.168.10.53:3306
 Source Schema         : ntest

 Target Server Type    : MySQL
 Target Server Version : 50545
 File Encoding         : 65001

 Date: 23/10/2018 17:51:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter`  (
  `novelId` int(11) NOT NULL,
  `chapterId` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`chapterId`, `novelId`) USING BTREE,
  INDEX `index_novelId`(`novelId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of chapter
-- ----------------------------
INSERT INTO `chapter` VALUES (1, 1, '沙漠中的彼岸花');
INSERT INTO `chapter` VALUES (1, 2, '后文明时代');

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `recentUpdateTime` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of novel
-- ----------------------------
INSERT INTO `novel` VALUES (1, '圣墟', '辰东', '在破败中崛起，在寂灭中复苏', 1540176442);
INSERT INTO `novel` VALUES (2, '三寸人间', '耳根', '星空古剑，万族进化，缥缈道院，谁与争锋天下万物，神兵不朽，宇宙苍穹，太虚称尊青木年华，悠悠牧之', 1540287502);

SET FOREIGN_KEY_CHECKS = 1;
