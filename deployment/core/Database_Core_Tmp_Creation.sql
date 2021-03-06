--
-- ER/Studio 8.0 SQL Code Generation
-- Project :      DATA MODEL
--
-- Date Created : Monday, April 21, 2014 13:52:40
-- Target DBMS : MySQL 5.x
--

-- 
-- TABLE: T_ALLOWED_ACCESS_COMPANY 
--

CREATE TABLE T_ALLOWED_ACCESS_COMPANY(
    USER_OID       DECIMAL(38, 0)    NOT NULL,
    COMPANY_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (COMPANY_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_BUYER_STORE_AREA_USER 
--

CREATE TABLE T_BUYER_STORE_AREA_USER(
    USER_OID    DECIMAL(38, 0)    NOT NULL,
    AREA_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, AREA_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_BUYER_STORE_USER 
--

CREATE TABLE T_BUYER_STORE_USER(
    STORE_OID    DECIMAL(38, 0)    NOT NULL,
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (STORE_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_GROUP 
--

CREATE TABLE T_GROUP(
    GROUP_OID              DECIMAL(38, 0)    NOT NULL,
    GROUP_ID               VARCHAR(20)       NOT NULL,
    GROUP_NAME             VARCHAR(100)      NOT NULL,
    GROUP_TYPE             ENUM('BUYER', 'SUPPLIER'),
    IGNORE_NOTIFICATION    BOOLEAN           NOT NULL,
    CREATE_DATE            DATETIME          NOT NULL,
    CREATE_BY              VARCHAR(50)       NOT NULL,
    UPDATE_DATE            DATETIME,
    UPDATE_BY              VARCHAR(50),
    USER_TYPE_OID          DECIMAL(38, 0)    NOT NULL,
    BUYER_OID              DECIMAL(38, 0),
    SUPPLIER_OID           DECIMAL(38, 0),
    ACTOR                  VARCHAR(50)       NOT NULL,
    ACTION_TYPE            ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    ACTION_DATE            DATETIME          NOT NULL,
    CTRL_STATUS            ENUM('APPROVED', 'PENDING', 'REJECTED', 'WITHDRAWN') NOT NULL,
    PRIMARY KEY (GROUP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_GROUP_SUPPLIER 
--

CREATE TABLE T_GROUP_SUPPLIER(
    GROUP_OID       DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (GROUP_OID, SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_GROUP_TRADING_PARTNER 
--

CREATE TABLE T_GROUP_TRADING_PARTNER(
    GROUP_OID    DECIMAL(38, 0)    NOT NULL,
    TP_OID       DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (GROUP_OID, TP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_GROUP_USER 
--

CREATE TABLE T_GROUP_USER(
    GROUP_OID           DECIMAL(38, 0)    NOT NULL,
    USER_OID            DECIMAL(38, 0)    NOT NULL,
    LAST_UPDATE_FROM    ENUM('GROUP', 'USER') NOT NULL,
    APPROVED            BOOLEAN           NOT NULL,
    ACTION_TYPE         ENUM('CREATE', 'DELETE') NOT NULL,
    PRIMARY KEY (GROUP_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_ROLE 
--

CREATE TABLE T_ROLE(
    ROLE_OID                 DECIMAL(38, 0)    NOT NULL,
    ROLE_ID                  VARCHAR(20)       NOT NULL,
    ROLE_NAME                VARCHAR(50)       NOT NULL,
    ROLE_TYPE                ENUM('ADMIN', 'BUYER', 'SUPPLIER') NOT NULL,
    CREATE_DATE              DATETIME          NOT NULL,
    CREATE_BY                VARCHAR(50)       NOT NULL,
    UPDATE_DATE              DATETIME,
    UPDATE_BY                VARCHAR(50),
    CREATED_FROM_SYSADMIN    BOOLEAN           NOT NULL,
    USER_TYPE_OID            DECIMAL(38, 0)    NOT NULL,
    BUYER_OID                DECIMAL(38, 0),
    ACTOR                    VARCHAR(50)       NOT NULL,
    ACTION_TYPE              ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    ACTION_DATE              DATETIME          NOT NULL,
    CTRL_STATUS              ENUM('APPROVED', 'PENDING', 'REJECTED', 'WITHDRAWN') NOT NULL,
    PRIMARY KEY (ROLE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_ROLE_GROUP 
--

CREATE TABLE T_ROLE_GROUP(
    ROLE_OID     DECIMAL(38, 0)    NOT NULL,
    GROUP_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (ROLE_OID, GROUP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_ROLE_OPERATION 
--

CREATE TABLE T_ROLE_OPERATION(
    ROLE_OID    DECIMAL(38, 0)    NOT NULL,
    OPN_ID      VARCHAR(20)       NOT NULL,
    PRIMARY KEY (ROLE_OID, OPN_ID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_ROLE_USER 
--

CREATE TABLE T_ROLE_USER(
    ROLE_OID    DECIMAL(38, 0)    NOT NULL,
    USER_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (ROLE_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_SUPPLIER_ROLE 
--

CREATE TABLE T_SUPPLIER_ROLE(
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    ROLE_OID        DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (SUPPLIER_OID, ROLE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_USER_CLASS 
--

CREATE TABLE T_USER_CLASS(
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    CLASS_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, CLASS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_USER_PROFILE 
--

CREATE TABLE T_USER_PROFILE(
    USER_OID               DECIMAL(38, 0)    NOT NULL,
    USER_NAME              VARCHAR(50)       NOT NULL,
    SALUTATION             VARCHAR(20),
    LOGIN_ID               VARCHAR(50)       NOT NULL,
    LOGIN_PWD              CHAR(128),
    LOGIN_MODE             ENUM('PASSWORD','AD') NOT NULL,
    GENDER                 CHAR(1),
    TEL                    VARCHAR(30),
    MOBILE                 VARCHAR(30),
    FAX                    VARCHAR(30),
    EMAIL                  VARCHAR(100),
    FAIL_ATTEMPTS          INT               DEFAULT 0,
    LAST_RESET_PWD_DATE    DATETIME,
    LAST_LOGIN_DATE        DATETIME,
    ACTIVE                 BOOLEAN           NOT NULL,
    BLOCKED                BOOLEAN           NOT NULL,
    BLOCK_REMARKS          VARCHAR(100),
    INIT                   BOOLEAN           NOT NULL,
    CREATE_DATE            DATETIME          NOT NULL,
    CREATE_BY              VARCHAR(50)       NOT NULL,
    UPDATE_DATE            DATETIME,
    UPDATE_BY              VARCHAR(50),
    BLOCK_DATE             DATETIME,
    BLOCK_BY               VARCHAR(50),
    INACTIVATE_DATE        DATETIME,
    INACTIVATE_BY          VARCHAR(50),
    USER_TYPE              DECIMAL(38, 0)    NOT NULL,
    BUYER_OID              DECIMAL(38, 0),
    SUPPLIER_OID           DECIMAL(38, 0),
    ACTOR                  VARCHAR(50)       NOT NULL,
    ACTION_TYPE            ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    ACTION_DATE            DATETIME          NOT NULL,
    CTRL_STATUS            ENUM('APPROVED', 'PENDING', 'REJECTED', 'WITHDRAWN') NOT NULL,
    PRIMARY KEY (USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: T_USER_SUBCLASS 
--

CREATE TABLE T_USER_SUBCLASS(
    USER_OID        DECIMAL(38, 0)    NOT NULL,
    SUBCLASS_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, SUBCLASS_OID)
)ENGINE=INNODB
;



-- 
-- INDEX: LOGIN_ID_UNIQUE 
--

CREATE UNIQUE INDEX LOGIN_ID_UNIQUE ON T_USER_PROFILE(LOGIN_ID)
;
-- 
-- TABLE: T_ALLOWED_ACCESS_COMPANY 
--

ALTER TABLE T_ALLOWED_ACCESS_COMPANY ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_BUYER_STORE_AREA_USER 
--

ALTER TABLE T_BUYER_STORE_AREA_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_BUYER_STORE_USER 
--

ALTER TABLE T_BUYER_STORE_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_GROUP_SUPPLIER 
--

ALTER TABLE T_GROUP_SUPPLIER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES T_GROUP(GROUP_OID)
;


-- 
-- TABLE: T_GROUP_TRADING_PARTNER 
--

ALTER TABLE T_GROUP_TRADING_PARTNER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES T_GROUP(GROUP_OID)
;


-- 
-- TABLE: T_GROUP_USER 
--

ALTER TABLE T_GROUP_USER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES T_GROUP(GROUP_OID)
;

ALTER TABLE T_GROUP_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_ROLE_GROUP 
--

ALTER TABLE T_ROLE_GROUP ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES T_ROLE(ROLE_OID)
;

ALTER TABLE T_ROLE_GROUP ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES T_GROUP(GROUP_OID)
;


-- 
-- TABLE: T_ROLE_OPERATION 
--

ALTER TABLE T_ROLE_OPERATION ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES T_ROLE(ROLE_OID)
;


-- 
-- TABLE: T_ROLE_USER 
--

ALTER TABLE T_ROLE_USER ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES T_ROLE(ROLE_OID)
;

ALTER TABLE T_ROLE_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_SUPPLIER_ROLE 
--

ALTER TABLE T_SUPPLIER_ROLE ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES T_ROLE(ROLE_OID)
;


-- 
-- TABLE: T_USER_CLASS 
--

ALTER TABLE T_USER_CLASS ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


-- 
-- TABLE: T_USER_SUBCLASS 
--

ALTER TABLE T_USER_SUBCLASS ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES T_USER_PROFILE(USER_OID)
;


