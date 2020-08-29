insert into group_university (name,spec_id,year)
  values ('211',1,1),('212',1,1),('213',1,1),
      ('221',1,2),('222',1,2),('223',1,2),
      ('231',1,3),('232',1,3),('233',1,3);

INSERT INTO student(user_id, group_id)
	VALUES ('student1',1),('student3',1),
        ('student2',2);

insert into enrollment (student_id, subject_id) VALUES
    ('student1','MLR0001'),('student1','MLE0005'),
    ('student2','MLR0001'),('student2','MLE0005'),
    ('student1','MLR0002'),('student2','MLR0002'),
    ('student2','MLR0003'),('student2','MLE0004');
