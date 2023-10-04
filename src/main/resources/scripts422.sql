create table car(
id serial primary key,
brand varchar(30),
model varchar(30),
price numeric(20,2)
)

create table car_owner(
id serial primary key,
name varchar(50) not null,
age serial,
has_license boolean default false,
car_id serial references car(id)
)