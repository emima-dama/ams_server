insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('3', 'prof1','$2y$12$cMigrouaTCn8NLiAVWg5Ouh3b72jHn/UgO8vajtHqDsZ6EeGAt386', 'prof1@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('4', 'prof2', '$2y$12$TjrMJ3V8ZMBoFxsPsKn8AO/SDWffw9bCHcqvOxQJpnF9CyAopmGZO','prof2@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('5', 'prof3', '$2y$12$noIHb6Bxcf/Gmnv7efDxUemfqB8bK1o2SbXCh0BDmp1KygQvov8z6','prof3@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('6', 'student1', '$2y$12$SZ64Inx6EAd/Rnoaca1AaOpd65h9ROgD20iTy/CmAE3j2pyP.RR/e','student1@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('7', 'student2', '$2y$12$fmNrMWylDxpnknKBy30WXexWeYOggt9ex9Vv2k0uwmAOC7clFzpLK','student2@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('8', 'student3', '$2y$12$c3V2.AHJbnaXmgMuWNCuj.z/D/ZAlkn3yFicFl2bX.Hhqol/iEM/O','student3@yahoo.com', '1', '1', '1', '1');
insert into oauth_user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('9', 'student4', '$2y$12$kN5SlKGWsxcg9SbM/sjA4.pJ3eHoHifRxiuPLYrQ86XvxhJbDTKJW','student4@yahoo.com', '1', '1', '1', '1');

INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
    VALUES
    /* PROFESSOR */
    (1, 3),
    (1, 4),
    (1, 5),
    /* STUDENT */
    (2, 6),
    (2, 7),
    (2, 8),
    (2, 9);
