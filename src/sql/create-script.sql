-- create database Items;
-- use Items;

create table Items (
                       sku varchar(255) not null,
                       category varchar(255),
                       title varchar(255),
                       primary key (sku)
);

-- drop table items;