DROP SCHEMA IF EXISTS `eshop` ;
CREATE SCHEMA IF NOT EXISTS `eshop`;
USE `eshop` ;

drop table eshop.order;
drop table eshop.line_items;

create table if not exists eshop.customers(
	name varchar(32) not null,
    address varchar(128) not null,
    email varchar(128) not null,
    
    primary key(name)
);

-- (1:M) rs
-- foreign key constraint should be written in the MANY table
-- FK field/column is non-unique and references the ONE table's PK
create table if not exists eshop.order(
	orderId varchar(128) not null,
    deliveryId varchar(128),
    name varchar(32) not null,
    address varchar(128) not null,
    email varchar(128) not null,
    status varchar(128),
    orderDate date,

    
    primary key (orderId)
    
);

    -- private String orderId;
	-- private String deliveryId;
	-- private String name;
	-- private String address;
	-- private String email;
	-- private String status;
	-- private Date orderDate = new Date();
	-- private List<LineItem> lineItems = new LinkedList<>();

create table if not exists eshop.line_items(

    item varchar(128) not null,
    quantity int not null,
    orderId varchar(128) not null,

    primary key (item),

    constraint fk_orderId
		foreign key(orderId) references eshop.order(orderId)
);

create table if not exists eshop.order_status(
    order_id varchar(128) not null,
    delivery_id varchar(128) not null,
    status enum('pending', 'dispatched'),
    status_update timestamp,

    primary key (delivery_id)
);

    -- private String item;
	-- private Integer quantity
    


-- if column has been specified, must have matching number of values args
-- key in null as value if required
insert into customers(name, address, email) values('fred','201 Cobblestone Lane','fredflintstone@bedrock.com');
insert into customers(name, address, email) values('sherlock','221B Baker Street, London','sherlock@consultingdetective.org');
insert into customers(name, address, email) values('spongebob','124 Conch Street, Bikini Bottom','spongebob@yahoo.com');
insert into customers(name, address, email) values('jessica','698 Candlewood Land, Cabot Cove','fletcher@gmail.com');
insert into customers(name, address, email) values('dursley','4 Privet Drive, Little Whinging, Surrey','dursley@gmail.com');

-- drop user 'fred'@'%';
-- create user 'fred'@'%' identified by 'fred';
-- grant all privileges on eshop.* to 'fred'@'%';
-- flush privileges;

