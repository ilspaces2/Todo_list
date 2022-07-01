create table categories
(
    id       SERIAL PRIMARY KEY,
    categoryName VARCHAR NOT NULL
);
create table items_categories
(
    item_id int NOT NULL,
    categories_id int NOT NULL
);

insert into categories (categoryName) values ('Финансы');
insert into categories (categoryName) values ('Срочно');
insert into categories (categoryName) values ('Не срочно');
insert into categories (categoryName) values ('Продукты');
insert into categories (categoryName) values ('Важно');