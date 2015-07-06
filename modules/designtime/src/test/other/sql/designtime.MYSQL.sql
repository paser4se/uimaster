#---------------------------------------
-- Create SQL Generated               --
--                                    --
-- Please DO NOT modify !!            --
-- Generated on 2014-07-28 08:47:58   --
#---------------------------------------

USE <database name>;

-- FOLDER
CREATE TABLE FOLDER
(id BIGINT(10) NOT NULL AUTO_INCREMENT,
 parentId INT(10),
 NAME VARCHAR(30),
 AUTHOR VARCHAR(255),
 PRIMARY KEY(id)
);

-- FILE
CREATE TABLE FILE
(id BIGINT(10) NOT NULL AUTO_INCREMENT,
 NAME VARCHAR(30),
 AUTHOR VARCHAR(255),
 TYPE INT(2),
 OWNER BIGINT(10),
 SIZE BIGINT(38),
 _version INT(2),
 _starttime TIMESTAMP,
 _endtime TIMESTAMP,
 _optuserid BIGINT(10),
 PRIMARY KEY(id)
);

-- USER
CREATE TABLE USER
(ID BIGINT(38),
 NAME VARCHAR(255),
 PRIMARY KEY(ID)
);

-- ADMINISTRATOR
CREATE TABLE ADMINISTRATOR
(FIELD0 VARCHAR(255));

-- GUEST
CREATE TABLE GUEST
(FIELD0 VARCHAR(255),
 FIELD1 VARCHAR(255));

-- FOLDER
CREATE TABLE FOLDER
(ID BIGINT(38),
 NAME VARCHAR(255),
 AUTHOR VARCHAR(255),
 FILES BIGINT(38),
 FOLDERS BIGINT(38),
 _enable INT(2));

-- FILE
CREATE TABLE FILE
(ID BIGINT(38),
 NAME VARCHAR(255),
 AUTHOR VARCHAR(255),
 TYPE INT(2),
 OWNER BIGINT(38),
 id BIGINT(38),
 SIZE BIGINT(38),
 FILES BIGINT(38),
 MODIFIED DATETIME,
 _enable INT(2));

-- USER
CREATE TABLE USER
(ID BIGINT(38),
 NAME VARCHAR(255),
 _enable INT(2));

-- ADMINISTRATOR
CREATE TABLE ADMINISTRATOR
(FIELD0 VARCHAR(255));

-- GUEST
CREATE TABLE GUEST
(FIELD0 VARCHAR(255),
 FIELD1 VARCHAR(255));

-- FOLDER
CREATE TABLE FOLDER
(id BIGINT(10) NOT NULL AUTO_INCREMENT,
 parentId INT(10),
 NAME VARCHAR(30),
 AUTHOR VARCHAR(255),
 PRIMARY KEY(id)
);

-- FILE
CREATE TABLE FILE
(id BIGINT(10) NOT NULL AUTO_INCREMENT,
 NAME VARCHAR(30),
 AUTHOR VARCHAR(255),
 TYPE INT(2),
 OWNER BIGINT(10),
 SIZE BIGINT(38),
 _version INT(2),
 _starttime TIMESTAMP,
 _endtime TIMESTAMP,
 _optuserid BIGINT(10),
 PRIMARY KEY(id)
);

-- USER
CREATE TABLE USER
(ID BIGINT(38),
 NAME VARCHAR(255),
 PRIMARY KEY(ID)
);

-- ADMINISTRATOR
CREATE TABLE ADMINISTRATOR
(FIELD0 VARCHAR(255));

-- GUEST
CREATE TABLE GUEST
(FIELD0 VARCHAR(255),
 FIELD1 VARCHAR(255));

