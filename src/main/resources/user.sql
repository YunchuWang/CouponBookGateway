CREATE TABLE User (
    name varchar(64) NOT NULL COMMENT 'User name',
    password varchar(1024) NOT NULL COMMENT 'Password',
    user_role varchar(256)  NOT NULL COMMENT 'role',
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;