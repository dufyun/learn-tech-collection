DROP TABLE IF EXISTS `user_t`;  
  
CREATE TABLE `user_t` (  
  `id` int(11) NOT NULL AUTO_INCREMENT,  
  `user_name` varchar(40) NOT NULL,  
  `password` varchar(255) NOT NULL,  
  `age` int(4) NOT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;  
  
/*Data for the table `user_t` */  
  
insert  into `user_t`(`id`,`user_name`,`password`,`age`) values (1,'测试','sfasgfaf',24)