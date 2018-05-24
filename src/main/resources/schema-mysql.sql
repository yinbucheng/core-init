------------修改时间:2018-05-24 14:05:05-----------------
create database if not exists test2;
create table if not exists test2.t_person(
id bigint  primary key auto_increment,
name varchar(255) ,
age int ,
id_card varchar(255) 
);
