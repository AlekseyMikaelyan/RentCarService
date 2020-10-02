create table brands(
    id bigint not null generated always as identity primary key,
    brand_name varchar(30) not null
);

create table car_status(
    id bigint not null generated always as identity primary key,
    status varchar(30) not null
);

create table car_types(
    id bigint not null generated always as identity primary key,
    body_type varchar(30) not null
);

create table engine_types(
    id bigint not null generated always as identity primary key,
    type varchar(30) not null,
    engine_capacity varchar(30) not null
);

create table clients(
    id bigint not null generated always as identity primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    phone_number varchar(60) not null,
    email varchar(60) not null,
    password varchar(60) not null
);

create table cars(
    id bigint not null generated always as identity primary key,
    brand_id bigint not null,
    engine_type_id bigint not null,
    car_type_id bigint not null,
    model varchar(60) not null,
    transmission varchar(60) not null,
    year_of_manufacture varchar(60) not null,
    car_status_id bigint not null,
    price varchar(60) not null,
    constraint car_brand_fk foreign key(brand_id) references brands (id),
    constraint car_engine_fk foreign key(engine_type_id) references engine_types (id),
    constraint car_type_fk foreign key(car_type_id) references car_types (id),
    constraint car_status_fk foreign key(car_status_id) references car_status (id)
);

create table orders(
    id bigint not null generated always as identity primary key,
    client_id bigint not null,
    car_id bigint not null,
    start_order date not null,
    end_order date not null,
    constraint order_client_fk foreign key (client_id) references clients (id),
    constraint order_car_fk foreign key (car_id) references cars (id)
);

insert into brands (brand_name) values ('Renault');
insert into brands (brand_name) values ('Peugeot');
insert into brands (brand_name) values ('Mercedes');
insert into brands (brand_name) values ('Audi');
insert into brands (brand_name) values ('BMW');
insert into brands (brand_name) values ('KIA');
insert into brands (brand_name) values ('Volkswagen');
insert into brands (brand_name) values ('Hyundai');

insert into car_status (status) values ('Under repair');
insert into car_status (status) values ('Available');
insert into car_status (status) values ('Unavailable');

insert into car_types(body_type) values ('Sedan');
insert into car_types(body_type) values ('Hatchback');
insert into car_types(body_type) values ('Minivan');
insert into car_types(body_type) values ('Coupe');
insert into car_types(body_type) values ('SUV');

insert into engine_types (type, engine_capacity) values ('Gasoline', '1.2');
insert into engine_types (type, engine_capacity) values ('Gasoline', '1.4');
insert into engine_types (type, engine_capacity) values ('Gasoline', '1.6');
insert into engine_types (type, engine_capacity) values ('Gasoline', '2.0');
insert into engine_types (type, engine_capacity) values ('Gasoline', '2.5');
insert into engine_types (type, engine_capacity) values ('Gasoline', '3.0');
insert into engine_types (type, engine_capacity) values ('Diesel', '1.5');
insert into engine_types (type, engine_capacity) values ('Diesel', '2.0');
insert into engine_types (type, engine_capacity) values ('Diesel', '2.5');

insert into clients (first_name, last_name, phone_number, email, password)
values ('Aleksey', 'Alekseev', '+380936435199', 'aleks@gmail.com', 'password1');
insert into clients (first_name, last_name, phone_number, email, password)
values ('Sergey', 'Sergeev', '+380990964311', 'serg@gmail.com', 'password2');

values (1, 1, 2, 'Sandero', 'Manual', 2015, 2, '25 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (1, 8, 1, 'Megan', 'Automatic', 2016, 2, '35 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (1, 3, 2, 'Megan', 'Automatic', 2016, 2, '30 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (2, 2, 2, '206', 'Manual', 2010, 2, '25 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (2, 4, 2, '307', 'Automatic', 2017, 2, '30 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (2, 1, 5, '2008', 'Automatic', 2017, 2, '40 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (3, 8, 1, 'C-class', 'Automatic', 2016, 2, '50 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (4, 8, 5, 'Q5', 'Automatic', 2014, 2, '55 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (5, 9, 5, 'X3', 'Automatic', 2015, 2, '50 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (5, 6, 4, 'Z4', 'Automatic', 2015, 2, '60 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (6, 3, 2, 'Ceed', 'Manual', 2017, 2, '30 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (6, 4, 1, 'Optima', 'Automatic', 2018, 2, '40 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (7, 2, 2, 'Polo', 'Manual', 2013, 2, '25 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (7, 9, 5, 'Touareg', 'Manual', 2013, 2, '60 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (8, 3, 2, 'i30', 'Manual', 2016, 2, '35 euro');
insert into cars (brand_id, engine_type_id, car_type_id, model, transmission, year_of_manufacture, car_status_id, price)
values (8, 7, 1, 'Elantra', 'Automatic', 2017, 2, '35 euro');

insert into orders (client_id, car_id, start_order, end_order) values (1, 6, '2020-09-17', '2020-09-20');
insert into orders (client_id, car_id, start_order, end_order) values (2, 3, '2020-09-21', '2020-09-25');