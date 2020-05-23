INSERT INTO subject(id, name, credits, spec_id, year)
	VALUES ('MLR0003', 'FLP', 6, 1, 3),('MLR0004', 'FLP', 6, 2, 3),('MLR0005', 'FLP', 6, 3, 3),
	('MLR0006','SO',6,2,1),('MLR0007','SO',6,3,1);

INSERT INTO activity_type(name)
  VALUES ('Curs'),('Seminar'),('Laborator');

INSERT INTO sp_link(subject_id, type_id, user_id)
	VALUES ('MLR0004', 1, 'prof1'),('MLR0004', 2, 'prof1'),('MLR0004', 3, 'prof1'),
	('MLR0005', 1, 'prof2'),('MLR0005', 2, 'prof2'),('MLR0005', 3, 'prof2'),
	('MLR0003', 1, 'prof3'),('MLR0003', 2, 'prof3'),('MLR0003', 3, 'prof3');