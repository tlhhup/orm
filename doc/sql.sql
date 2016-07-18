create database if not EXISTS v1;
use v1;
create table if not EXISTS users(id int PRIMARY key auto_increment,username varchar(20),`password` varchar(32),address VARCHAR(100),tel varchar(11));
create table if not EXISTS students(id varchar(64),score DECIMAL(5,2),name varchar(20));