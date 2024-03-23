create sequence if not exists
    USER_SEQ
as bigint
start with 1
increment by 50;

create sequence if not exists
    TASK_SEQ
as bigint
start with 1
increment by 50;

CREATE TABLE IF NOT EXISTS test (
    id bigint auto_increment primary key,
    name varchar(255)
);

CREATE TABLE IF NOT EXISTS "User" (
    id bigint primary key,
    name varchar(255),
    email varchar(255) unique not null,
    password varchar(255) not null,
    roles varchar(255) array
);

CREATE TABLE IF NOT EXISTS Task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    description VARCHAR(1200),
    finished BOOLEAN DEFAULT FALSE,
    user_id BIGINT REFERENCES "User"(id),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modifiedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);