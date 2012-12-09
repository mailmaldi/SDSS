CREATE TABLE SDSSAuth(
id  INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
emailid CHAR(200) UNIQUE,
userid CHAR(20) UNIQUE DEFAULT NULL,
password VARCHAR(200),
salt VARCHAR(200),
timestamp BIGINT,
metadata VARCHAR(200) default "{}"
) ENGINE=InnoDb DEFAULT CHARSET=utf8 MAX_ROWS = 1000000000;

--grant all privileges on *.* to 'test'@'%' identified by 'test';