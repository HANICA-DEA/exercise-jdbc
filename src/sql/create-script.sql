-- create database Items;
-- drop table items;
-- use Items;

create table Items (
     sku varchar(255) not null,
     category varchar(255) not null,
     title varchar(255),
     primary key (sku)
);