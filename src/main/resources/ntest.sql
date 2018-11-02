/*
Navicat MySQL Data Transfer

Source Server         : 53
Source Server Version : 50545
Source Host           : 192.168.10.53:3306
Source Database       : ntest

Target Server Type    : MYSQL
Target Server Version : 50545
File Encoding         : 65001

Date: 2018-11-01 18:15:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `novelId` int(11) NOT NULL,
  `chapterId` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `source` varchar(11) DEFAULT NULL COMMENT '目标url',
  PRIMARY KEY (`chapterId`,`novelId`) USING BTREE,
  KEY `index_novelId` (`novelId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for novel
-- ----------------------------
DROP TABLE IF EXISTS `novel`;
CREATE TABLE `novel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `author` varchar(255) NOT NULL DEFAULT '',
  `introduction` varchar(255) NOT NULL DEFAULT '' COMMENT '简介',
  `recentChapterUpdateId` int(11) NOT NULL DEFAULT '0' COMMENT '最近更新章节id',
  `sourceId` int(11) NOT NULL COMMENT '源id',
  `tagid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=397891 DEFAULT CHARSET=utf8;
