use dbmiddle;

drop table if exists datasource;

CREATE TABLE datasource
(
  id INT(11) NOT NULL AUTO_INCREMENT,
  tablename VARCHAR(40) not null,
  poolname VARCHAR(40) not null,
  dbtype VARCHAR(40) not null,
  PRIMARY KEY (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT INTO datasource VALUES ('','user','user','MYSQL');
INSERT INTO datasource VALUES ('','user','user2','Oracle');
INSERT INTO datasource VALUES ('','user','user3','postgreSQL');