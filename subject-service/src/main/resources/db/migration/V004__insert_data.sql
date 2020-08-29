INSERT INTO subject_code (code, subject_name) VALUES
  (1,'FLP'),(2,'SO'),(3,'MAP'),(4,'FLP'),(5,'SO'),(6,'MAP');

INSERT INTO subject(id, name, credits, spec_id, year)
	VALUES ('MLR0001', 'FLP', 6, 1, 3),('MLR0002', 'SO', 6, 1, 1),('MLR0003', 'MAP', 6, 1, 2),
	('MLE0004','FLP',5,2,3),('MLE0005','SO',6,2,1),('MLE0006','MAP',6,2,1);

INSERT INTO activity_type(name)
  VALUES ('Curs'),('Seminar'),('Laborator');

INSERT INTO sp_link(subject_id, type_id, user_id)
	VALUES ('MLR0001', 1, 'prof1'),('MLR0001', 2, 'prof1'),('MLR0001', 3, 'prof1'),
	('MLR0002', 1, 'prof2'),('MLR0002', 2, 'prof1'),('MLR0002', 3, 'prof2'),
	('MLR0003', 1, 'prof3'),('MLR0003', 2, 'prof3'),('MLR0003', 3, 'prof3'),
	('MLE0004', 1, 'prof2'),('MLE0004', 2, 'prof3'),('MLE0004', 3, 'prof1'),
	('MLE0005', 1, 'prof3'),('MLE0005', 2, 'prof1'),('MLE0005', 3, 'prof3'),
	('MLE0006', 1, 'prof2'),('MLE0006', 2, 'prof2'),('MLE0006', 3, 'prof2');