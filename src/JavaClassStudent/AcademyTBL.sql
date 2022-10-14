DROP DATABASE IF EXISTS StudentAcademyDB;
CREATE DATABASE IF NOT EXISTS StudentAcademyDB;
USE StudentAcademyDB;
-- -------------------------------------------------------------
-- Academy TABLE 생성
CREATE TABLE Academytbl(
NO char(10) not null,
NAME char(10) not null,
GENDER char(2)  not null,
DATEOFBIRTH char(10)  not null,
AGE int  not null,
PHONE char(13)  not null,
CONSTRAINT PK_Academy_NO PRIMARY KEY(NO)
);
-- DeleteAcademy Table
CREATE TABLE DeleteAcademytbl(
NO char(10) not null,
NAME char(10) not null,
GENDER char(2)  not null,
DATEOFBIRTH char(10)  not null,
AGE int  not null,
PHONE char(13)  not null,
DELETEDATE datetime
);
-- UpdateAcademy Table
CREATE TABLE UpdateAcademytbl(
NO char(10) not null,
NAME char(10) not null,
GENDER char(2)  not null,
DATEOFBIRTH char(10)  not null,
AGE int  not null,
PHONE char(13)  not null,
UPDATETIME datetime
);
-- -------------------------------------------------------------
-- TRIGGER
DROP TRIGGER IF EXISTS Trg_DeleteAcademytbl;
DELIMITER $$
CREATE TRIGGER Trg_DeleteAcademytbl
	AFTER DELETE
    ON Academytbl
    FOR EACH ROW
BEGIN
	INSERT INTO DeleteAcademytbl VALUES
(old.NO, old.NAME, old.GENDER, old.DATEOFBIRTH, old.AGE, old.PHONE, now());

END $$
DELIMITER ;

DROP TRIGGER IF EXISTS Trg_UpdateAcademytbl;
DELIMITER $$
CREATE TRIGGER Trg_UpdateAcademytbl
	AFTER update
    ON Academytbl
    FOR EACH ROW
BEGIN
	INSERT INTO UpdateAcademytbl VALUES
(old.NO, old.NAME, old.GENDER, old.DATEOFBIRTH, old.AGE, old.PHONE, now());

END $$
DELIMITER ;

SHOW TRIGGERS;
-- -------------------------------------------------------------
-- INDEX
CREATE INDEX IDX_Academytbl_NAME ON Academytbl(NAME);
-- -------------------------------------------------------------
-- PROCEDURE
DELIMITER $$
CREATE PROCEDURE ORDER_BY_DATA(IN IN_TYPE INT)
BEGIN
	CASE 
		WHEN (IN_TYPE = 1) THEN SELECT * FROM ACADEMYTBL ORDER BY NO ASC;
		WHEN (IN_TYPE = 2) THEN SELECT * FROM ACADEMYTBL ORDER BY NAME ASC;
	END CASE;
END $$
DELIMITER ;
-- ----------------------------------------------------------------
-- DML
INSERT INTO Academytbl VALUES('3120','강현모','남자','19840208',39,01055510208);
INSERT INTO Academytbl VALUES('2000','홍길동','남자','19840208',39,01055510208);
UPDATE Academytbl SET NAME = '홍길동', GENDER = '남자', 
DATEOFBIRTH = '16660101', AGE = 60, PHONE = "01022221111" WHERE NO = '3120';
UPDATE Academytbl SET NAME = '홍길동' WHERE NO = '2000';
DELETE FROM Academytbl WHERE NAME = '강현모';
delete from Academytbl where no = '3120';
DESCRIBE Academytbl;
SELECT * FROM Academytbl;
SELECT * FROM deleteAcademytbl;
-- ----------------------------------------------------------------
SET GLOBAL Log_bin_Trust_Function_Creators = 1;
DROP FUNCTION IF EXISTS GetAgeFunc;
DELIMITER $$
CREATE FUNCTION GetAgeFunc(AGEYear INT)
    RETURNS INT
BEGIN
    DECLARE AGE INT;
    SET AGE = YEAR(CURDATE()) - AGEYear;
    RETURN AGE;
END $$
DELIMITER ;

SELECT GetAgeFunc();
