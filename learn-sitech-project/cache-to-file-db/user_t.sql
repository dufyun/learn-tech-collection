/*
初始化 用户表
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_t
-- ----------------------------
DROP TABLE IF EXISTS `user_t`;
CREATE TABLE `user_t` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL,
  `age` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `ssm`.`user_t` (`id`, `user_name`, `password`, `age`) VALUES ('1', '测试', 'sfasgfaf', '24');
INSERT INTO `ssm`.`user_t` (`id`, `user_name`, `password`, `age`) VALUES ('2', '测试11', 'sfasgfaf', '24');
INSERT INTO `ssm`.`user_t` (`id`, `user_name`, `password`, `age`) VALUES ('3', 'dufy', '123456', '25');
INSERT INTO `ssm`.`user_t` (`id`, `user_name`, `password`, `age`) VALUES ('4', 'github', '123456', '10');
