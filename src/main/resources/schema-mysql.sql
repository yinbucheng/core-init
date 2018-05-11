create database if not EXISTS db1;
create table if not EXISTS db1.t_user(
id bigint primary key auto_increment,
name varchar(255),
age int
);

create table if not EXISTS db1.t_address(
id bigint primary key auto_increment,
name varchar(255),
code varchar(255),
detail varchar(255)
)