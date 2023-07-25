use railway;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
  email_id int(21) NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (email_id)
);

DROP TABLE IF EXISTS travelplan;
CREATE TABLE travelplan (
  pid int(21) NOT NULL AUTO_INCREMENT,
  datetime DATETIME NOT NULL,
  description varchar(1000) NOT NULL,
  city varchar(60) NOT NULL,
  destination varchar(45) NOT NULL,
  url varchar(100),
  email_id int(60) NOT NULL,
  PRIMARY KEY (pid),
  FOREIGN KEY (email_id)
  REFERENCES user(email_id)
)