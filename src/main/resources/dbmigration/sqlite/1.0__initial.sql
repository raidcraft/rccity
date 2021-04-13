-- apply changes
create table rccity_cities (
  id                            varchar(40) not null,
  name                          varchar(255),
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint uq_rccity_cities_name unique (name),
  constraint pk_rccity_cities primary key (id)
);

create table rccity_progress (
  id                            varchar(40) not null,
  city_id                       varchar(40),
  status                        varchar(10),
  required_value                float not null,
  value                         float not null,
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint ck_rccity_progress_status check ( status in ('ACTIVE','EXPIRED','SUCCESSFUL')),
  constraint pk_rccity_progress primary key (id),
  foreign key (city_id) references rccity_cities (id) on delete restrict on update restrict
);

create table rccity_contributions (
  id                            varchar(40) not null,
  progress_id                   varchar(40),
  resident_id                   varchar(40),
  source                        varchar(255),
  value                         float not null,
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rccity_contributions primary key (id),
  foreign key (progress_id) references rccity_progress (id) on delete restrict on update restrict,
  foreign key (resident_id) references rccity_residents (id) on delete restrict on update restrict
);

create table rccity_residents (
  id                            varchar(40) not null,
  name                          varchar(255),
  city_id                       varchar(40),
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rccity_residents primary key (id),
  foreign key (city_id) references rccity_cities (id) on delete restrict on update restrict
);

