create DATABASE travelapp;

use travelapp;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
  email_id int(21) NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) NOT NULL,
PRIMARY KEY (email_id)
);

DROP TABLE IF EXISTS travelplan;
CREATE TABLE travelplan (
  pid int(21) NOT NULL,
  datetime DATETIME NOT NULL,
  description varchar(800) NOT NULL,
  city varchar(60) NOT NULL,
  destination varchar(45) NOT NULL,
  email_id int(60) NOT NULL,
  PRIMARY KEY (pid),
  CONSTRAINT email_id 
  FOREIGN KEY (email_id)
 REFERENCES user(email_id)
)