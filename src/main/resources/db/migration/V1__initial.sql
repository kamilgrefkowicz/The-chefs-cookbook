create table ingredient
(
    id                    bigint not null auto_increment,
    uuid                  varchar(255),
    version               bigint,
    amount                decimal(19, 2),
    child_item_id         bigint,
    recipe_parent_item_id bigint,
    primary key (id)
) engine = InnoDB;
create table item
(
    id             bigint not null auto_increment,
    uuid           varchar(255),
    version        bigint,
    name           varchar(255),
    type           varchar(255),
    unit           varchar(255),
    user_entity_id bigint not null,
    primary key (id)
) engine = InnoDB;
create table menu
(
    id             bigint not null auto_increment,
    uuid           varchar(255),
    version        bigint,
    name           varchar(255),
    user_entity_id bigint not null,
    primary key (id)
) engine = InnoDB;
create table menu_items
(
    menu_id bigint not null,
    item_id bigint not null,
    primary key (menu_id, item_id)
) engine = InnoDB;
create table recipe
(
    description    varchar(1000),
    recipe_yield   decimal(19, 2),
    uuid           varchar(255),
    version        bigint,
    parent_item_id bigint not null,
    primary key (parent_item_id)
) engine = InnoDB;
create table users
(
    id       bigint not null auto_increment,
    uuid     varchar(255),
    version  bigint,
    password varchar(255),
    username varchar(255),
    primary key (id)
) engine = InnoDB;
alter table ingredient
    add constraint FK44t2bsi2mi8kxx04xkvyx4qkt foreign key (child_item_id) references item (id);
alter table ingredient
    add constraint FKc8ixuljs0vdo6ps7it4sgywuy foreign key (recipe_parent_item_id) references recipe (parent_item_id);
alter table item
    add constraint FKg3x466xvdklb90mfnwihq8vi foreign key (user_entity_id) references users (id);
alter table menu
    add constraint FKoyc48km7xt5kge953tr39eh9n foreign key (user_entity_id) references users (id);
alter table menu_items
    add constraint FKn0ndqh9rjxa37rylxwy5ym1lx foreign key (item_id) references item (id);
alter table menu_items
    add constraint FKh788p5mv4cmsg53s1ynvibkua foreign key (menu_id) references menu (id);
alter table recipe
    add constraint FKo69mmke59uesnh10wcbmpl2fu foreign key (parent_item_id) references item (id);
