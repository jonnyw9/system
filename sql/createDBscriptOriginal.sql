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
	password_id INT(150) primary key NOT NULL,
	password VARCHAR(255)
);


CREATE TABLE bs_user(
	user_id int(150) primary key NOT NULL,
	email varchar(100),
	first_name varchar(100),
	last_name varchar(100),
	user_pass_id int(150),
	calendar_user_id int(150)
);


CREATE TABLE staff(
	staff_id int(150) primary key NOT NULL,
	user_id int(150) NOT NULL,
	room varchar(50),
    foreign key (user_id) references bs_user(user_id)
);


CREATE TABLE student(
	student_id int(150) primary key NOT NULL,
	user_id int(150) NOT NULL,
	student_number varchar(9),
    foreign key (user_id) references bs_user(user_id)
);


CREATE TABLE user_password(
	user_pass_id int(150) primary key NOT NULL,
	user_id int(150) NOT NULL,
	password_id int(150) NOT NULL,
    foreign key (user_id) references bs_user(user_id),
    foreign key (password_id) references password(password_id)
);


CREATE TABLE calendar(
	calendar_id int(150) primary key NOT NULL,
	calendar_user_id int(150) NOT NULL,
	day_start_time TIME,
	day_end_time TIME
);


CREATE TABLE calendar_user(
	calendar_user_id int(150) primary key NOT NULL,
	calendar_id int(150) NOT NULL,
	user_id int(150) NOT NULL,
    foreign key (calendar_id) references calendar(calendar_id),
    foreign key (user_id) references bs_user(user_id)
);


CREATE TABLE bs_event(
    event_id int(150) primary key NOT NULL,
    event_start DATETIME NOT NULL,
    event_end DATETIME NOT NULL,
    title varchar(50),
    description varchar (250)
);


CREATE TABLE event_user(
    event_user_id int(150) primary key NOT NULL,
	event_id int(150) NOT NULL,
    creator_user_id int(150) NOT NULL,
    recipient_user_id int(150) NOT NULL,
	foreign key (event_id) references bs_event(event_id),
    foreign key (creator_user_id) references bs_user(user_id),
    foreign key (recipient_user_id) references bs_user(user_id)
);

CREATE TABLE notification(
	notification_id int(150) auto_increment primary key NOT NULL,
    user_id int(150),
    title varchar(50),
    description varchar(250),
    action_link varchar(300),
    seen tinyint(1),
    created_on datetime,
    foreign key (user_id) references bs_user(user_id)
);

ALTER TABLE bs_user ADD FOREIGN KEY (user_pass_id) REFERENCES userPassword(user_pass_id);
ALTER TABLE bs_user ADD FOREIGN KEY (calendar_user_id) REFERENCES calendar_user(calendar_user_id);

