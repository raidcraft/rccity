-- apply changes
create table rccity_cities (
  id                            uuid not null,
  name                          varchar(255),
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint uq_rccity_cities_name unique (name),
  constraint pk_rccity_cities primary key (id)
);

create table rccity_progress (
  id                            uuid not null,
  city_id                       uuid,
  status                        varchar(10),
  required_value                float not null,
  value                         float not null,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint ck_rccity_progress_status check ( status in ('ACTIVE','EXPIRED','SUCCESSFUL')),
  constraint pk_rccity_progress primary key (id)
);

create table rccity_contributions (
  id                            uuid not null,
  progress_id                   uuid,
  resident_id                   uuid,
  source                        varchar(255),
  value                         float not null,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rccity_contributions primary key (id)
);

create table rccity_residents (
  id                            uuid not null,
  name                          varchar(255),
  city_id                       uuid,
  version                       bigint not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rccity_residents primary key (id)
);

create index ix_rccity_progress_city_id on rccity_progress (city_id);
alter table rccity_progress add constraint fk_rccity_progress_city_id foreign key (city_id) references rccity_cities (id) on delete restrict on update restrict;

create index ix_rccity_contributions_progress_id on rccity_contributions (progress_id);
alter table rccity_contributions add constraint fk_rccity_contributions_progress_id foreign key (progress_id) references rccity_progress (id) on delete restrict on update restrict;

create index ix_rccity_contributions_resident_id on rccity_contributions (resident_id);
alter table rccity_contributions add constraint fk_rccity_contributions_resident_id foreign key (resident_id) references rccity_residents (id) on delete restrict on update restrict;

create index ix_rccity_residents_city_id on rccity_residents (city_id);
alter table rccity_residents add constraint fk_rccity_residents_city_id foreign key (city_id) references rccity_cities (id) on delete restrict on update restrict;

