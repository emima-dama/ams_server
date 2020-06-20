CREATE TABLE student (
  user_id varchar(50) not null,
  group_id serial ,
  CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES group_university (group_id),
  constraint pk_student primary key (user_id, group_id)
);