DROP  database if exists bookingSystem;

CREATE DATABASE bookingSystem;

USE bookingSystem;

DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS bs_event;
DROP TABLE IF EXISTS calendar;
DROP TABLE IF EXISTS password;
DROP TABLE IF EXISTS bs_user;

CREATE TABLE password(
	password_id INT(150) auto_increment primary key NOT NULL,
	password VARCHAR(255)
);


CREATE TABLE bs_user(
	user_id int(150) auto_increment primary key NOT NULL,
	email varchar(100) NOT NULL,
	first_name varchar(100) NOT NULL,
	last_name varchar(100) NOT NULL,
	calendar_id int(150),
	role_id int(150),
    staff_id int(150),
    student_id int(150),
    password_id int(150),
    foreign key (password_id) references password(password_id)
);


CREATE TABLE staff(
	staff_id int(150) auto_increment primary key NOT NULL,
	room varchar(50)
);

CREATE TABLE calendar(
	calendar_id int(150) auto_increment primary key NOT NULL,
	day_start_time TIME,
	day_end_time TIME
);


CREATE TABLE bs_event(
    event_id int(150) auto_increment primary key NOT NULL,
    event_start DATETIME,
    event_end DATETIME,
    title varchar(50),
    description varchar (250),
	accepted tinyint(1),
    creator_user_id int(150),
    recipient_user_id int(150),
    location varchar(250),
    foreign key (creator_user_id) references bs_user(user_id),
    foreign key (recipient_user_id) references bs_user(user_id)
);

CREATE TABLE roles(
	role_id int(150) auto_increment primary key NOT NULL,
    staff tinyint(1),
    student tinyint(1)
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


ALTER TABLE bs_user ADD FOREIGN KEY (calendar_id) REFERENCES calendar(calendar_id);
ALTER TABLE bs_user ADD FOREIGN KEY (role_id) REFERENCES roles(role_id);
ALTER TABLE bs_user ADD FOREIGN KEY (staff_id) REFERENCES staff(staff_id);
ALTER TABLE bs_user ADD FOREIGN KEY (student_id) REFERENCES student(student_id);


