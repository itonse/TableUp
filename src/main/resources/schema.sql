
-- auto-generated definition
CREATE TABLE IF NOT EXISTS PARTNERSHIP
(
    ID          BIGINT auto_increment primary key,
    EMAIL       VARCHAR(100),
    PASSWORD    VARCHAR(100),
    OWNER_NAME  VARCHAR(100),
    PHONE       VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS RESTAURANT
(
    ID                         BIGINT auto_increment primary key,
    RESTAURANT_NAME            VARCHAR(100),
    RESTAURANT_LOCATION        VARCHAR(100),
    RESTAURANT_DESCRIPTION     VARCHAR(255),
    STAR                       DECIMAL(3,2),

    constraint FK_RESTAURANT_PARTNERSHIP_ID foreign key(PARTNERSHIP_ID) references PARTNERSHIP(ID)
);

CREATE TABLE IF NOT EXISTS MEMBER
(
    ID          BIGINT auto_increment primary key,
    EMAIL       VARCHAR(100),
    PASSWORD    VARCHAR(100),
    USER_NAME   VARCHAR(100),
    PHONE       VARCHAR(100)
)


