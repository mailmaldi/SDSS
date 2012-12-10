CREATE TABLE SDSSAuth(
id  INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
emailid CHAR(200) UNIQUE,
userid CHAR(20) UNIQUE DEFAULT NULL,
password VARCHAR(200),
salt VARCHAR(200),
timestamp BIGINT,
metadata VARCHAR(200) default "{}"
) ENGINE=InnoDb DEFAULT CHARSET=utf8 MAX_ROWS = 1000000000;

INSERT INTO SDSSAuth (emailid,userid,password) values ('milindp1@umbc.edu','milindp1','KMWpb7m9JuSsesw0+Tlin5P/HKM=');

--grant all privileges on *.* to 'test'@'%' identified by 'test';

CREATE TABLE `sdss_run125_objects` (  
`basePath` varchar(100) DEFAULT NULL,  
`imageName` varchar(100) DEFAULT NULL,  
`ojbId` INT DEFAULT NULL,  
`rightAscension` double DEFAULT NULL,  
`declension` double DEFAULT NULL,  
`majorAxis` double DEFAULT NULL,  
`minorAxis` double DEFAULT NULL,  
`eccentricity` double DEFAULT NULL,  
`theta` double DEFAULT NULL,  
`solidAngle` INT DEFAULT NULL,  
`count` INT NOT NULL,  
`numPixels` INT NOT NULL,  
`magnitude` double NOT NULL,  
`constellation` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
