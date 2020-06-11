create table group_university (
  group_id serial,
  name     varchar(32) not null,
  spec_id  serial     not null,
  year     serial      not null,
  CONSTRAINT uq_name_spec_id unique (name, spec_id),
  constraint pk_group primary key (group_id)
);

create table enrollment (
  student_id varchar(50) not null, -- username for oauth_users table
  subject_id varchar(50) not null,
  constraint pk_enrollments primary key (student_id, subject_id)
);