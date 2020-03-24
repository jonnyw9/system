DROP  database if exists bookingSystem;

CREATE DATABASE bookingSystem;

USE bookingSystem;






DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS userPassword;
DROP TABLE IF EXISTS calendarUser;
DROP TABLE IF EXISTS bs_event;
DROP TABLE IF EXISTS eventUser;
DROP TABLE IF EXISTS calendar;
DROP TABLE IF EXISTS password;
DROP TABLE IF EXISTS bs_user;

CREATE TABLE password(
	passwordId INT(150) primary key NOT NULL,
	password VARCHAR(255)
);


CREATE TABLE bs_user(
	userId int(150) primary key NOT NULL,
	email varchar(100),
	firstName varchar(100),
	lastName varchar(100),
	userPassID int(150),
	calendarUserID int(150)
);


CREATE TABLE staff(
	staffId int(150) primary key NOT NULL,
	userId int(150) NOT NULL,
	room varchar(50),
    foreign key (userId) references bs_user(userId)
);


CREATE TABLE student(
	studentId int(150) primary key NOT NULL,
	userId int(150) NOT NULL,
	studentNumber varchar(9),
    foreign key (userId) references bs_user(userId)
);


CREATE TABLE userPassword(
	userPassID int(150) primary key NOT NULL,
	userID int(150) NOT NULL,
	passwordId int(150) NOT NULL,
    foreign key (userId) references bs_user(userId),
    foreign key (passwordId) references password(passwordId)
);


CREATE TABLE calendar(
	calendarID int(150) primary key NOT NULL,
	calendarUserID int(150) NOT NULL,
	dayStartTime TIME,
	dayEndTime TIME
);


CREATE TABLE calendarUser(
	calendarUserID int(150) primary key NOT NULL,
	calendarID int(150) NOT NULL,
	userID int(150) NOT NULL,
    foreign key (calendarId) references calendar(calendarID),
    foreign key (userId) references bs_user(userId)
);


CREATE TABLE bs_event(
    eventId int(150) primary key NOT NULL,
    event_start DATETIME NOT NULL,
    event_end DATETIME NOT NULL,
    title varchar(50),
    description varchar (250)
);


CREATE TABLE eventUser(
    eventUserId int(150) primary key NOT NULL,
    creatorUserId int(150) NOT NULL,
    recipientUserId int(150) NOT NULL,
    foreign key (creatorUserId) references bs_user(userId),
    foreign key (recipientUserId) references bs_user(userId)
);



ALTER TABLE bs_user ADD FOREIGN KEY (userPassID) REFERENCES userPassword(userPassID);
ALTER TABLE bs_user ADD FOREIGN KEY (calendarUserID) REFERENCES calendarUser(calendarUserID);

