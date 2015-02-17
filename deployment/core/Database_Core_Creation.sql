--
-- ER/Studio 8.0 SQL Code Generation
-- Project :      DATA MODEL
--
-- Date Created : Monday, April 21, 2014 13:14:28
-- Target DBMS : MySQL 5.x
--

-- 
-- TABLE: `GROUP` 
--

CREATE TABLE `GROUP`(
    GROUP_OID              DECIMAL(38, 0)    NOT NULL,
    GROUP_ID               VARCHAR(20)       NOT NULL,
    GROUP_NAME             VARCHAR(100)      NOT NULL,
    GROUP_TYPE             ENUM('BUYER', 'SUPPLIER') NOT NULL,
    IGNORE_NOTIFICATION    BOOLEAN           NOT NULL,
    CREATE_DATE            DATETIME          NOT NULL,
    CREATE_BY              VARCHAR(50)       NOT NULL,
    UPDATE_DATE            DATETIME,
    UPDATE_BY              VARCHAR(50),
    USER_TYPE_OID          DECIMAL(38, 0)    NOT NULL,
    BUYER_OID              DECIMAL(38, 0),
    SUPPLIER_OID           DECIMAL(38, 0),
    PRIMARY KEY (GROUP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: ALLOWED_ACCESS_COMPANY 
--

CREATE TABLE ALLOWED_ACCESS_COMPANY(
    USER_OID       DECIMAL(38, 0)    NOT NULL,
    COMPANY_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, COMPANY_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: AUDIT_ACCESS 
--

CREATE TABLE AUDIT_ACCESS(
    ACCESS_OID        DECIMAL(38, 0)    NOT NULL,
    PRINCIPAL_TYPE    ENUM('USER', 'APPLICATION') NOT NULL,
    PRINCIPAL_OID     DECIMAL(38, 0),
    LOGIN_ID          VARCHAR(50),
    USER_TYPE_OID     DECIMAL(38, 0),
    COMPANY_OID       DECIMAL(38, 0),
    CLIENT_IP         VARCHAR(100)      NOT NULL,
    SUCCESS           BOOLEAN,
    ACTION_TYPE       ENUM('IN', 'OUT') NOT NULL,
    ACTION_DATE       DATETIME          NOT NULL,
    ATTEMPT_NO        INT,
    ERROR_CODE        CHAR(5),
    PRIMARY KEY (ACCESS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: AUDIT_SESSION 
--

CREATE TABLE AUDIT_SESSION(
    SESSION_OID        DECIMAL(38, 0)    NOT NULL,
    SESSION_ID         CHAR(14)          NOT NULL,
    START_DATE         DATETIME          NOT NULL,
    END_DATE           DATETIME,
    SESSION_SUMMARY    VARCHAR(255),
    USER_OID           DECIMAL(38, 0),
    LOGIN_ID           VARCHAR(50),
    BKEND_OPN_ID       VARCHAR(50),
    PRIMARY KEY (SESSION_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: AUDIT_TRAIL 
--

CREATE TABLE AUDIT_TRAIL(
    ADT_OID          DECIMAL(38, 0)    NOT NULL,
    ACTION_DATE      DATETIME          NOT NULL,
    ACTION_TYPE      ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    ACTOR_ACTION     ENUM('CREATE', 'UPDATE', 'DELETE', 'APPROVE', 'REJECT', 'WITHDRAW') NOT NULL,
    ACTOR_OID        DECIMAL(38, 0)    NOT NULL,
    ACTOR            VARCHAR(50)       NOT NULL,
    CLIENT_IP        VARCHAR(100)      NOT NULL,
    RECORD_KEY       VARCHAR(100),
    RECORD_TYPE      VARCHAR(50)       NOT NULL,
    RECORD_STATUS    ENUM('PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN'),
    XML_CONTENT      LONGTEXT          NOT NULL,
    SESSION_OID      DECIMAL(38, 0),
    PRIMARY KEY (ADT_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUSINESS_RULE 
--

CREATE TABLE BUSINESS_RULE(
    RULE_OID         DECIMAL(38, 0)    NOT NULL,
    FUNC_GROUP       VARCHAR(50)       NOT NULL,
    FUNC_ID          VARCHAR(50)       NOT NULL,
    RULE_ID          VARCHAR(100)      NOT NULL,
    RULE_DESC        VARCHAR(150),
    RULE_DESC_KEY    VARCHAR(150),
    PRIMARY KEY (RULE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER 
--

CREATE TABLE BUYER(
    BUYER_OID          DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE         VARCHAR(20)       NOT NULL,
    BUYER_NAME         VARCHAR(100)      NOT NULL,
    BUYER_ALIAS        VARCHAR(50),
    REG_NO             VARCHAR(50),
    GST_REG_NO         VARCHAR(50),
    GST_PERCENT        DECIMAL(5, 2),
    OTHER_REG_NO       VARCHAR(50),
    BRANCH             BOOLEAN           NOT NULL,
    ADDRESS_1          VARCHAR(100)      NOT NULL,
    ADDRESS_2          VARCHAR(100),
    ADDRESS_3          VARCHAR(100),
    ADDRESS_4          VARCHAR(100),
    STATE              VARCHAR(50),
    POSTAL_CODE        VARCHAR(15),
    CTRY_CODE          CHAR(2)           NOT NULL,
    CURR_CODE          CHAR(3)           NOT NULL,
    CONTACT_NAME       VARCHAR(50),
    CONTACT_TEL        VARCHAR(30),
    CONTACT_MOBILE     VARCHAR(30),
    CONTACT_FAX        VARCHAR(30),
    CONTACT_EMAIL      VARCHAR(100),
    ACTIVE             BOOLEAN           NOT NULL,
    BLOCKED            BOOLEAN           NOT NULL,
    BLOCK_REMARKS      VARCHAR(100),
    CREATE_DATE        DATETIME          NOT NULL,
    CREATE_BY          VARCHAR(50)       NOT NULL,
    INACTIVATE_DATE    DATETIME,
    INACTIVATE_BY      VARCHAR(50),
    UPDATE_BY          VARCHAR(50),
    UPDATE_DATE        DATETIME,
    BLOCK_DATE         DATETIME,
    BLOCK_BY           VARCHAR(50),
    MBOX_ID            VARCHAR(20)       NOT NULL,
    DEPLOYMENT_MODE    ENUM('LOCAL','REMOTE') NOT NULL,
    CHANNEL            VARCHAR(20),
    LOGO               MEDIUMBLOB,
    PRIMARY KEY (BUYER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_GIVEN_SUPPLIER_OPERATION 
--

CREATE TABLE BUYER_GIVEN_SUPPLIER_OPERATION(
    BUYER_OID    DECIMAL(38, 0)    NOT NULL,
    OPN_ID       VARCHAR(20)       NOT NULL,
    PRIMARY KEY (OPN_ID, BUYER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_MSG_SETTING 
--

CREATE TABLE BUYER_MSG_SETTING(
    BUYER_OID           DECIMAL(38, 0)    NOT NULL,
    MSG_TYPE            VARCHAR(20)       NOT NULL,
    ALERT_FREQUENCY     ENUM('BATCH', 'DOC', 'INTERVAL') NOT NULL,
    ALERT_INTERVAL      SMALLINT,
    RCPS_ADDRS          VARCHAR(255),
    ERROR_RCPS_ADDRS    VARCHAR(255),
    EXCLUDE_SUCC        BOOLEAN           NOT NULL,
    FILE_FORMAT         VARCHAR(20),
    PRIMARY KEY (MSG_TYPE, BUYER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_MSG_SETTING_REPORT 
--

CREATE TABLE BUYER_MSG_SETTING_REPORT(
    BUYER_OID            DECIMAL(38, 0)    NOT NULL,
    MSG_TYPE             VARCHAR(20)       NOT NULL,
    SUB_TYPE             VARCHAR(20)       DEFAULT 'Default' NOT NULL,
    CUSTOMIZED_REPORT    BOOLEAN           NOT NULL,
    REPORT_TEMPLATE      VARCHAR(30),
    PRIMARY KEY (BUYER_OID, MSG_TYPE, SUB_TYPE)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_OPERATION 
--

CREATE TABLE BUYER_OPERATION(
    BUYER_OID    DECIMAL(38, 0)    NOT NULL,
    OPN_ID       VARCHAR(20)       NOT NULL,
    PRIMARY KEY (OPN_ID, BUYER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_RULE 
--

CREATE TABLE BUYER_RULE(
    BUYER_OID       DECIMAL(38, 0)    NOT NULL,
    RULE_OID        DECIMAL(38, 0)    NOT NULL,
    NUM_VALUE       DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    PRIMARY KEY (RULE_OID, BUYER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_STORE 
--

CREATE TABLE BUYER_STORE(
    STORE_OID            DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE           VARCHAR(20)       NOT NULL,
    STORE_CODE           VARCHAR(20)       NOT NULL,
    STORE_NAME           VARCHAR(100),
    STORE_ADDR1          VARCHAR(100),
    STORE_ADDR2          VARCHAR(100),
    STORE_ADDR3          VARCHAR(100),
    STORE_ADDR4          VARCHAR(100),
    STORE_ADDR5          VARCHAR(100),
    STORE_CITY           VARCHAR(50),
    STORE_STATE          VARCHAR(50),
    STORE_POSTAL_CODE    VARCHAR(15),
    STORE_CTRY_CODE      CHAR(2),
    CONTACT_PERSON       VARCHAR(50),
    CONTACT_TEL          VARCHAR(30),
    CONTACT_EMAIL        VARCHAR(100),
    CREATE_DATE          DATETIME          NOT NULL,
    UPDATE_DATE          DATETIME,
    IS_WAREHOUSE         BOOLEAN           NOT NULL,
    AREA_OID             DECIMAL(38, 0),
    PRIMARY KEY (STORE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_STORE_AREA 
--

CREATE TABLE BUYER_STORE_AREA(
    AREA_OID       DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE     VARCHAR(20)       NOT NULL,
    AREA_CODE      VARCHAR(20)       NOT NULL,
    AREA_NAME      VARCHAR(100),
    CREATE_DATE    DATETIME          NOT NULL,
    UPDATE_DATE    DATETIME,
    PRIMARY KEY (AREA_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_STORE_AREA_USER 
--

CREATE TABLE BUYER_STORE_AREA_USER(
    USER_OID    DECIMAL(38, 0)    NOT NULL,
    AREA_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, AREA_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: BUYER_STORE_USER 
--

CREATE TABLE BUYER_STORE_USER(
    STORE_OID    DECIMAL(38, 0)    NOT NULL,
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (STORE_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CC_DETAIL 
--

CREATE TABLE CC_DETAIL(
    INV_OID               DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO           INTEGER           NOT NULL,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BARCODE               VARCHAR(50),
    ITEM_DESC             VARCHAR(100),
    BRAND                 VARCHAR(20),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(20),
    SIZE_DESC             VARCHAR(50),
    PACKING_FACTOR        DECIMAL(8, 2),
    INV_BASE_UNIT         CHAR(1),
    INV_UOM               VARCHAR(20)       NOT NULL,
    INV_QTY               DECIMAL(10, 4)    NOT NULL,
    FOC_BASE_UNIT         CHAR(1),
    FOC_UOM               VARCHAR(20),
    FOC_QTY               DECIMAL(10, 4),
    UNIT_PRICE            DECIMAL(15, 4),
    DISCOUNT_AMOUNT       DECIMAL(15, 4)    NOT NULL,
    DISCOUNT_PERCENT      DECIMAL(4, 2),
    NET_PRICE             DECIMAL(15, 4),
    ITEM_AMOUNT           DECIMAL(15, 4)    NOT NULL,
    NET_AMOUNT            DECIMAL(15, 4)    NOT NULL,
    ITEM_SHARED_COST      DECIMAL(15, 4),
    ITEM_GROSS_AMOUNT     DECIMAL(15, 4),
    ITEM_REMARKS          VARCHAR(100),
    LINE_REF_NO           VARCHAR(20),
    PRIMARY KEY (LINE_SEQ_NO, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CC_DETAIL_EXTENDED 
--

CREATE TABLE CC_DETAIL_EXTENDED(
    INV_OID         DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, LINE_SEQ_NO, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CC_HEADER 
--

CREATE TABLE CC_HEADER(
    INV_OID                        DECIMAL(38, 0)    NOT NULL,
    INV_NO                         VARCHAR(20)       NOT NULL,
    DOC_ACTION                     CHAR(1)           NOT NULL,
    ACTION_DATE                    DATETIME          NOT NULL,
    INV_TYPE                       CHAR(3)           NOT NULL,
    INV_DATE                       DATETIME          NOT NULL,
    PO_NO                          VARCHAR(20),
    PO_DATE                        DATE,
    DELIVERY_DATE                  DATETIME          NOT NULL,
    BUYER_OID                      DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                     VARCHAR(20)       NOT NULL,
    BUYER_NAME                     VARCHAR(100)      NOT NULL,
    BUYER_ADDR1                    VARCHAR(100),
    BUYER_ADDR2                    VARCHAR(100),
    BUYER_ADDR3                    VARCHAR(100),
    BUYER_ADDR4                    VARCHAR(100),
    BUYER_CITY                     VARCHAR(50),
    BUYER_STATE                    VARCHAR(50),
    BUYER_CTRY_CODE                CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE              VARCHAR(15),
    DEPT_CODE                      VARCHAR(20),
    DEPT_NAME                      VARCHAR(100),
    SUB_DEPT_CODE                  VARCHAR(20),
    SUB_DEPT_NAME                  VARCHAR(100),
    SUPPLIER_OID                   DECIMAL(38, 0),
    SUPPLIER_CODE                  VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME                  VARCHAR(100),
    SUPPLIER_ADDR1                 VARCHAR(100),
    SUPPLIER_ADDR2                 VARCHAR(100),
    SUPPLIER_ADDR3                 VARCHAR(100),
    SUPPLIER_ADDR4                 VARCHAR(100),
    SUPPLIER_CITY                  VARCHAR(50),
    SUPPLIER_STATE                 VARCHAR(50),
    SUPPLIER_CTRY_CODE             CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE           VARCHAR(15),
    STORE_CODE                     VARCHAR(20),
    STORE_NAME                     VARCHAR(100),
    STORE_ADDR1                    VARCHAR(100),
    STORE_ADDR2                    VARCHAR(100),
    STORE_ADDR3                    VARCHAR(100),
    STORE_ADDR4                    VARCHAR(100),
    STORE_CITY                     VARCHAR(50),
    STORE_STATE                    VARCHAR(50),
    STORE_CTRY_CODE                CHAR(2),
    STORE_POSTAL_CODE              VARCHAR(15),
    PAY_TERM_CODE                  VARCHAR(20),
    PAY_TERM_DESC                  VARCHAR(100),
    PAY_INSTRUCT                   VARCHAR(500),
    ADDITIONAL_DISCOUNT_AMOUNT     DECIMAL(15, 4)    NOT NULL,
    ADDITIONAL_DISCOUNT_PERCENT    DECIMAL(4, 2),
    INV_AMOUNT_NO_VAT              DECIMAL(15, 4)    NOT NULL,
    VAT_AMOUNT                     DECIMAL(15, 4)    NOT NULL,
    INV_AMOUNT_WITH_VAT            DECIMAL(15, 4)    NOT NULL,
    VAT_RATE                       DECIMAL(4, 2)     NOT NULL,
    FOOTER_LINE_1                  VARCHAR(100),
    FOOTER_LINE_2                  VARCHAR(100),
    FOOTER_LINE_3                  VARCHAR(100),
    FOOTER_LINE_4                  VARCHAR(100),
    INV_REMARKS                    VARCHAR(500),
    CTRL_STATUS                    ENUM('NEW','AMENDED','OUTDATED') NOT NULL,
    DUPLICATE                      BOOLEAN           NOT NULL,
    PRIMARY KEY (INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CC_HEADER_EXTENDED 
--

CREATE TABLE CC_HEADER_EXTENDED(
    INV_OID         DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CLASS 
--

CREATE TABLE CLASS(
    CLASS_OID     DECIMAL(38, 0)    NOT NULL,
    CLASS_CODE    VARCHAR(20)       NOT NULL,
    CLASS_DESC    VARCHAR(50),
    BUYER_OID     DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (CLASS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CLIENT_AUDIT_TRAIL 
--

CREATE TABLE CLIENT_AUDIT_TRAIL(
    ADT_OID            DECIMAL(38, 0)    NOT NULL,
    ACTION_DATE        DATETIME          NOT NULL,
    ACTION_TYPE        ENUM('INBOX', 'OUTBOX') NOT NULL,
    ACTION_STATUS      BOOLEAN           NOT NULL,
    ACTION_SUMMARY     TEXT,
    CLIENT_IP          VARCHAR(150)      NOT NULL,
    BATCH_FILE_NAME    VARCHAR(50)       NOT NULL,
    XML_CONTENT        TEXT              NOT NULL,
    PRIMARY KEY (ADT_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CN_DETAIL 
--

CREATE TABLE CN_DETAIL(
    CN_OID                    DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO               INTEGER           NOT NULL,
    BUYER_ITEM_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE        VARCHAR(20),
    BARCODE                   VARCHAR(50),
    ITEM_DESC                 VARCHAR(100),
    BRAND                     VARCHAR(20),
    COLOUR_CODE               VARCHAR(20),
    COLOUR_DESC               VARCHAR(50),
    SIZE_CODE                 VARCHAR(20),
    SIZE_DESC                 VARCHAR(50),
    PO_NO                     VARCHAR(20),
    PO_DATE                   DATE,
    INV_NO                    VARCHAR(20),
    INV_DATE                  DATE,
    RTV_NO                    VARCHAR(20),
    RTV_DATE                  DATE,
    GI_NO                     VARCHAR(20),
    GI_DATE                   DATE,
    PACKING_FACTOR            DECIMAL(8, 2),
    CREDIT_BASE_UNIT          CHAR(1),
    CREDIT_UOM                VARCHAR(20),
    CREDIT_QTY                DECIMAL(10, 4),
    UNIT_COST                 DECIMAL(15, 4),
    COST_DISCOUNT_AMOUNT      DECIMAL(15, 4),
    COST_DISCOUNT_PERCENT     DECIMAL(4, 2),
    RETAIL_DISCOUNT_AMOUNT    DECIMAL(15, 4),
    NET_UNIT_COST             DECIMAL(15, 4),
    ITEM_COST                 DECIMAL(15, 4),
    ITEM_SHARED_COST          DECIMAL(15, 4),
    ITEM_GROSS_COST           DECIMAL(15, 4),
    RETAIL_PRICE              DECIMAL(15, 4),
    ITEM_RETAIL_AMOUNT        DECIMAL(15, 4),
    REASON_CODE               VARCHAR(20),
    REASON_DESC               VARCHAR(100),
    LINE_REF_NO               VARCHAR(20),
    PRIMARY KEY (LINE_SEQ_NO, CN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CN_DETAIL_EXTENDED 
--

CREATE TABLE CN_DETAIL_EXTENDED(
    CN_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, CN_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: CN_HEADER 
--

CREATE TABLE CN_HEADER(
    CN_OID                  DECIMAL(38, 0)    NOT NULL,
    CN_NO                   VARCHAR(20)       NOT NULL,
    DOC_ACTION              CHAR(1)           NOT NULL,
    ACTION_DATE             DATETIME          NOT NULL,
    CN_TYPE                 ENUM('CON')       NOT NULL,
    CN_TYPE_DESC            VARCHAR(50),
    CN_DATE                 DATE              NOT NULL,
    PO_NO                   VARCHAR(20),
    PO_DATE                 DATE,
    INV_NO                  VARCHAR(20),
    INV_DATE                DATE,
    RTV_NO                  VARCHAR(20),
    RTV_DATE                DATE,
    GI_NO                   VARCHAR(20),
    GI_DATE                 DATE,
    BUYER_OID               DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE              VARCHAR(20)       NOT NULL,
    BUYER_NAME              VARCHAR(100)      NOT NULL,
    BUYER_ADDR1             VARCHAR(100),
    BUYER_ADDR2             VARCHAR(100),
    BUYER_ADDR3             VARCHAR(100),
    BUYER_ADDR4             VARCHAR(100),
    BUYER_CITY              VARCHAR(50),
    BUYER_STATE             VARCHAR(50),
    BUYER_CTRY_CODE         CHAR(2),
    BUYER_POSTAL_CODE       VARCHAR(15),
    DEPT_CODE               VARCHAR(20),
    DEPT_NAME               VARCHAR(100),
    SUB_DEPT_CODE           VARCHAR(20),
    SUB_DEPT_NAME           VARCHAR(100),
    SUPPLIER_OID            DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME           VARCHAR(100),
    SUPPLIER_ADDR1          VARCHAR(100),
    SUPPLIER_ADDR2          VARCHAR(100),
    SUPPLIER_ADDR3          VARCHAR(100),
    SUPPLIER_ADDR4          VARCHAR(100),
    SUPPLIER_CITY           VARCHAR(50),
    SUPPLIER_STATE          VARCHAR(50),
    SUPPLIER_CTRY_CODE      CHAR(2),
    SUPPLIER_POSTAL_CODE    VARCHAR(15),
    STORE_CODE              VARCHAR(20),
    STORE_NAME              VARCHAR(100),
    STORE_ADDR1             VARCHAR(100),
    STORE_ADDR2             VARCHAR(100),
    STORE_ADDR3             VARCHAR(100),
    STORE_ADDR4             VARCHAR(100),
    STORE_CITY              VARCHAR(50),
    STORE_STATE             VARCHAR(50),
    STORE_CTRY_CODE         CHAR(2),
    STORE_POSTAL_CODE       VARCHAR(15),
    TOTAL_COST              DECIMAL(15, 4)    NOT NULL,
    TOTAL_VAT               DECIMAL(15, 4)    NOT NULL,
    TOTAL_COST_WITH_VAT     DECIMAL(15, 4)    NOT NULL,
    VAT_RATE                DECIMAL(4, 2)     NOT NULL,
    REASON_CODE             VARCHAR(20),
    REASON_DESC             VARCHAR(100),
    CN_REMARKS              VARCHAR(500),
    CTRL_STATUS             ENUM('NEW', 'SUBMIT', 'VOID','VOID_OUTDATED') NOT NULL,
    DUPLICATE               BOOLEAN           NOT NULL,
    PRIMARY KEY (CN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CN_HEADER_EXTENDED 
--

CREATE TABLE CN_HEADER_EXTENDED(
    CN_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, CN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: CONTROL_PARAMETER 
--

CREATE TABLE CONTROL_PARAMETER(
    PARAM_OID       DECIMAL(38, 0)          NOT NULL,
    SECT_ID         VARCHAR(30)             NOT NULL,
    PARAM_ID        VARCHAR(30)             NOT NULL,
    CAT_ID          VARCHAR(30),
    PARAM_DESC      VARCHAR(50),
    STRING_VALUE    VARCHAR(255),
    NUM_VALUE       INT,
    DATE_VALUE      DATETIME,
    CREATE_DATE     DATETIME                NOT NULL,
    CREATE_BY       NATIONAL VARCHAR(50)    NOT NULL,
    UPDATE_DATE     DATETIME,
    UPDATE_BY       NATIONAL VARCHAR(50),
    VALID           BOOLEAN                 NOT NULL,
    PRIMARY KEY (PARAM_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: COUNTRY 
--

CREATE TABLE COUNTRY(
    CTRY_CODE    CHAR(2)        NOT NULL,
    CTRY_DESC    VARCHAR(50)    NOT NULL,
    PRIMARY KEY (CTRY_CODE)
)ENGINE=INNODB
;



-- 
-- TABLE: CURRENCY 
--

CREATE TABLE CURRENCY(
    CURR_CODE    CHAR(3)        NOT NULL,
    CURR_DESC    VARCHAR(50)    NOT NULL,
    PRIMARY KEY (CURR_CODE)
)ENGINE=INNODB
;



-- 
-- TABLE: DICTIONARY_WORD 
--

CREATE TABLE DICTIONARY_WORD(
    KEY_WORD    VARCHAR(20)    NOT NULL,
    PRIMARY KEY (KEY_WORD)
)ENGINE=MYISAM
;



-- 
-- TABLE: DN_DETAIL 
--

CREATE TABLE DN_DETAIL(
    DN_OID                         DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO                    INTEGER           NOT NULL,
    BUYER_ITEM_CODE                VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE             VARCHAR(20),
    BARCODE                        VARCHAR(50),
    ITEM_DESC                      VARCHAR(100),
    BRAND                          VARCHAR(20),
    COLOUR_CODE                    VARCHAR(20),
    COLOUR_DESC                    VARCHAR(50),
    SIZE_CODE                      VARCHAR(20),
    SIZE_DESC                      VARCHAR(50),
    PO_NO                          VARCHAR(20),
    PO_DATE                        DATE,
    INV_NO                         VARCHAR(20),
    INV_DATE                       DATE,
    PACKING_FACTOR                 DECIMAL(8, 2),
    DEBIT_BASE_UNIT                CHAR(1),
    ORDER_UOM                      VARCHAR(20),
    DEBIT_QTY                      DECIMAL(10, 4),
    UNIT_COST                      DECIMAL(15, 4),
    COST_DISCOUNT_AMOUNT           DECIMAL(15, 4),
    COST_DISCOUNT_PERCENT          DECIMAL(4, 2),
    RETAIL_DISCOUNT_AMOUNT         DECIMAL(15, 4),
    NET_UNIT_COST                  DECIMAL(15, 4),
    ITEM_COST                      DECIMAL(15, 4),
    ITEM_SHARED_CODE               DECIMAL(15, 4),
    ITEM_GROSS_COST                DECIMAL(15, 4),
    RETAIL_PRICE                   DECIMAL(15, 4),
    ITEM_RETAIL_AMOUNT             DECIMAL(15, 4),
    ITEM_REMARKS                   VARCHAR(100),
    DISPUTE_PRICE                  DECIMAL(15, 4),
    DISPUTE_PRICE_REMARKS          VARCHAR(255),
    DISPUTE_QTY                    DECIMAL(10, 4),
    DISPUTE_QTY_REMARKS            VARCHAR(255),
    PRICE_STATUS                   ENUM('PENDING','REJECTED','ACCEPTED'),
    PRICE_STATUS_ACTION_REMARKS    VARCHAR(255),
    PRICE_STATUS_ACTION_BY         VARCHAR(50),
    PRICE_STATUS_ACTION_DATE       DATETIME,
    QTY_STATUS                     ENUM('PENDING','REJECTED','ACCEPTED'),
    QTY_STATUS_ACTION_REMARKS      VARCHAR(255),
    QTY_STATUS_ACTION_BY           VARCHAR(50),
    QTY_STATUS_ACTION_DATE         DATETIME,
    CONFIRM_PRICE                  DECIMAL(15, 4),
    CONFIRM_QTY                    DECIMAL(10, 4),
    LINE_REF_NO                    VARCHAR(20),
    PRIMARY KEY (DN_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: DN_DETAIL_EXTENDED 
--

CREATE TABLE DN_DETAIL_EXTENDED(
    DN_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (DN_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: DN_HEADER 
--

CREATE TABLE DN_HEADER(
    DN_OID                   DECIMAL(38, 0)    NOT NULL,
    DN_NO                    VARCHAR(20)       NOT NULL,
    DOC_ACTION               CHAR(1)           NOT NULL,
    ACTION_DATE              DATETIME          NOT NULL,
    DN_TYPE                  VARCHAR(10)       NOT NULL,
    DN_TYPE_DESC             VARCHAR(50),
    DN_DATE                  DATE              NOT NULL,
    PO_NO                    VARCHAR(20),
    PO_DATE                  DATE,
    INV_NO                   VARCHAR(20),
    INV_DATE                 DATE,
    RTV_NO                   VARCHAR(20),
    RTV_DATE                 DATE,
    GI_NO                    VARCHAR(20),
    GI_DATE                  DATE,
    BUYER_OID                DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE               VARCHAR(20)       NOT NULL,
    BUYER_NAME               VARCHAR(100)      NOT NULL,
    BUYER_ADDR1              VARCHAR(100),
    BUYER_ADDR2              VARCHAR(100),
    BUYER_ADDR3              VARCHAR(100),
    BUYER_ADDR4              VARCHAR(100),
    BUYER_CITY               VARCHAR(50),
    BUYER_STATE              VARCHAR(50),
    BUYER_CTRY_CODE          CHAR(2),
    BUYER_POSTAL_CODE        VARCHAR(15),
    DEPT_CODE                VARCHAR(20),
    DEPT_NAME                VARCHAR(100),
    SUB_DEPT_CODE            VARCHAR(20),
    SUB_DEPT_NAME            VARCHAR(100),
    SUPPLIER_OID             DECIMAL(38, 0),
    SUPPLIER_CODE            VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME            VARCHAR(100),
    SUPPLIER_ADDR1           VARCHAR(100),
    SUPPLIER_ADDR2           VARCHAR(100),
    SUPPLIER_ADDR3           VARCHAR(100),
    SUPPLIER_ADDR4           VARCHAR(100),
    SUPPLIER_CITY            VARCHAR(50),
    SUPPLIER_STATE           VARCHAR(50),
    SUPPLIER_CTRY_CODE       CHAR(2),
    SUPPLIER_POSTAL_CODE     VARCHAR(15),
    STORE_CODE               VARCHAR(20),
    STORE_NAME               VARCHAR(100),
    STORE_ADDR1              VARCHAR(100),
    STORE_ADDR2              VARCHAR(100),
    STORE_ADDR3              VARCHAR(100),
    STORE_ADDR4              VARCHAR(100),
    STORE_CITY               VARCHAR(50),
    STORE_STATE              VARCHAR(50),
    STORE_CTRY_CODE          CHAR(2),
    STORE_POSTAL_CODE        VARCHAR(15),
    TOTAL_COST               DECIMAL(15, 4)    NOT NULL,
    TOTAL_VAT                DECIMAL(15, 4)    NOT NULL,
    TOTAL_COST_WITH_VAT      DECIMAL(15, 4)    NOT NULL,
    VAT_RATE                 DECIMAL(4, 2)     NOT NULL,
    REASON_CODE              VARCHAR(20),
    REASON_DESC              VARCHAR(100),
    DN_REMARKS               VARCHAR(500),
    SENT_TO_SUPPLIER         BOOLEAN           NOT NULL,
    MARK_SENT_TO_SUPPLIER    BOOLEAN           NOT NULL,
    CTRL_STATUS              ENUM('NEW', 'AMENDED', 'OUTDATED') NOT NULL,
    DUPLICATE                BOOLEAN           NOT NULL,
    PRICE_STATUS             ENUM('PENDING','ACCEPTED','REJECTED'),
    QTY_STATUS               ENUM('PENDING','REJECTED','ACCEPTED'),
    BUYER_STATUS             ENUM('PENDING','REJECTED','ACCEPTED'),
    DISPUTE                  BOOLEAN,
    DISPUTE_BY               VARCHAR(50),
    DISPUTE_DATE             DATETIME,
    EXPORTED                 BOOLEAN,
    EXPORTED_DATE            DATETIME,
    CLOSED                   BOOLEAN,
    CLOSED_BY                VARCHAR(50),
    CLOSED_DATE              DATETIME,
    CLOSED_REMARKS           VARCHAR(255),
    PRICE_DISPUTED           BOOLEAN,
    QTY_DISPUTED             BOOLEAN,
    PRIMARY KEY (DN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: DN_HEADER_EXTENDED 
--

CREATE TABLE DN_HEADER_EXTENDED(
    DN_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (DN_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: DO_DETAIL 
--

CREATE TABLE DO_DETAIL(
    DO_OID                DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO           INTEGER           NOT NULL,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BARCODE               VARCHAR(50),
    ITEM_DESC             VARCHAR(100),
    BRAND                 VARCHAR(20),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(20),
    SIZE_DESC             VARCHAR(50),
    PACKING_FACTOR        DECIMAL(8, 2),
    DELIVERY_BASE_UNIT    CHAR(1),
    DELIVERY_UOM          VARCHAR(20)       NOT NULL,
    DELIVERY_QTY          DECIMAL(10, 4)    NOT NULL,
    FOC_BASE_UNIT         CHAR(1),
    FOC_UOM               VARCHAR(20),
    FOC_QTY               DECIMAL(10, 4),
    ITEM_REMARKS          VARCHAR(100),
    PRIMARY KEY (DO_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: DO_DETAIL_EXTENDED 
--

CREATE TABLE DO_DETAIL_EXTENDED(
    DO_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (DO_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: DO_HEADER 
--

CREATE TABLE DO_HEADER(
    DO_OID                  DECIMAL(38, 0)    NOT NULL,
    DO_NO                   VARCHAR(20)       NOT NULL,
    DOC_ACTION              CHAR(1)           NOT NULL,
    ACTION_DATE             DATETIME          NOT NULL,
    DO_DATE                 DATE              NOT NULL,
    PO_NO                   VARCHAR(20)       NOT NULL,
    PO_DATE                 DATE              NOT NULL,
    DELIVERY_DATE           DATETIME          NOT NULL,
    EXPIRY_DATE             DATETIME,
    BUYER_OID               DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE              VARCHAR(20)       NOT NULL,
    BUYER_NAME              VARCHAR(100)      NOT NULL,
    BUYER_ADDR1             VARCHAR(100),
    BUYER_ADDR2             VARCHAR(100),
    BUYER_ADDR3             VARCHAR(100),
    BUYER_ADDR4             VARCHAR(100),
    BUYER_CITY              VARCHAR(50),
    BUYER_STATE             VARCHAR(50),
    BUYER_CTRY_CODE         CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE       VARCHAR(15),
    DEPT_CODE               VARCHAR(20),
    DEPT_NAME               VARCHAR(100),
    SUB_DEPT_CODE           VARCHAR(20),
    SUB_DEPT_NAME           VARCHAR(100),
    SUPPLIER_OID            DECIMAL(38, 0),
    SUPPLIER_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME           VARCHAR(100),
    SUPPLIER_ADDR1          VARCHAR(100),
    SUPPLIER_ADDR2          VARCHAR(100),
    SUPPLIER_ADDR3          VARCHAR(100),
    SUPPLIER_ADDR4          VARCHAR(100),
    SUPPLIER_CITY           VARCHAR(50),
    SUPPLIER_STATE          VARCHAR(50),
    SUPPLIER_CTRY_CODE      CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE    VARCHAR(15),
    SHIP_TO_CODE            VARCHAR(20),
    SHIP_TO_NAME            VARCHAR(100),
    SHIP_TO_ADDR1           VARCHAR(100),
    SHIP_TO_ADDR2           VARCHAR(100),
    SHIP_TO_ADDR3           VARCHAR(100),
    SHIP_TO_ADDR4           VARCHAR(100),
    SHIP_TO_CITY            VARCHAR(50),
    SHIP_TO_STATE           VARCHAR(50),
    SHIP_TO_CTRY_CODE       CHAR(2),
    SHIP_TO_POSTAL_CODE     VARCHAR(15),
    DO_REMARKS              VARCHAR(500),
    PRIMARY KEY (DO_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: DO_HEADER_EXTENDED 
--

CREATE TABLE DO_HEADER_EXTENDED(
    DO_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (DO_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: DOC_CLASS 
--

CREATE TABLE DOC_CLASS(
    DOC_OID           DECIMAL(38, 0)    NOT NULL,
    CLASS_CODE        VARCHAR(20)       NOT NULL,
    AUDIT_FINISHED    BOOLEAN           NOT NULL,
    PRIMARY KEY (DOC_OID, CLASS_CODE)
)ENGINE=INNODB
;



-- 
-- TABLE: DOC_SUBCLASS 
--

CREATE TABLE DOC_SUBCLASS(
    DOC_OID           DECIMAL(38, 0)    NOT NULL,
    CLASS_CODE        VARCHAR(20)       NOT NULL,
    SUBCLASS_CODE     VARCHAR(20)       NOT NULL,
    AUDIT_FINISHED    BOOLEAN           NOT NULL,
    PRIMARY KEY (DOC_OID, SUBCLASS_CODE, CLASS_CODE)
)ENGINE=INNODB
;



-- 
-- TABLE: FAVOURITE_LIST 
--

CREATE TABLE FAVOURITE_LIST(
    LIST_OID     DECIMAL(38, 0)    NOT NULL,
    LIST_CODE    VARCHAR(20)       NOT NULL,
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (LIST_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: FAVOURITE_LIST_SUPPLIER 
--

CREATE TABLE FAVOURITE_LIST_SUPPLIER(
    LIST_OID        DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (LIST_OID, SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GI_DETAIL 
--

CREATE TABLE GI_DETAIL(
    GI_OID                DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO           INTEGER           NOT NULL,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BARCODE               VARCHAR(50),
    ITEM_DESC             VARCHAR(50),
    BRAND                 VARCHAR(20),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(10),
    SIZE_DESC             VARCHAR(20),
    PACKING_FACTOR        DECIMAL(8, 2),
    RTV_BASE_UNIT         CHAR(1),
    RTV_UOM               VARCHAR(20),
    RTV_QTY               DECIMAL(10, 4),
    ISSUED_QTY            DECIMAL(10, 4)    NOT NULL,
    UNIT_COST             DECIMAL(15, 4),
    ITEM_COST             DECIMAL(15, 4),
    ITEM_REMARKS          VARCHAR(100),
    PRIMARY KEY (GI_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: GI_DETAIL_EXTENDED 
--

CREATE TABLE GI_DETAIL_EXTENDED(
    GI_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VLAUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (GI_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: GI_HEADER 
--

CREATE TABLE GI_HEADER(
    GI_OID                DECIMAL(38, 0)    NOT NULL,
    GI_NO                 VARCHAR(20)       NOT NULL,
    DOC_ACTION            CHAR(1)           NOT NULL,
    ACTION_DATE           DATETIME          NOT NULL,
    GI_DATE               DATE,
    RTV_NO                VARCHAR(20)       NOT NULL,
    RTV_DATE              DATE              NOT NULL,
    CREATE_DATE           DATETIME          NOT NULL,
    BUYER_OID             DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE            VARCHAR(20)       NOT NULL,
    BUYER_NAME            VARCHAR(100),
    SUPPLIER_OID          DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_CODE         VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME         VARCHAR(100),
    ISSUING_STORE_CODE    VARCHAR(20),
    ISSUING_STORE_NAME    VARCHAR(100),
    TOTAL_ISSUED_QTY      DECIMAL(10, 4),
    COLLECTED_BY          VARCHAR(50),
    ITEM_COUNT            INTEGER,
    TOTAL_COST            DECIMAL(15, 4),
    GI_REMARKS            VARCHAR(500),
    CTRL_STATUS           ENUM('NEW', 'AMENDED', 'OUTDATED') NOT NULL,
    DUPLICATE             BOOLEAN           NOT NULL,
    PRIMARY KEY (GI_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GI_HEADER_EXTENDED 
--

CREATE TABLE GI_HEADER_EXTENDED(
    GI_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FILED_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, GI_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GRN_DETAIL 
--

CREATE TABLE GRN_DETAIL(
    GRN_OID               DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO           INTEGER           NOT NULL,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BARCODE               VARCHAR(50),
    ITEM_DESC             VARCHAR(100),
    BRAND                 VARCHAR(20),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(20),
    SIZE_DESC             VARCHAR(50),
    PACKING_FACTOR        DECIMAL(8, 2)     NOT NULL,
    ORDER_BASE_UNIT       CHAR(1)           NOT NULL,
    ORDER_UOM             VARCHAR(20)       NOT NULL,
    ORDER_QTY             DECIMAL(10, 4)    NOT NULL,
    RECEIVE_QTY           DECIMAL(10, 4)    NOT NULL,
    FOC_BASE_UNIT         CHAR(1),
    FOC_UOM               VARCHAR(20),
    FOC_QTY               DECIMAL(10, 4),
    FOC_RECEIVE_QTY       DECIMAL(10, 4),
    UNIT_COST             DECIMAL(15, 4),
    ITEM_COST             DECIMAL(15, 4)    NOT NULL,
    RETAIL_PRICE          DECIMAL(15, 4),
    ITEM_RETAIL_AMOUNT    DECIMAL(15, 4),
    ITEM_REMARKS          VARCHAR(100),
    DELIVERY_QTY          DECIMAL(10, 4),
    PRIMARY KEY (GRN_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: GRN_DETAIL_EXTENDED 
--

CREATE TABLE GRN_DETAIL_EXTENDED(
    GRN_OID         DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (GRN_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: GRN_HEADER 
--

CREATE TABLE GRN_HEADER(
    GRN_OID                      DECIMAL(38, 0)    NOT NULL,
    GRN_NO                       VARCHAR(20)       NOT NULL,
    DOC_ACTION                   CHAR(1)           NOT NULL,
    ACTION_DATE                  DATETIME          NOT NULL,
    GRN_DATE                     DATE              NOT NULL,
    CREATE_DATE                  DATETIME          NOT NULL,
    PO_NO                        VARCHAR(20)       NOT NULL,
    PO_DATE                      DATE              NOT NULL,
    BUYER_OID                    DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                   VARCHAR(20)       NOT NULL,
    BUYER_NAME                   VARCHAR(100)      NOT NULL,
    BUYER_ADDR1                  VARCHAR(100),
    BUYER_ADDR2                  VARCHAR(100),
    BUYER_ADDR3                  VARCHAR(100),
    BUYER_ADDR4                  VARCHAR(100),
    BUYER_CITY                   VARCHAR(50),
    BUYER_STATE                  VARCHAR(50),
    BUYER_CTRY_CODE              CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE            VARCHAR(15),
    DEPT_CODE                    VARCHAR(20),
    DEPT_NAME                    VARCHAR(100),
    SUB_DEPT_CODE                VARCHAR(20),
    SUB_DEPT_NAME                VARCHAR(100),
    SUPPLIER_OID                 DECIMAL(38, 0),
    SUPPLIER_CODE                VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME                VARCHAR(100),
    SUPPLIER_ADDR1               VARCHAR(100),
    SUPPLIER_ADDR2               VARCHAR(100),
    SUPPLIER_ADDR3               VARCHAR(100),
    SUPPLIER_ADDR4               VARCHAR(100),
    SUPPLIER_CITY                VARCHAR(50),
    SUPPLIER_STATE               VARCHAR(50),
    SUPPLIER_CTRY_CODE           CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE         VARCHAR(15),
    RECEIVE_STORE_CODE           VARCHAR(20),
    RECEIVE_STORE_NAME           VARCHAR(100),
    RECEIVE_STORE_ADDR1          VARCHAR(100),
    RECEIVE_STORE_ADDR2          VARCHAR(100),
    RECEIVE_STORE_ADDR3          VARCHAR(100),
    RECEIVE_STORE_ADDR4          VARCHAR(100),
    RECEIVE_STORE_CITY           VARCHAR(50),
    RECEIVE_STORE_STATE          VARCHAR(50),
    RECEIVE_STORE_CTRY_CODE      CHAR(2),
    RECEIVE_STORE_POSTAL_CODE    VARCHAR(15),
    TOTAL_EXPECTED_QTY           DECIMAL(10, 4),
    TOTAL_RECEIVED_QTY           DECIMAL(10, 4),
    ITEM_COUNT                   DECIMAL(4, 0),
    DISCOUNT_AMOUNT              DECIMAL(15, 4),
    NET_COST                     DECIMAL(15, 4),
    TOTAL_COST                   DECIMAL(15, 4),
    TOTAL_RETAIL_AMOUNT          DECIMAL(15, 4),
    GRN_REMARKS                  VARCHAR(500),
    CTRL_STATUS                  ENUM('NEW', 'AMENDED', 'OUTDATED') NOT NULL,
    DUPLICATE                    BOOLEAN           NOT NULL,
    DISPUTE                      BOOLEAN           NOT NULL,
    DISPUTE_STATUS               ENUM('PENDING','APPROVED','REJECTED') NOT NULL,
    DISPUTE_BUYER_BY             VARCHAR(20),
    DISPUTE_BUYER_DATE           DATETIME,
    DISPUTE_BUYER_REMARKS        VARCHAR(255),
    DISPUTE_SUPPLIER_BY          VARCHAR(20),
    DISPUTE_SUPPLIER_DATE        DATETIME,
    DISPUTE_SUPPLIER_REMARKS     VARCHAR(255),
    PRIMARY KEY (GRN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GRN_HEADER_EXTENDED 
--

CREATE TABLE GRN_HEADER_EXTENDED(
    GRN_OID         DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (GRN_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: GROUP_SUPPLIER 
--

CREATE TABLE GROUP_SUPPLIER(
    GROUP_OID       DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (GROUP_OID, SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GROUP_TRADING_PARTNER 
--

CREATE TABLE GROUP_TRADING_PARTNER(
    GROUP_OID    DECIMAL(38, 0)    NOT NULL,
    TP_OID       DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (GROUP_OID, TP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: GROUP_USER 
--

CREATE TABLE GROUP_USER(
    GROUP_OID    DECIMAL(38, 0)    NOT NULL,
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (GROUP_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: INV_DETAIL 
--

CREATE TABLE INV_DETAIL(
    INV_OID               DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO           INTEGER           NOT NULL,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BARCODE               VARCHAR(50),
    ITEM_DESC             VARCHAR(100),
    BRAND                 VARCHAR(20),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(20),
    SIZE_DESC             VARCHAR(50),
    PACKING_FACTOR        DECIMAL(8, 2),
    INV_BASE_UNIT         CHAR(1),
    INV_UOM               VARCHAR(20)       NOT NULL,
    INV_QTY               DECIMAL(10, 4)    NOT NULL,
    FOC_BASE_UNIT         CHAR(1),
    FOC_UOM               VARCHAR(20),
    FOC_QTY               DECIMAL(10, 4),
    UNIT_PRICE            DECIMAL(15, 4),
    DISCOUNT_AMOUNT       DECIMAL(15, 4)    NOT NULL,
    DISCOUNT_PERCENT      DECIMAL(4, 2),
    NET_PRICE             DECIMAL(15, 4),
    ITEM_AMOUNT           DECIMAL(15, 4)    NOT NULL,
    NET_AMOUNT            DECIMAL(15, 4)    NOT NULL,
    ITEM_SHARED_COST      DECIMAL(15, 4),
    ITEM_GROSS_AMOUNT     DECIMAL(15, 4),
    ITEM_REMARKS          VARCHAR(100),
    LINE_REF_NO           VARCHAR(20),
    PRIMARY KEY (LINE_SEQ_NO, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: INV_DETAIL_EXTENDED 
--

CREATE TABLE INV_DETAIL_EXTENDED(
    INV_OID         DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (LINE_SEQ_NO, FIELD_NAME, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: INV_HEADER 
--

CREATE TABLE INV_HEADER(
    INV_OID                        DECIMAL(38, 0)    NOT NULL,
    INV_NO                         VARCHAR(20)       NOT NULL,
    DOC_ACTION                     CHAR(1)           NOT NULL,
    ACTION_DATE                    DATETIME          NOT NULL,
    INV_TYPE                       CHAR(3)           NOT NULL,
    INV_DATE                       DATETIME          NOT NULL,
    PO_NO                          VARCHAR(20),
    PO_DATE                        DATE,
    DELIVERY_NO                    VARCHAR(20),
    DELIVERY_DATE                  DATETIME,
    BUYER_OID                      DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                     VARCHAR(20)       NOT NULL,
    BUYER_NAME                     VARCHAR(100)      NOT NULL,
    BUYER_ADDR1                    VARCHAR(100),
    BUYER_ADDR2                    VARCHAR(100),
    BUYER_ADDR3                    VARCHAR(100),
    BUYER_ADDR4                    VARCHAR(100),
    BUYER_CITY                     VARCHAR(50),
    BUYER_STATE                    VARCHAR(50),
    BUYER_CTRY_CODE                CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE              VARCHAR(15),
    DEPT_CODE                      VARCHAR(20),
    DEPT_NAME                      VARCHAR(100),
    SUB_DEPT_CODE                  VARCHAR(20),
    SUB_DEPT_NAME                  VARCHAR(100),
    SUPPLIER_OID                   DECIMAL(38, 0),
    SUPPLIER_CODE                  VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME                  VARCHAR(100),
    SUPPLIER_ADDR1                 VARCHAR(100),
    SUPPLIER_ADDR2                 VARCHAR(100),
    SUPPLIER_ADDR3                 VARCHAR(100),
    SUPPLIER_ADDR4                 VARCHAR(100),
    SUPPLIER_CITY                  VARCHAR(50),
    SUPPLIER_STATE                 VARCHAR(50),
    SUPPLIER_CTRY_CODE             CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE           VARCHAR(15),
    SHIP_TO_CODE                   VARCHAR(20),
    SHIP_TO_NAME                   VARCHAR(100),
    SHIP_TO_ADDR1                  VARCHAR(100),
    SHIP_TO_ADDR2                  VARCHAR(100),
    SHIP_TO_ADDR3                  VARCHAR(100),
    SHIP_TO_ADDR4                  VARCHAR(100),
    SHIP_TO_CITY                   VARCHAR(50),
    SHIP_TO_STATE                  VARCHAR(50),
    SHIP_TO_CTRY_CODE              CHAR(2),
    SHIP_TO_POSTAL_CODE            VARCHAR(15),
    PAY_TERM_CODE                  VARCHAR(20),
    PAY_TERM_DESC                  VARCHAR(100),
    PAY_INSTRUCT                   VARCHAR(500),
    ADDITIONAL_DISCOUNT_AMOUNT     DECIMAL(15, 4)    NOT NULL,
    ADDITIONAL_DISCOUNT_PERCENT    DECIMAL(4, 2),
    CASH_DISCOUNT_AMOUNT           DECIMAL(15, 4),
    CASH_DISCOUNT_PERCENT          DECIMAL(4, 2),
    INV_AMOUNT_NO_VAT              DECIMAL(15, 4)    NOT NULL,
    VAT_AMOUNT                     DECIMAL(15, 4)    NOT NULL,
    INV_AMOUNT_WITH_VAT            DECIMAL(15, 4)    NOT NULL,
    VAT_RATE                       DECIMAL(4, 2)     NOT NULL,
    FOOTER_LINE_1                  VARCHAR(100),
    FOOTER_LINE_2                  VARCHAR(100),
    FOOTER_LINE_3                  VARCHAR(100),
    FOOTER_LINE_4                  VARCHAR(100),
    INV_REMARKS                    VARCHAR(500),
    CTRL_STATUS                    ENUM('NEW', 'SUBMIT','VOID','VOID_OUTDATED') NOT NULL,
    PO_OID                         DECIMAL(38, 0),
    OLD_INV_NO                     VARCHAR(20),
    PRIMARY KEY (INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: INV_HEADER_EXTENDED 
--

CREATE TABLE INV_HEADER_EXTENDED(
    INV_OID         DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (INV_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: ITEM 
--

CREATE TABLE ITEM(
    ITEM_OID              BIGINT            AUTO_INCREMENT,
    BUYER_ITEM_CODE       VARCHAR(20)       NOT NULL,
    BUYER_OID             DECIMAL(38, 0)    NOT NULL,
    DOC_OID               DECIMAL(38, 0)    NOT NULL,
    ITEM_DESC             VARCHAR(100)      NOT NULL,
    SUPPLIER_ITEM_CODE    VARCHAR(20),
    BRAND                 VARCHAR(50),
    COLOUR_CODE           VARCHAR(20),
    COLOUR_DESC           VARCHAR(50),
    SIZE_CODE             VARCHAR(10),
    SIZE_DESC             VARCHAR(20),
    UOM                   VARCHAR(20)       NOT NULL,
    CLASS_CODE            VARCHAR(20),
    CLASS_DESC            VARCHAR(50),
    SUBCLASS_CODE         VARCHAR(20),
    SUBCLASS_DESC         VARCHAR(50),
    UNIT_COST             DECIMAL(15, 4)    NOT NULL,
    RETAIL_PRICE          DECIMAL(15, 4),
    ACTIVE                BOOLEAN           NOT NULL,
    CREATE_DATE           DATETIME,
    UPDATE_DATE           DATETIME,
    PRIMARY KEY (ITEM_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: ITEM_BARCODE 
--

CREATE TABLE ITEM_BARCODE(
    ITEM_OID    BIGINT         NOT NULL,
    BARCODE     VARCHAR(50)    NOT NULL,
    PRIMARY KEY (ITEM_OID, BARCODE)
)ENGINE=INNODB
;



-- 
-- TABLE: ITEM_MASTER 
--

CREATE TABLE ITEM_MASTER(
    ITEM_OID         DECIMAL(38, 0)    NOT NULL,
    ITEM_NO          VARCHAR(20)       NOT NULL,
    ITEM_TYPE        ENUM('ITEMIN','ITEMOUT') NOT NULL,
    FILE_NAME        VARCHAR(100)      NOT NULL,
    CTRL_STATUS      ENUM('NEW','SENT') NOT NULL,
    BUYER_CODE       VARCHAR(20)       NOT NULL,
    BUYER_NAME       VARCHAR(100),
    SUPPLIER_CODE    VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME    VARCHAR(100),
    PRIMARY KEY (ITEM_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: JOB 
--

CREATE TABLE JOB(
    JOB_OID            DECIMAL(38, 0)    NOT NULL,
    JOB_NAME           VARCHAR(50)       NOT NULL,
    JOB_GROUP          VARCHAR(20)       NOT NULL,
    TRIGGER_NAME       VARCHAR(50)       NOT NULL,
    TRIGGER_GROUP      VARCHAR(20)       NOT NULL,
    JOB_DESCRIPTION    VARCHAR(255)      NOT NULL,
    CRON_EXPRESSION    VARCHAR(255)      NOT NULL,
    UPDATE_DATE        DATETIME,
    UPDATE_BY          VARCHAR(50),
    ENABLED            BOOLEAN           NOT NULL,
    PRIMARY KEY (JOB_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: MODULE 
--

CREATE TABLE MODULE(
    MODULE_ID           VARCHAR(20)      NOT NULL,
    MODULE_NAME         VARCHAR(50)      NOT NULL,
    MODULE_TITLE_KEY    VARCHAR(50),
    PARENT_ID           VARCHAR(20),
    MODULE_LEVEL        SMALLINT         NOT NULL,
    SHOWABLE            BOOLEAN          NOT NULL,
    SHOW_SORT           DECIMAL(6, 0)    DEFAULT 1 NOT NULL,
    MODULE_LINK         VARCHAR(255),
    PRIMARY KEY (MODULE_ID)
)ENGINE=INNODB
;



-- 
-- TABLE: MSG_TRANSACTIONS 
--

CREATE TABLE MSG_TRANSACTIONS(
    DOC_OID                DECIMAL(38, 0)    NOT NULL,
    MSG_TYPE               VARCHAR(20)       NOT NULL,
    MSG_REF_NO             VARCHAR(20)       NOT NULL,
    BUYER_OID              DECIMAL(38, 0),
    BUYER_CODE             VARCHAR(20),
    BUYER_NAME             VARCHAR(100),
    SUPPLIER_OID           DECIMAL(38, 0),
    SUPPLIER_CODE          VARCHAR(20),
    SUPPLIER_NAME          VARCHAR(100),
    CREATE_DATE            DATETIME          NOT NULL,
    PROC_DATE              DATETIME,
    SENT_DATE              DATETIME,
    OUT_DATE               DATETIME,
    ALERT_DATE             DATETIME,
    ORIGINAL_FILENAME      VARCHAR(100),
    EXCHANGE_FILENAME      VARCHAR(100),
    REPORT_FILENAME        VARCHAR(100),
    ACTIVE                 BOOLEAN           NOT NULL,
    VALID                  BOOLEAN           NOT NULL,
    REMARKS                VARCHAR(255),
    READ_STATUS            ENUM('READ', 'UNREAD'),
    READ_DATE              DATETIME,
    GENERATED_ON_PORTAL    BOOLEAN           DEFAULT false,
    BATCH_OID              DECIMAL(38, 0),
    PRIMARY KEY (DOC_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: OID_GENERIC 
--

CREATE TABLE OID_GENERIC(
    OID    BIGINT    AUTO_INCREMENT,
    PRIMARY KEY (OID)
)ENGINE=INNODB
;



-- 
-- TABLE: OPERATION 
--

CREATE TABLE OPERATION(
    OPN_ID              VARCHAR(20)    NOT NULL,
    OPN_DESC            VARCHAR(50),
    OPN_ORDER           SMALLINT       DEFAULT 0 NOT NULL,
    IMPLICIT            BOOLEAN        NOT NULL,
    OPT_TYPE            ENUM('CREATE', 'UPDATE', 'DELETE', 'VIEW', 'SEARCH', 'OTHER') NOT NULL,
    MODULE_ID           VARCHAR(20),
    BUTTON_TITLE_KEY    VARCHAR(50),
    NEED_AUTH           BOOLEAN        NOT NULL,
    PRIMARY KEY (OPN_ID)
)ENGINE=INNODB
;



-- 
-- TABLE: OPERATION_URL 
--

CREATE TABLE OPERATION_URL(
    OPN_ID        VARCHAR(20)     NOT NULL,
    ACCESS_URL    VARCHAR(255)    NOT NULL,
    IMPLICIT      BOOLEAN         NOT NULL,
    NEED_AUTH     BOOLEAN         NOT NULL,
    PRIMARY KEY (ACCESS_URL, OPN_ID)
)ENGINE=INNODB
;



-- 
-- TABLE: PARENT_USER_TYPE 
--

CREATE TABLE PARENT_USER_TYPE(
    USER_TYPE_OID           DECIMAL(38, 0)    NOT NULL,
    PARENT_USER_TYPE_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (PARENT_USER_TYPE_OID, USER_TYPE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PASSWORD_HISTORY 
--

CREATE TABLE PASSWORD_HISTORY(
    USER_OID         DECIMAL(38, 0)    NOT NULL,
    CHANGE_DATE      DATETIME          NOT NULL,
    OLD_LOGIN_PWD    VARCHAR(150)      NOT NULL,
    ACTOR            VARCHAR(50)       NOT NULL,
    CHANGE_REASON    CHAR(1)           NOT NULL,
    PRIMARY KEY (CHANGE_DATE, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PN_DETAIL 
--

CREATE TABLE PN_DETAIL(
    PN_OID             DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO        INTEGER           NOT NULL,
    DOC_TYPE           CHAR(1)           NOT NULL,
    DOC_REF_NO         VARCHAR(20),
    DOC_DATE           DATE,
    PAY_TRANS_NO       VARCHAR(20)       NOT NULL,
    PAY_REF_NO         VARCHAR(20),
    GROSS_AMOUNT       DECIMAL(15, 4)    NOT NULL,
    DISCOUNT_AMOUNT    DECIMAL(15, 4)    NOT NULL,
    NET_AMOUNT         DECIMAL(15, 4)    NOT NULL,
    ITEM_REMARKS       VARCHAR(100),
    PRIMARY KEY (PN_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: PN_DETAIL_EXTENDED 
--

CREATE TABLE PN_DETAIL_EXTENDED(
    PN_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (PN_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: PN_HEADER 
--

CREATE TABLE PN_HEADER(
    PN_OID                  DECIMAL(38, 0)    NOT NULL,
    PN_NO                   VARCHAR(20)       NOT NULL,
    DOC_ACTION              CHAR(1)           NOT NULL,
    ACTION_DATE             DATETIME          NOT NULL,
    PN_DATE                 DATE              NOT NULL,
    BUYER_OID               DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE              VARCHAR(20)       NOT NULL,
    BUYER_NAME              VARCHAR(100)      NOT NULL,
    BUYER_ADDR1             VARCHAR(100),
    BUYER_ADDR2             VARCHAR(100),
    BUYER_ADDR3             VARCHAR(100),
    BUYER_ADDR4             VARCHAR(100),
    BUYER_CITY              VARCHAR(50),
    BUYER_STATE             VARCHAR(50),
    BUYER_CTRY_CODE         CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE       VARCHAR(15),
    SUPPLIER_OID            DECIMAL(38, 0),
    SUPPLIER_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME           VARCHAR(100),
    SUPPLIER_ADDR1          VARCHAR(100),
    SUPPLIER_ADDR2          VARCHAR(100),
    SUPPLIER_ADDR3          VARCHAR(100),
    SUPPLIER_ADDR4          VARCHAR(100),
    SUPPLIER_CITY           VARCHAR(50),
    SUPPLIER_STATE          VARCHAR(50),
    SUPPLIER_CTRY_CODE      CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE    VARCHAR(15),
    PAY_METHOD_CODE         VARCHAR(20),
    PAY_METHOD_DESC         VARCHAR(100),
    BANK_CODE               VARCHAR(20),
    TOTAL_AMOUNT            DECIMAL(15, 4)    NOT NULL,
    DISCOUNT_AMOUNT         DECIMAL(15, 4)    NOT NULL,
    NET_TOTAL_AMOUNT        DECIMAL(15, 4)    NOT NULL,
    PN_REMARKS              VARCHAR(500),
    CTRL_STATUS             ENUM('NEW', 'AMENDED', 'OUTDATED') NOT NULL,
    DUPLICATE               BOOLEAN           NOT NULL,
    PRIMARY KEY (PN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PN_HEADER_EXTENDED 
--

CREATE TABLE PN_HEADER_EXTENDED(
    PN_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (PN_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_DETAIL 
--

CREATE TABLE PO_DETAIL(
    PO_OID                    DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO               INTEGER           NOT NULL,
    BUYER_ITEM_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE        VARCHAR(20),
    BARCODE                   VARCHAR(50),
    ITEM_DESC                 VARCHAR(100),
    BRAND                     VARCHAR(20),
    COLOUR_CODE               VARCHAR(20),
    COLOUR_DESC               VARCHAR(50),
    SIZE_CODE                 VARCHAR(20),
    SIZE_DESC                 VARCHAR(50),
    PACKING_FACTOR            DECIMAL(8, 2),
    ORDER_BASE_UNIT           CHAR(1),
    ORDER_UOM                 VARCHAR(20)       NOT NULL,
    ORDER_QTY                 DECIMAL(10, 4)    NOT NULL,
    FOC_BASE_UNIT             CHAR(1),
    FOC_UOM                   VARCHAR(20),
    FOC_QTY                   DECIMAL(10, 4),
    UNIT_COST                 DECIMAL(15, 4)    NOT NULL,
    PACK_COST                 DECIMAL(15, 4),
    COST_DISCOUNT_AMOUNT      DECIMAL(15, 4)    NOT NULL,
    COST_DISCOUNT_PERCENT     DECIMAL(4, 2),
    RETAIL_DISCOUNT_AMOUNT    DECIMAL(15, 4),
    NET_UNIT_COST             DECIMAL(15, 4)    NOT NULL,
    NET_PACK_COST             DECIMAL(15, 4),
    ITEM_COST                 DECIMAL(15, 4)    NOT NULL,
    ITEM_SHARED_COST          DECIMAL(15, 4),
    ITEM_GROSS_COST           DECIMAL(15, 4),
    RETAIL_PRICE              DECIMAL(15, 4),
    ITEM_RETAIL_AMOUNT        DECIMAL(15, 4),
    ITEM_REMARKS              VARCHAR(100),
    PRIMARY KEY (PO_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_DETAIL_EXTENDED 
--

CREATE TABLE PO_DETAIL_EXTENDED(
    PO_OID          DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (PO_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_HEADER 
--

CREATE TABLE PO_HEADER(
    PO_OID                         DECIMAL(38, 0)    NOT NULL,
    PO_NO                          VARCHAR(20)       NOT NULL,
    DOC_ACTION                     CHAR(1)           NOT NULL,
    ACTION_DATE                    DATETIME          NOT NULL,
    PO_TYPE                        CHAR(3)           NOT NULL,
    PO_SUB_TYPE                    CHAR(1),
    PO_DATE                        DATE              NOT NULL,
    DELIVERY_DATE_FROM             DATETIME,
    DELIVERY_DATE_TO               DATETIME,
    EXPIRY_DATE                    DATETIME,
    BUYER_OID                      DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                     VARCHAR(20)       NOT NULL,
    BUYER_NAME                     VARCHAR(100)      NOT NULL,
    BUYER_ADDR1                    VARCHAR(100),
    BUYER_ADDR2                    VARCHAR(100),
    BUYER_ADDR3                    VARCHAR(100),
    BUYER_ADDR4                    VARCHAR(100),
    BUYER_CITY                     VARCHAR(50),
    BUYER_STATE                    VARCHAR(50),
    BUYER_CTRY_CODE                CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE              VARCHAR(15),
    DEPT_CODE                      VARCHAR(20),
    DEPT_NAME                      VARCHAR(100),
    SUB_DEPT_CODE                  VARCHAR(20),
    SUB_DEPT_NAME                  VARCHAR(100),
    SUPPLIER_OID                   DECIMAL(38, 0),
    SUPPLIER_CODE                  VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME                  VARCHAR(100),
    SUPPLIER_ADDR1                 VARCHAR(100),
    SUPPLIER_ADDR2                 VARCHAR(100),
    SUPPLIER_ADDR3                 VARCHAR(100),
    SUPPLIER_ADDR4                 VARCHAR(100),
    SUPPLIER_CITY                  VARCHAR(50),
    SUPPLIER_STATE                 VARCHAR(50),
    SUPPLIER_CTRY_CODE             CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE           VARCHAR(15),
    SHIP_TO_CODE                   VARCHAR(20),
    SHIP_TO_NAME                   VARCHAR(100),
    SHIP_TO_ADDR1                  VARCHAR(100),
    SHIP_TO_ADDR2                  VARCHAR(100),
    SHIP_TO_ADDR3                  VARCHAR(100),
    SHIP_TO_ADDR4                  VARCHAR(100),
    SHIP_TO_CITY                   VARCHAR(50),
    SHIP_TO_STATE                  VARCHAR(50),
    SHIP_TO_CTRY_CODE              CHAR(2),
    SHIP_TO_POSTAL_CODE            VARCHAR(15),
    CREDIT_TERM_CODE               VARCHAR(20),
    CREDIT_TERM_DESC               VARCHAR(100),
    TOTAL_COST                     DECIMAL(15, 4)    NOT NULL,
    ADDITIONAL_DISCOUNT_AMOUNT     DECIMAL(15, 4)    NOT NULL,
    ADDITIONAL_DISCOUNT_PERCENT    DECIMAL(4, 2),
    NET_COST                       DECIMAL(15, 4)    NOT NULL,
    GROSS_PROFIT_MARGIN            DECIMAL(5, 2),
    TOTAL_SHARED_COST              DECIMAL(15, 4),
    TOTAL_GROSS_COST               DECIMAL(15, 4),
    TOTAL_RETAIL_AMOUNT            DECIMAL(15, 4),
    ORDER_REMARKS                  VARCHAR(500),
    CTRL_STATUS                    ENUM('NEW', 'INVOICED', 'PARTIAL_INVOICED', 'CANCELLED', 'CANCELLED_INVOICED', 'AMENDED', 'OUTDATED','CREDITED') NOT NULL,
    DUPLICATE                      BOOLEAN           NOT NULL,
    PERIOD_START_DATE              DATE,
    PERIOD_END_DATE                DATE,
    PO_SUB_TYPE2                   VARCHAR(10),
    PRIMARY KEY (PO_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_HEADER_EXTENDED 
--

CREATE TABLE PO_HEADER_EXTENDED(
    PO_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (PO_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_INV_GRN_DN_MATCHING 
--

CREATE TABLE PO_INV_GRN_DN_MATCHING(
    MATCHING_OID                      DECIMAL(38, 0)    NOT NULL,
    BUYER_OID                         DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                        VARCHAR(20)       NOT NULL,
    BUYER_NAME                        VARCHAR(100)      NOT NULL,
    SUPPLIER_OID                      DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_CODE                     VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME                     VARCHAR(100)      NOT NULL,
    PO_OID                            DECIMAL(38, 0)    NOT NULL,
    PO_STORE_CODE                     VARCHAR(20),
    PO_STORE_NAME                     VARCHAR(100),
    PO_NO                             VARCHAR(20)       NOT NULL,
    PO_DATE                           DATE              NOT NULL,
    PO_AMT                            DECIMAL(15, 4)    NOT NULL,
    INV_OID                           DECIMAL(38, 0),
    INV_NO                            VARCHAR(20),
    INV_DATE                          DATETIME,
    INV_AMT                           DECIMAL(15, 4),
    DN_OID                            DECIMAL(38, 0),
    DN_NO                             VARCHAR(20),
    DN_DATE                           DATE,
    DN_AMT                            DECIMAL(15, 4),
    MATCHING_STATUS                   ENUM('PENDING','MATCHED', 'UNMATCHED', 'AMOUNT_UNMATCHED','PRICE_UNMATCHED','QTY_UNMATCHED','MATCHED_BY_DN','INSUFFICIENT_GRN','INSUFFICIENT_INV','OUTDATED') NOT NULL,
    INV_STATUS                        ENUM('PENDING','APPROVED','SYS_APPROVED') NOT NULL,
    INV_STATUS_ACTION_DATE            DATETIME,
    INV_STATUS_ACTION_REMARKS         VARCHAR(255),
    INV_STATUS_ACTION_BY              VARCHAR(50),
    CREATE_DATE                       DATETIME          NOT NULL,
    MATCHING_DATE                     DATETIME,
    SUPPLIER_STATUS                   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL,
    SUPPLIER_STATUS_ACTION_DATE       DATETIME,
    SUPPLIER_STATUS_ACTION_REMARKS    VARCHAR(255),
    SUPPLIER_STATUS_ACTION_BY         VARCHAR(50),
    QTY_STATUS                        ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL,
    PRICE_STATUS                      ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL,
    BUYER_STATUS                      ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL,
    CLOSED                            BOOLEAN           NOT NULL,
    CLOSED_BY                         VARCHAR(50),
    CLOSED_DATE                       DATETIME,
    CLOSED_REMARKS                    VARCHAR(255),
    REVISED                           BOOLEAN           NOT NULL,
    REVISED_DATE                      DATETIME,
    ACCEPT_FALG                       BOOLEAN           NOT NULL,
    PRIMARY KEY (MATCHING_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_INV_GRN_DN_MATCHING_DETAIL 
--

CREATE TABLE PO_INV_GRN_DN_MATCHING_DETAIL(
    MATCHING_OID                   DECIMAL(38, 0)    NOT NULL,
    SEQ                            INTEGER           NOT NULL,
    BUYER_ITEM_CODE                VARCHAR(20)       NOT NULL,
    ITEM_DESC                      VARCHAR(100),
    BARCODE                        VARCHAR(50),
    UOM                            VARCHAR(20),
    PO_PRICE                       DECIMAL(15, 4),
    PO_QTY                         DECIMAL(10, 4),
    PO_AMT                         DECIMAL(15, 4),
    INV_PRICE                      DECIMAL(15, 4),
    INV_QTY                        DECIMAL(10, 4),
    INV_AMT                        DECIMAL(15, 4),
    GRN_QTY                        DECIMAL(10, 4),
    DN_AMT                         DECIMAL(15, 4),
    PRICE_STATUS                   ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL,
    PRICE_STATUS_ACTION_REMARKS    VARCHAR(255),
    PRICE_STATUS_ACTION_DATE       DATETIME,
    PRICE_STATUS_ACTION_BY         VARCHAR(20),
    QTY_STATUS                     ENUM('PENDING','ACCEPTED','REJECTED'),
    QTY_STATUS_ACTION_REMARKS      VARCHAR(255),
    QTY_STATUS_ACTION_DATE         DATETIME,
    QTY_STATUS_ACTION_BY           VARCHAR(20),
    PRIMARY KEY (SEQ, MATCHING_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_INV_GRN_DN_MATCHING_GRN 
--

CREATE TABLE PO_INV_GRN_DN_MATCHING_GRN(
    MATCHING_OID    DECIMAL(38, 0)    NOT NULL,
    GRN_OID         DECIMAL(38, 0)    NOT NULL,
    GRN_NO          VARCHAR(20)       NOT NULL,
    GRN_DATE        DATE              NOT NULL,
    GRN_AMT         DECIMAL(15, 4)    NOT NULL,
    PRIMARY KEY (MATCHING_OID, GRN_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_INV_MAPPING 
--

CREATE TABLE PO_INV_MAPPING(
    PO_OID     DECIMAL(38, 0)    NOT NULL,
    INV_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (PO_OID, INV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_LOCATION 
--

CREATE TABLE PO_LOCATION(
    PO_OID                  DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO             INTEGER           NOT NULL,
    LOCATION_CODE           VARCHAR(20)       NOT NULL,
    LOCATION_NAME           VARCHAR(100),
    LOCATION_ADDR1          VARCHAR(100),
    LOCATION_ADDR2          VARCHAR(100),
    LOCATION_ADDR3          VARCHAR(100),
    LOCATION_ADDR4          VARCHAR(100),
    LOCATION_ADDR5          VARCHAR(100),
    LOCATION_CITY           VARCHAR(50),
    LOCATION_STATE          VARCHAR(50),
    LOCATION_CTRY_CODE      CHAR(2),
    LOCATION_POSTAL_CODE    VARCHAR(15),
    LOCATION_CONTACT_TEL    VARCHAR(30),
    PRIMARY KEY (PO_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_LOCATION_DETAIL 
--

CREATE TABLE PO_LOCATION_DETAIL(
    PO_OID                  DECIMAL(38, 0)    NOT NULL,
    LOCATION_LINE_SEQ_NO    INTEGER           NOT NULL,
    DETAIL_LINE_SEQ_NO      INTEGER           NOT NULL,
    LOCATION_SHIP_QTY       DECIMAL(10, 4)    NOT NULL,
    LOCATION_FOC_QTY        DECIMAL(10, 4),
    LINE_REF_NO             VARCHAR(20),
    PRIMARY KEY (PO_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: PO_LOCATION_DETAIL_EXTENDED 
--

CREATE TABLE PO_LOCATION_DETAIL_EXTENDED(
    PO_OID                  DECIMAL(38, 0)    NOT NULL,
    LOCATION_LINE_SEQ_NO    INTEGER           NOT NULL,
    DETAIL_LINE_SEQ_NO      INTEGER           NOT NULL,
    FIELD_NAME              VARCHAR(30)       NOT NULL,
    FIELD_TYPE              CHAR(1)           NOT NULL,
    INT_VALUE               INTEGER,
    FLOAT_VALUE             DECIMAL(15, 4),
    STRING_VALUE            VARCHAR(255),
    BOOL_VALUE              BOOLEAN,
    DATE_VALUE              DATETIME,
    PRIMARY KEY (FIELD_NAME, PO_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: RESERVE_MESSAGE 
--

CREATE TABLE RESERVE_MESSAGE(
    RM_OID               DECIMAL(38, 0)    NOT NULL,
    TITLE                VARCHAR(50)       NOT NULL,
    CONTENT              VARCHAR(255)      NOT NULL,
    VALID_FROM           DATE              NOT NULL,
    VALID_TO             DATE              NOT NULL,
    CREATE_BY            VARCHAR(50)       NOT NULL,
    CREATE_DATE          DATETIME          NOT NULL,
    UPDATE_BY            VARCHAR(50),
    UPDATE_DATE          DATETIME,
    ANNOUNCEMENT_TYPE    ENUM('BUYER','SUPPLIER','BOTH') NOT NULL,
    PRIMARY KEY (RM_OID)
)ENGINE=MYISAM
;



-- 
-- TABLE: RESET_PASSWORD_REQUEST_HISTORY 
--

CREATE TABLE RESET_PASSWORD_REQUEST_HISTORY(
    HASH_CODE       VARCHAR(150)    NOT NULL,
    LOGIN_ID        VARCHAR(50)     NOT NULL,
    REQUEST_TIME    DATETIME        NOT NULL,
    CLIENT_IP       VARCHAR(128)    NOT NULL,
    VALID           BOOLEAN         NOT NULL
)ENGINE=INNODB
;



-- 
-- TABLE: ROLE 
--

CREATE TABLE ROLE(
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
    PRIMARY KEY (ROLE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: ROLE_GROUP 
--

CREATE TABLE ROLE_GROUP(
    ROLE_OID     DECIMAL(38, 0)    NOT NULL,
    GROUP_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (ROLE_OID, GROUP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: ROLE_OPERATION 
--

CREATE TABLE ROLE_OPERATION(
    ROLE_OID    DECIMAL(38, 0)    NOT NULL,
    OPN_ID      VARCHAR(20)       NOT NULL,
    PRIMARY KEY (OPN_ID, ROLE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: ROLE_USER 
--

CREATE TABLE ROLE_USER(
    ROLE_OID    DECIMAL(38, 0)    NOT NULL,
    USER_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (ROLE_OID, USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_DETAIL 
--

CREATE TABLE RTV_DETAIL(
    RTV_OID                 DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO             INTEGER           NOT NULL,
    BUYER_ITEM_CODE         VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE      VARCHAR(20),
    BARCODE                 VARCHAR(50),
    ITEM_DESC               VARCHAR(100),
    BRAND                   VARCHAR(20),
    COLOUR_CODE             VARCHAR(20),
    COLOUR_DESC             VARCHAR(50),
    SIZE_CODE               VARCHAR(20),
    SIZE_DESC               VARCHAR(50),
    DO_NO                   VARCHAR(20),
    DO_DATE                 DATE,
    INV_NO                  VARCHAR(20),
    INV_DATE                DATE,
    PACKING_FACTOR          DECIMAL(8, 2),
    RETURN_BASE_UNIT        CHAR(1),
    RETURN_UOM              VARCHAR(20)       NOT NULL,
    RETURN_QTY              DECIMAL(10, 4)    NOT NULL,
    UNIT_COST               DECIMAL(15, 4)    NOT NULL,
    COST_DISCOUNT_AMOUNT    DECIMAL(15, 4),
    ITEM_COST               DECIMAL(15, 4)    NOT NULL,
    RETAIL_PRICE            DECIMAL(15, 4),
    ITEM_RETAIL_AMOUNT      DECIMAL(15, 4),
    REASON_CODE             VARCHAR(20),
    REASON_DESC             VARCHAR(100),
    LINE_REF_NO             VARCHAR(20),
    PRIMARY KEY (RTV_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_DETAIL_EXTENDED 
--

CREATE TABLE RTV_DETAIL_EXTENDED(
    RTV_OID         DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (RTV_OID, LINE_SEQ_NO, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_HEADER 
--

CREATE TABLE RTV_HEADER(
    RTV_OID                 DECIMAL(38, 0)    NOT NULL,
    RTV_NO                  VARCHAR(20)       NOT NULL,
    DOC_ACTION              CHAR(1)           NOT NULL,
    ACTION_DATE             DATETIME          NOT NULL,
    RTV_DATE                DATE              NOT NULL,
    COLLECTION_DATE         DATE,
    DO_NO                   VARCHAR(20),
    DO_DATE                 DATE,
    INV_NO                  VARCHAR(20),
    INV_DATE                DATE,
    BUYER_OID               DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE              VARCHAR(20)       NOT NULL,
    BUYER_NAME              VARCHAR(100)      NOT NULL,
    BUYER_ADDR1             VARCHAR(100),
    BUYER_ADDR2             VARCHAR(100),
    BUYER_ADDR3             VARCHAR(100),
    BUYER_ADDR4             VARCHAR(100),
    BUYER_CITY              VARCHAR(50),
    BUYER_STATE             VARCHAR(50),
    BUYER_CTRY_CODE         CHAR(2)           NOT NULL,
    BUYER_POSTAL_CODE       VARCHAR(15),
    DEPT_CODE               VARCHAR(20),
    DEPT_NAME               VARCHAR(100),
    SUB_DEPT_CODE           VARCHAR(20),
    SUB_DEPT_NAME           VARCHAR(100),
    SUPPLIER_OID            DECIMAL(38, 0),
    SUPPLIER_CODE           VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME           VARCHAR(100),
    SUPPLIER_ADDR1          VARCHAR(100),
    SUPPLIER_ADDR2          VARCHAR(100),
    SUPPLIER_ADDR3          VARCHAR(100),
    SUPPLIER_ADDR4          VARCHAR(100),
    SUPPLIER_CITY           VARCHAR(50),
    SUPPLIER_STATE          VARCHAR(50),
    SUPPLIER_CTRY_CODE      CHAR(2)           NOT NULL,
    SUPPLIER_POSTAL_CODE    VARCHAR(15),
    TOTAL_COST              DECIMAL(15, 4)    NOT NULL,
    REASON_CODE             VARCHAR(20),
    REASON_DESC             VARCHAR(100),
    RTV_REMARKS             VARCHAR(500),
    CTRL_STATUS             ENUM('NEW', 'AMENDED', 'OUTDATED') NOT NULL,
    DUPLICATE               BOOLEAN           NOT NULL,
    PRIMARY KEY (RTV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_HEADER_EXTENDED 
--

CREATE TABLE RTV_HEADER_EXTENDED(
    RTV_OID         DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (RTV_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_LOCATION 
--

CREATE TABLE RTV_LOCATION(
    RTV_OID                 DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO             INTEGER           NOT NULL,
    LOCATION_CODE           VARCHAR(20)       NOT NULL,
    LOCATION_NAME           VARCHAR(100),
    LOCATION_ADDR1          VARCHAR(100),
    LOCATION_ADDR2          VARCHAR(100),
    LOCATION_ADDR3          VARCHAR(100),
    LOCATION_ADDR4          VARCHAR(100),
    LOCATION_ADDR5          VARCHAR(100),
    LOCATION_CITY           VARCHAR(50),
    LOCATION_STATE          VARCHAR(50),
    LOCATION_CTRY_CODE      CHAR(2),
    LOCATION_POSTAL_CODE    VARCHAR(15),
    PRIMARY KEY (LINE_SEQ_NO, RTV_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_LOCATION_DETAIL 
--

CREATE TABLE RTV_LOCATION_DETAIL(
    LOCATION_SHIP_QTY       DECIMAL(10, 4)    NOT NULL,
    LOCATION_FOC_QTY        DECIMAL(10, 4),
    RTV_OID                 DECIMAL(38, 0)    NOT NULL,
    LOCATION_LINE_SEQ_NO    INTEGER           NOT NULL,
    DETAIL_LINE_SEQ_NO      INTEGER           NOT NULL,
    PRIMARY KEY (RTV_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: RTV_LOCATION_DETAIL_EXTENDED 
--

CREATE TABLE RTV_LOCATION_DETAIL_EXTENDED(
    RTV_OID                 DECIMAL(38, 0)    NOT NULL,
    LOCATION_LINE_SEQ_NO    INTEGER           NOT NULL,
    DETAIL_LINE_SEQ_NO      INTEGER           NOT NULL,
    FIELD_NAME              VARCHAR(30)       NOT NULL,
    FIELD_TYPE              CHAR(1)           NOT NULL,
    INT_VALUE               INTEGER,
    FLOAT_VALUE             DECIMAL(15, 4),
    STRING_VALUE            VARCHAR(255),
    BOOL_VALUE              BOOLEAN,
    DATE_VALUE              DATETIME,
    PRIMARY KEY (FIELD_NAME, RTV_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: RUNNING_NUMBER 
--

CREATE TABLE RUNNING_NUMBER(
    COMPANY_OID    DECIMAL(38, 0)    NOT NULL,
    NUMBER_TYPE    CHAR(1)           NOT NULL,
    MAX_NUMBER     BIGINT,
    PRIMARY KEY (COMPANY_OID, NUMBER_TYPE)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_DATE 
--

CREATE TABLE SALES_DATE(
    SALES_OID      DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO    INTEGER           NOT NULL,
    SALES_DATE     DATE              NOT NULL,
    PRIMARY KEY (LINE_SEQ_NO, SALES_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_DATE_LOCATION_DETAIL 
--

CREATE TABLE SALES_DATE_LOCATION_DETAIL(
    SALES_OID                DECIMAL(38, 0)    NOT NULL,
    DETAIL_LINE_SEQ_NO       INTEGER           NOT NULL,
    DATE_LINE_SEQ_NO         INTEGER           NOT NULL,
    LOCATION_LINE_SEQ_NO     INTEGER           NOT NULL,
    SALES_QTY                DECIMAL(10, 4)    NOT NULL,
    ITEM_COST                DECIMAL(15, 4),
    SALES_PRICE              DECIMAL(15, 4),
    SALES_AMOUNT             DECIMAL(15, 4),
    SALES_DISCOUNT_AMOUNT    DECIMAL(15, 4),
    VAT_AMOUNT               DECIMAL(15, 4),
    NET_SALES_AMOUNT         DECIMAL(15, 4),
    LINE_REF_NO              VARCHAR(20),
    PRIMARY KEY (DETAIL_LINE_SEQ_NO, DATE_LINE_SEQ_NO, SALES_OID, LOCATION_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_DATE_LOCATION_DETAIL_EXTENDED 
--

CREATE TABLE SALES_DATE_LOCATION_DETAIL_EXTENDED(
    FIELD_NAME              VARCHAR(30)       NOT NULL,
    FIELD_TYPE              CHAR(1)           NOT NULL,
    INT_VALUE               INTEGER,
    FLOAT_VALUE             DECIMAL(15, 4),
    STRING_VALUE            VARCHAR(255),
    BOOL_VALUE              BOOLEAN,
    DATE_VALUE              DATETIME,
    SALES_OID               DECIMAL(38, 0)    NOT NULL,
    DETAIL_LINE_SEQ_NO      INTEGER           NOT NULL,
    DATE_LINE_SEQ_NO        INTEGER           NOT NULL,
    LOCATION_LINE_SEQ_NO    INTEGER           NOT NULL,
    PRIMARY KEY (FIELD_NAME, SALES_OID, DETAIL_LINE_SEQ_NO, DATE_LINE_SEQ_NO, LOCATION_LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_DETAIL 
--

CREATE TABLE SALES_DETAIL(
    SALES_OID                DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO              INTEGER           NOT NULL,
    POS_ID                   VARCHAR(20),
    RECEIPT_NO               VARCHAR(20),
    RECEIPT_DATE             DATETIME,
    BUYER_ITEM_CODE          VARCHAR(20)       NOT NULL,
    SUPPLIER_ITEM_CODE       VARCHAR(20),
    BARCODE                  VARCHAR(50),
    ITEM_DESC                VARCHAR(100),
    BRAND                    VARCHAR(20),
    DEPT_CODE                VARCHAR(20),
    DEPT_NAME                VARCHAR(100),
    SUB_DEPT_CODE            VARCHAR(20),
    SUB_DEPT_NAME            VARCHAR(100),
    COLOUR_CODE              VARCHAR(20),
    COLOUR_DESC              VARCHAR(50),
    SIZE_CODE                VARCHAR(20),
    SIZE_DESC                VARCHAR(50),
    PACKING_FACTOR           DECIMAL(8, 2)     NOT NULL,
    SALES_BASE_UNIT          CHAR(1)           NOT NULL,
    SALES_UOM                VARCHAR(20),
    SALES_QTY                DECIMAL(10, 4)    NOT NULL,
    ITEM_COST                DECIMAL(15, 4),
    ITEM_SALES_AMOUNT        DECIMAL(15, 4),
    SALES_DISCOUNT_AMOUNT    DECIMAL(15, 4),
    VAT_AMOUNT               DECIMAL(15, 4),
    ITEM_NET_SALES_AMOUNT    DECIMAL(15, 4),
    ITEM_REMARKS             VARCHAR(100),
    PRIMARY KEY (SALES_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_DETAIL_EXTENDED 
--

CREATE TABLE SALES_DETAIL_EXTENDED(
    SALES_OID       DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO     INTEGER           NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (FIELD_NAME, LINE_SEQ_NO, SALES_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_HEADER 
--

CREATE TABLE SALES_HEADER(
    SALES_OID                   DECIMAL(38, 0)    NOT NULL,
    SALES_NO                    VARCHAR(20)       NOT NULL,
    DOC_ACTION                  CHAR(1)           NOT NULL,
    ACTION_DATE                 DATETIME          NOT NULL,
    SALES_DATA_TYPE             VARCHAR(3)        NOT NULL,
    SALES_DATE                  DATE              NOT NULL,
    BUYER_OID                   DECIMAL(38, 0)    NOT NULL,
    BUYER_CODE                  VARCHAR(20)       NOT NULL,
    BUYER_NAME                  VARCHAR(100),
    BUYER_ADDR1                 VARCHAR(100),
    BUYER_ADDR2                 VARCHAR(100),
    BUYER_ADDR3                 VARCHAR(100),
    BUYER_ADDR4                 VARCHAR(100),
    BUYER_CITY                  VARCHAR(50),
    BUYER_STATE                 VARCHAR(50),
    BUYER_CTRY_CODE             CHAR(2),
    BUYER_POSTAL_CODE           VARCHAR(15),
    SUPPLIER_OID                DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_CODE               VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME               VARCHAR(100),
    SUPPLIER_ADDR1              VARCHAR(100),
    SUPPLIER_ADDR2              VARCHAR(100),
    SUPPLIER_ADDR3              VARCHAR(100),
    SUPPLIER_ADDR4              VARCHAR(100),
    SUPPLIER_CITY               VARCHAR(50),
    SUPPLIER_STATE              VARCHAR(50),
    SUPPLIER_CTRY_CODE          CHAR(2),
    SUPPLIER_POSTAL_CODE        VARCHAR(15),
    STORE_CODE                  VARCHAR(20),
    STORE_NAME                  VARCHAR(100),
    STORE_ADDR1                 VARCHAR(100),
    STORE_ADDR2                 VARCHAR(100),
    STORE_ADDR3                 VARCHAR(100),
    STORE_ADDR4                 VARCHAR(100),
    STORE_CITY                  VARCHAR(50),
    STORE_STATE                 VARCHAR(50),
    STORE_CTRY_CODE             CHAR(2),
    STORE_POSTAL_CODE           VARCHAR(15),
    TOTAL_QTY                   DECIMAL(15, 4),
    TOTAL_GROSS_SALES_AMOUNT    DECIMAL(15, 4),
    TOTAL_DISCOUNT_AMOUNT       DECIMAL(15, 4),
    TOTAL_VAT_AMOUNT            DECIMAL(15, 4),
    TOTAL_NET_SALES_AMOUNT      DECIMAL(15, 4),
    PERIOD_START_DATE           DATE,
    PERIOD_END_DATE             DATE,
    PRIMARY KEY (SALES_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_HEADER_EXTENDED 
--

CREATE TABLE SALES_HEADER_EXTENDED(
    SALES_OID       DECIMAL(38, 0)    NOT NULL,
    FIELD_NAME      VARCHAR(30)       NOT NULL,
    FIELD_TYPE      CHAR(1)           NOT NULL,
    INT_VALUE       INTEGER,
    FLOAT_VALUE     DECIMAL(15, 4),
    STRING_VALUE    VARCHAR(255),
    BOOL_VALUE      BOOLEAN,
    DATE_VALUE      DATETIME,
    PRIMARY KEY (SALES_OID, FIELD_NAME)
)ENGINE=INNODB
;



-- 
-- TABLE: SALES_LOCATION 
--

CREATE TABLE SALES_LOCATION(
    SALES_OID               DECIMAL(38, 0)    NOT NULL,
    LINE_SEQ_NO             INTEGER           NOT NULL,
    LOCATION_CODE           VARCHAR(20)       NOT NULL,
    LOCATION_NAME           VARCHAR(100),
    LOCATION_ADDR1          VARCHAR(100),
    LOCATION_ADDR2          VARCHAR(100),
    LOCATION_ADDR3          VARCHAR(100),
    LOCATION_ADDR4          VARCHAR(100),
    LOCATION_CITY           VARCHAR(50),
    LOCATION_STATE          VARCHAR(50),
    LOCATION_CTRY_CODE      CHAR(2),
    LOCATION_POSTAL_CODE    VARCHAR(15),
    LOCATION_CONTACT_TEL    VARCHAR(30),
    PRIMARY KEY (SALES_OID, LINE_SEQ_NO)
)ENGINE=INNODB
;



-- 
-- TABLE: SUBCLASS 
--

CREATE TABLE SUBCLASS(
    SUBCLASS_OID     DECIMAL(38, 0)    NOT NULL,
    SUBCLASS_CODE    VARCHAR(20)       NOT NULL,
    SUBCLASS_DESC    VARCHAR(50),
    CLASS_OID        DECIMAL(38, 0),
    PRIMARY KEY (SUBCLASS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUMMARY_FIELD 
--

CREATE TABLE SUMMARY_FIELD(
    FIELD_OID          DECIMAL(38, 0)    NOT NULL,
    FIELD_ID           VARCHAR(30)       NOT NULL,
    FIELD_COLUMN       VARCHAR(50)       NOT NULL,
    FIELD_LABEL_KEY    VARCHAR(100)      NOT NULL,
    AVAILABLE          BOOLEAN           NOT NULL,
    FIELD_WIDTH        DECIMAL(3, 0),
    SORTABLE           BOOLEAN           NOT NULL,
    SHOW_ORDER         INTEGER           NOT NULL,
    SETTING_OID        DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (FIELD_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUMMARY_PAGE_SETTING 
--

CREATE TABLE SUMMARY_PAGE_SETTING(
    SETTING_OID    DECIMAL(38, 0)    NOT NULL,
    PAGE_ID        CHAR(4)           NOT NULL,
    ACCESS_TYPE    CHAR(1)           NOT NULL,
    PAGE_DESC      VARCHAR(50)       NOT NULL,
    PRIMARY KEY (SETTING_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER 
--

CREATE TABLE SUPPLIER(
    SUPPLIER_OID               DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_CODE              VARCHAR(20)       NOT NULL,
    SUPPLIER_NAME              VARCHAR(100)      NOT NULL,
    SUPPLIER_ALIAS             VARCHAR(50),
    TRANS_MODE                 VARCHAR(20),
    REG_NO                     VARCHAR(50),
    GST_REG_NO                 VARCHAR(50),
    GST_PERCENT                DECIMAL(5, 2),
    OTHER_REG_NO               VARCHAR(50),
    SUPPLIER_SOURCE            ENUM('LOCAL', 'OVERSEA'),
    BRANCH                     BOOLEAN           NOT NULL,
    ADDRESS_1                  VARCHAR(100)      NOT NULL,
    ADDRESS_2                  VARCHAR(100),
    ADDRESS_3                  VARCHAR(100),
    ADDRESS_4                  VARCHAR(100),
    STATE                      VARCHAR(50),
    POSTAL_CODE                VARCHAR(15),
    CTRY_CODE                  CHAR(2)           NOT NULL,
    CURR_CODE                  CHAR(3)           NOT NULL,
    CONTACT_NAME               VARCHAR(50),
    CONTACT_TEL                VARCHAR(30),
    CONTACT_MOBILE             VARCHAR(30),
    CONTACT_FAX                VARCHAR(30),
    CONTACT_EMAIL              VARCHAR(100),
    ACTIVE                     BOOLEAN           NOT NULL,
    BLOCKED                    BOOLEAN           NOT NULL,
    BLOCK_REMARKS              VARCHAR(100),
    CREATE_DATE                DATETIME          NOT NULL,
    CREATE_BY                  VARCHAR(50)       NOT NULL,
    UPDATE_BY                  VARCHAR(50),
    UPDATE_DATE                DATETIME,
    BLOCK_DATE                 DATETIME,
    BLOCK_BY                   VARCHAR(50),
    MBOX_ID                    VARCHAR(20),
    DEPLOYMENT_MODE            ENUM('LOCAL','REMOTE') NOT NULL,
    CHANNEL                    VARCHAR(20),
    AUTO_INV_NUMBER            BOOLEAN,
    START_NUMBER               INTEGER,
    CLIENT_ENABLED             BOOLEAN           NOT NULL,
    REQUIRE_REPORT             BOOLEAN           NOT NULL,
    REQUIRE_TRANSLATION_IN     BOOLEAN           NOT NULL,
    REQUIRE_TRANSLATION_OUT    BOOLEAN           NOT NULL,
    LOGO                       MEDIUMBLOB,
    SET_OID                    DECIMAL(38, 0),
    LIVE_DATE                  DATE,
    PRIMARY KEY (SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER_ADMIN_ROLLOUT 
--

CREATE TABLE SUPPLIER_ADMIN_ROLLOUT(
    SUPPLIER_OID        DECIMAL(38, 0)    NOT NULL,
    PASSWD_SEND_DATE    DATETIME          NOT NULL,
    LIVE_DATE           DATETIME          NOT NULL,
    BATCH_NO            VARCHAR(50),
    PRIMARY KEY (SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER_MSG_SETTING 
--

CREATE TABLE SUPPLIER_MSG_SETTING(
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    MSG_TYPE        VARCHAR(20)       NOT NULL,
    RCPS_ADDRS      VARCHAR(255),
    EXCLUDE_SUCC    BOOLEAN           NOT NULL,
    FILE_FORMAT     VARCHAR(20),
    PRIMARY KEY (MSG_TYPE, SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER_ROLE 
--

CREATE TABLE SUPPLIER_ROLE(
    SUPPLIER_OID    DECIMAL(38, 0)    NOT NULL,
    ROLE_OID        DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (ROLE_OID, SUPPLIER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER_SET 
--

CREATE TABLE SUPPLIER_SET(
    SET_OID            DECIMAL(38, 0)    NOT NULL,
    SET_ID             VARCHAR(20)       NOT NULL,
    SET_DESCRIPTION    VARCHAR(150),
    PRIMARY KEY (SET_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: SUPPLIER_SHARED 
--

CREATE TABLE SUPPLIER_SHARED(
    SELF_SUP_OID     DECIMAL(38, 0)    NOT NULL,
    GRANT_SUP_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (SELF_SUP_OID, GRANT_SUP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: TERM_CONDITION 
--

CREATE TABLE TERM_CONDITION(
    TC_OID              DECIMAL(38, 0)    NOT NULL,
    SEQ                 SMALLINT          NOT NULL,
    TC_CODE             VARCHAR(20),
    TERM_CONDITION_1    VARCHAR(100),
    TERM_CONDITION_2    VARCHAR(100),
    TERM_CONDITION_3    VARCHAR(100),
    TERM_CONDITION_4    VARCHAR(100),
    DEFAULT_SELECTED    BOOLEAN           NOT NULL,
    SUPPLIER_OID        DECIMAL(38, 0),
    PRIMARY KEY (TC_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: TOOLTIP 
--

CREATE TABLE TOOLTIP(
    FIELD_OID            DECIMAL(38, 0)    NOT NULL,
    TOOLTIP_FIELD_OID    DECIMAL(38, 0)    NOT NULL,
    SHOW_ORDER           INTEGER           NOT NULL,
    PRIMARY KEY (FIELD_OID, TOOLTIP_FIELD_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: TRADING_PARTNER 
--

CREATE TABLE TRADING_PARTNER(
    TP_OID                     DECIMAL(38, 0)    NOT NULL,
    BUYER_SUPPLIER_CODE        VARCHAR(20)       NOT NULL,
    SUPPLIER_BUYER_CODE        VARCHAR(20),
    BUYER_CONTACT_PERSON       VARCHAR(50)       NOT NULL,
    BUYER_CONTACT_TEL          VARCHAR(30)       NOT NULL,
    BUYER_CONTACT_MOBILE       VARCHAR(30),
    BUYER_CONTACT_FAX          VARCHAR(30),
    BUYER_CONTACT_EMAIL        VARCHAR(100)      NOT NULL,
    SUPPLIER_CONTACT_PERSON    VARCHAR(50)       NOT NULL,
    SUPPLIER_CONTACT_TEL       VARCHAR(30)       NOT NULL,
    SUPPLIER_CONTACT_MOBILE    VARCHAR(30),
    SUPPLIER_CONTACT_FAX       VARCHAR(30),
    SUPPLIER_CONTACT_EMAIL     VARCHAR(100)      NOT NULL,
    ACTIVE                     BOOLEAN           NOT NULL,
    CREATE_DATE                DATETIME          NOT NULL,
    CREATE_BY                  VARCHAR(50)       NOT NULL,
    UPDATE_DATE                DATETIME,
    UPDATE_BY                  VARCHAR(50),
    CONCESSIVE                 BOOLEAN,
    BUYER_OID                  DECIMAL(38, 0)    NOT NULL,
    SUPPLIER_OID               DECIMAL(38, 0)    NOT NULL,
    TC_OID                     DECIMAL(38, 0),
    PRIMARY KEY (TP_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: TRANSACTION_BATCH 
--

CREATE TABLE TRANSACTION_BATCH(
    BATCH_OID            DECIMAL(38, 0)    NOT NULL,
    BATCH_NO             VARCHAR(50)       NOT NULL,
    BATCH_FILENAME       VARCHAR(100)      NOT NULL,
    CREATE_DATE          DATETIME          NOT NULL,
    ALERT_SENDER_DATE    DATETIME,
    PRIMARY KEY (BATCH_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_CLASS 
--

CREATE TABLE USER_CLASS(
    USER_OID     DECIMAL(38, 0)    NOT NULL,
    CLASS_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, CLASS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_PROFILE 
--

CREATE TABLE USER_PROFILE(
    USER_OID               DECIMAL(38, 0)    NOT NULL,
    USER_NAME              VARCHAR(50)       NOT NULL,
    SALUTATION             VARCHAR(20),
    LOGIN_ID               VARCHAR(50)       NOT NULL,
    LOGIN_PWD              CHAR(128),
    LOGIN_MODE             ENUM('PASSWORD', 'AD') NOT NULL,
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
    PRIMARY KEY (USER_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_SESSION 
--

CREATE TABLE USER_SESSION(
    SESSION_ID     VARCHAR(100)      NOT NULL,
    USER_OID       DECIMAL(38, 0)    NOT NULL,
    CREATE_DATE    DATETIME          NOT NULL,
    PRIMARY KEY (SESSION_ID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_SUBCLASS 
--

CREATE TABLE USER_SUBCLASS(
    USER_OID        DECIMAL(38, 0)    NOT NULL,
    SUBCLASS_OID    DECIMAL(38, 0)    NOT NULL,
    PRIMARY KEY (USER_OID, SUBCLASS_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_TYPE 
--

CREATE TABLE USER_TYPE(
    USER_TYPE_OID     DECIMAL(38, 0)    NOT NULL,
    USER_TYPE_ID      VARCHAR(20)       NOT NULL,
    USER_TYPE_DESC    VARCHAR(50)       NOT NULL,
    GROUP_TYPE        ENUM('BUYER', 'SUPPLIER'),
    ROLE_TYPE         ENUM('ADMIN', 'BUYER', 'SUPPLIER') NOT NULL,
    PRIMARY KEY (USER_TYPE_OID)
)ENGINE=INNODB
;



-- 
-- TABLE: USER_TYPE_OPERATION 
--

CREATE TABLE USER_TYPE_OPERATION(
    USER_TYPE_OID    DECIMAL(38, 0)    NOT NULL,
    OPN_ID           VARCHAR(20)       NOT NULL,
    PRIMARY KEY (OPN_ID, USER_TYPE_OID)
)ENGINE=INNODB
;



-- 
-- INDEX: BUYER_CODE_UNIQUE 
--

CREATE UNIQUE INDEX BUYER_CODE_UNIQUE ON BUYER(BUYER_CODE)
;
-- 
-- INDEX: CLASS_CODE 
--

CREATE INDEX CLASS_CODE ON CLASS(CLASS_CODE)
;
-- 
-- INDEX: BUYER_ITEM_CODE 
--

CREATE INDEX BUYER_ITEM_CODE ON ITEM(BUYER_ITEM_CODE)
;
-- 
-- INDEX: CLASS_CODE_INDEX 
--

CREATE INDEX CLASS_CODE_INDEX ON ITEM(CLASS_CODE)
;
-- 
-- INDEX: SUBCLASS_CODE_INDEX 
--

CREATE INDEX SUBCLASS_CODE_INDEX ON ITEM(SUBCLASS_CODE)
;
-- 
-- INDEX: HASH_CODE_UNIQUE 
--

CREATE UNIQUE INDEX HASH_CODE_UNIQUE ON RESET_PASSWORD_REQUEST_HISTORY(HASH_CODE)
;
-- 
-- INDEX: SUBCLASS_CODE 
--

CREATE INDEX SUBCLASS_CODE ON SUBCLASS(SUBCLASS_CODE)
;
-- 
-- INDEX: SUPPLIER_CODE_UNIQUE 
--

CREATE UNIQUE INDEX SUPPLIER_CODE_UNIQUE ON SUPPLIER(SUPPLIER_CODE)
;
-- 
-- INDEX: SET_ID_UNIQUE 
--

CREATE UNIQUE INDEX SET_ID_UNIQUE ON SUPPLIER_SET(SET_ID)
;
-- 
-- INDEX: LOGIN_ID_UNIQUE 
--

CREATE UNIQUE INDEX LOGIN_ID_UNIQUE ON USER_PROFILE(LOGIN_ID)
;
-- 
-- TABLE: `GROUP` 
--

ALTER TABLE `GROUP` ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE `GROUP` ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE `GROUP` ADD 
    FOREIGN KEY (USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;


-- 
-- TABLE: ALLOWED_ACCESS_COMPANY 
--

ALTER TABLE ALLOWED_ACCESS_COMPANY ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: AUDIT_ACCESS 
--

ALTER TABLE AUDIT_ACCESS ADD 
    FOREIGN KEY (USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;


-- 
-- TABLE: AUDIT_TRAIL 
--

ALTER TABLE AUDIT_TRAIL ADD 
    FOREIGN KEY (SESSION_OID)
    REFERENCES AUDIT_SESSION(SESSION_OID)
;


-- 
-- TABLE: BUYER 
--

ALTER TABLE BUYER ADD 
    FOREIGN KEY (CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE BUYER ADD 
    FOREIGN KEY (CURR_CODE)
    REFERENCES CURRENCY(CURR_CODE)
;


-- 
-- TABLE: BUYER_GIVEN_SUPPLIER_OPERATION 
--

ALTER TABLE BUYER_GIVEN_SUPPLIER_OPERATION ADD 
    FOREIGN KEY (OPN_ID)
    REFERENCES OPERATION(OPN_ID)
;

ALTER TABLE BUYER_GIVEN_SUPPLIER_OPERATION ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: BUYER_MSG_SETTING 
--

ALTER TABLE BUYER_MSG_SETTING ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: BUYER_MSG_SETTING_REPORT 
--

ALTER TABLE BUYER_MSG_SETTING_REPORT ADD 
    FOREIGN KEY (BUYER_OID, MSG_TYPE)
    REFERENCES BUYER_MSG_SETTING(BUYER_OID, MSG_TYPE)
;


-- 
-- TABLE: BUYER_OPERATION 
--

ALTER TABLE BUYER_OPERATION ADD 
    FOREIGN KEY (OPN_ID)
    REFERENCES OPERATION(OPN_ID)
;

ALTER TABLE BUYER_OPERATION ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: BUYER_RULE 
--

ALTER TABLE BUYER_RULE ADD 
    FOREIGN KEY (RULE_OID)
    REFERENCES BUSINESS_RULE(RULE_OID)
;

ALTER TABLE BUYER_RULE ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: BUYER_STORE 
--

ALTER TABLE BUYER_STORE ADD 
    FOREIGN KEY (STORE_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE BUYER_STORE ADD 
    FOREIGN KEY (AREA_OID)
    REFERENCES BUYER_STORE_AREA(AREA_OID)
;


-- 
-- TABLE: BUYER_STORE_AREA_USER 
--

ALTER TABLE BUYER_STORE_AREA_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: BUYER_STORE_USER 
--

ALTER TABLE BUYER_STORE_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: CC_DETAIL 
--

ALTER TABLE CC_DETAIL ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES CC_HEADER(INV_OID)
;


-- 
-- TABLE: CC_DETAIL_EXTENDED 
--

ALTER TABLE CC_DETAIL_EXTENDED ADD 
    FOREIGN KEY (INV_OID, LINE_SEQ_NO)
    REFERENCES CC_DETAIL(INV_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: CC_HEADER 
--

ALTER TABLE CC_HEADER ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: CC_HEADER_EXTENDED 
--

ALTER TABLE CC_HEADER_EXTENDED ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES CC_HEADER(INV_OID)
;


-- 
-- TABLE: CLASS 
--

ALTER TABLE CLASS ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: CN_DETAIL 
--

ALTER TABLE CN_DETAIL ADD 
    FOREIGN KEY (CN_OID)
    REFERENCES CN_HEADER(CN_OID)
;


-- 
-- TABLE: CN_DETAIL_EXTENDED 
--

ALTER TABLE CN_DETAIL_EXTENDED ADD 
    FOREIGN KEY (CN_OID, LINE_SEQ_NO)
    REFERENCES CN_DETAIL(CN_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: CN_HEADER 
--

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (CN_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE CN_HEADER ADD 
    FOREIGN KEY (STORE_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;


-- 
-- TABLE: CN_HEADER_EXTENDED 
--

ALTER TABLE CN_HEADER_EXTENDED ADD 
    FOREIGN KEY (CN_OID)
    REFERENCES CN_HEADER(CN_OID)
;


-- 
-- TABLE: DN_DETAIL 
--

ALTER TABLE DN_DETAIL ADD 
    FOREIGN KEY (DN_OID)
    REFERENCES DN_HEADER(DN_OID)
;


-- 
-- TABLE: DN_DETAIL_EXTENDED 
--

ALTER TABLE DN_DETAIL_EXTENDED ADD 
    FOREIGN KEY (DN_OID, LINE_SEQ_NO)
    REFERENCES DN_DETAIL(DN_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: DN_HEADER 
--

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (STORE_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE DN_HEADER ADD 
    FOREIGN KEY (DN_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: DN_HEADER_EXTENDED 
--

ALTER TABLE DN_HEADER_EXTENDED ADD 
    FOREIGN KEY (DN_OID)
    REFERENCES DN_HEADER(DN_OID)
;


-- 
-- TABLE: DO_DETAIL 
--

ALTER TABLE DO_DETAIL ADD 
    FOREIGN KEY (DO_OID)
    REFERENCES DO_HEADER(DO_OID)
;


-- 
-- TABLE: DO_DETAIL_EXTENDED 
--

ALTER TABLE DO_DETAIL_EXTENDED ADD 
    FOREIGN KEY (DO_OID, LINE_SEQ_NO)
    REFERENCES DO_DETAIL(DO_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: DO_HEADER 
--

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (SHIP_TO_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE DO_HEADER ADD 
    FOREIGN KEY (DO_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: DO_HEADER_EXTENDED 
--

ALTER TABLE DO_HEADER_EXTENDED ADD 
    FOREIGN KEY (DO_OID)
    REFERENCES DO_HEADER(DO_OID)
;


-- 
-- TABLE: DOC_CLASS 
--

ALTER TABLE DOC_CLASS ADD 
    FOREIGN KEY (DOC_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: DOC_SUBCLASS 
--

ALTER TABLE DOC_SUBCLASS ADD 
    FOREIGN KEY (DOC_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: FAVOURITE_LIST 
--

ALTER TABLE FAVOURITE_LIST ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: FAVOURITE_LIST_SUPPLIER 
--

ALTER TABLE FAVOURITE_LIST_SUPPLIER ADD 
    FOREIGN KEY (LIST_OID)
    REFERENCES FAVOURITE_LIST(LIST_OID)
;

ALTER TABLE FAVOURITE_LIST_SUPPLIER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: GI_DETAIL 
--

ALTER TABLE GI_DETAIL ADD 
    FOREIGN KEY (GI_OID)
    REFERENCES GI_HEADER(GI_OID)
;


-- 
-- TABLE: GI_DETAIL_EXTENDED 
--

ALTER TABLE GI_DETAIL_EXTENDED ADD 
    FOREIGN KEY (GI_OID, LINE_SEQ_NO)
    REFERENCES GI_DETAIL(GI_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: GI_HEADER 
--

ALTER TABLE GI_HEADER ADD 
    FOREIGN KEY (GI_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;

ALTER TABLE GI_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE GI_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: GI_HEADER_EXTENDED 
--

ALTER TABLE GI_HEADER_EXTENDED ADD 
    FOREIGN KEY (GI_OID)
    REFERENCES GI_HEADER(GI_OID)
;


-- 
-- TABLE: GRN_DETAIL 
--

ALTER TABLE GRN_DETAIL ADD 
    FOREIGN KEY (GRN_OID)
    REFERENCES GRN_HEADER(GRN_OID)
;


-- 
-- TABLE: GRN_DETAIL_EXTENDED 
--

ALTER TABLE GRN_DETAIL_EXTENDED ADD 
    FOREIGN KEY (GRN_OID, LINE_SEQ_NO)
    REFERENCES GRN_DETAIL(GRN_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: GRN_HEADER 
--

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (RECEIVE_STORE_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE GRN_HEADER ADD 
    FOREIGN KEY (GRN_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: GRN_HEADER_EXTENDED 
--

ALTER TABLE GRN_HEADER_EXTENDED ADD 
    FOREIGN KEY (GRN_OID)
    REFERENCES GRN_HEADER(GRN_OID)
;


-- 
-- TABLE: GROUP_SUPPLIER 
--

ALTER TABLE GROUP_SUPPLIER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES `GROUP`(GROUP_OID)
;


-- 
-- TABLE: GROUP_TRADING_PARTNER 
--

ALTER TABLE GROUP_TRADING_PARTNER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES `GROUP`(GROUP_OID)
;


-- 
-- TABLE: GROUP_USER 
--

ALTER TABLE GROUP_USER ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES `GROUP`(GROUP_OID)
;

ALTER TABLE GROUP_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: INV_DETAIL 
--

ALTER TABLE INV_DETAIL ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES INV_HEADER(INV_OID)
;


-- 
-- TABLE: INV_DETAIL_EXTENDED 
--

ALTER TABLE INV_DETAIL_EXTENDED ADD 
    FOREIGN KEY (INV_OID, LINE_SEQ_NO)
    REFERENCES INV_DETAIL(INV_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: INV_HEADER 
--

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (SHIP_TO_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE INV_HEADER ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: INV_HEADER_EXTENDED 
--

ALTER TABLE INV_HEADER_EXTENDED ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES INV_HEADER(INV_OID)
;


-- 
-- TABLE: ITEM 
--

ALTER TABLE ITEM ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE ITEM ADD 
    FOREIGN KEY (DOC_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: ITEM_BARCODE 
--

ALTER TABLE ITEM_BARCODE ADD 
    FOREIGN KEY (ITEM_OID)
    REFERENCES ITEM(ITEM_OID)
;


-- 
-- TABLE: ITEM_MASTER 
--

ALTER TABLE ITEM_MASTER ADD 
    FOREIGN KEY (ITEM_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: MSG_TRANSACTIONS 
--

ALTER TABLE MSG_TRANSACTIONS ADD 
    FOREIGN KEY (BATCH_OID)
    REFERENCES TRANSACTION_BATCH(BATCH_OID)
;

ALTER TABLE MSG_TRANSACTIONS ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE MSG_TRANSACTIONS ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: OPERATION 
--

ALTER TABLE OPERATION ADD 
    FOREIGN KEY (MODULE_ID)
    REFERENCES MODULE(MODULE_ID)
;


-- 
-- TABLE: OPERATION_URL 
--

ALTER TABLE OPERATION_URL ADD 
    FOREIGN KEY (OPN_ID)
    REFERENCES OPERATION(OPN_ID)
;


-- 
-- TABLE: PARENT_USER_TYPE 
--

ALTER TABLE PARENT_USER_TYPE ADD 
    FOREIGN KEY (PARENT_USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;

ALTER TABLE PARENT_USER_TYPE ADD 
    FOREIGN KEY (USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;


-- 
-- TABLE: PASSWORD_HISTORY 
--

ALTER TABLE PASSWORD_HISTORY ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: PN_DETAIL 
--

ALTER TABLE PN_DETAIL ADD 
    FOREIGN KEY (PN_OID)
    REFERENCES PN_HEADER(PN_OID)
;


-- 
-- TABLE: PN_DETAIL_EXTENDED 
--

ALTER TABLE PN_DETAIL_EXTENDED ADD 
    FOREIGN KEY (PN_OID, LINE_SEQ_NO)
    REFERENCES PN_DETAIL(PN_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: PN_HEADER 
--

ALTER TABLE PN_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE PN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE PN_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE PN_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE PN_HEADER ADD 
    FOREIGN KEY (PN_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: PN_HEADER_EXTENDED 
--

ALTER TABLE PN_HEADER_EXTENDED ADD 
    FOREIGN KEY (PN_OID)
    REFERENCES PN_HEADER(PN_OID)
;


-- 
-- TABLE: PO_DETAIL 
--

ALTER TABLE PO_DETAIL ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;


-- 
-- TABLE: PO_DETAIL_EXTENDED 
--

ALTER TABLE PO_DETAIL_EXTENDED ADD 
    FOREIGN KEY (PO_OID, LINE_SEQ_NO)
    REFERENCES PO_DETAIL(PO_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: PO_HEADER 
--

ALTER TABLE PO_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE PO_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE PO_HEADER ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: PO_HEADER_EXTENDED 
--

ALTER TABLE PO_HEADER_EXTENDED ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;


-- 
-- TABLE: PO_INV_GRN_DN_MATCHING 
--

ALTER TABLE PO_INV_GRN_DN_MATCHING ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE PO_INV_GRN_DN_MATCHING ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE PO_INV_GRN_DN_MATCHING ADD 
    FOREIGN KEY (DN_OID)
    REFERENCES DN_HEADER(DN_OID)
;

ALTER TABLE PO_INV_GRN_DN_MATCHING ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES INV_HEADER(INV_OID)
;

ALTER TABLE PO_INV_GRN_DN_MATCHING ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;


-- 
-- TABLE: PO_INV_GRN_DN_MATCHING_DETAIL 
--

ALTER TABLE PO_INV_GRN_DN_MATCHING_DETAIL ADD 
    FOREIGN KEY (MATCHING_OID)
    REFERENCES PO_INV_GRN_DN_MATCHING(MATCHING_OID)
;


-- 
-- TABLE: PO_INV_GRN_DN_MATCHING_GRN 
--

ALTER TABLE PO_INV_GRN_DN_MATCHING_GRN ADD 
    FOREIGN KEY (MATCHING_OID)
    REFERENCES PO_INV_GRN_DN_MATCHING(MATCHING_OID)
;

ALTER TABLE PO_INV_GRN_DN_MATCHING_GRN ADD 
    FOREIGN KEY (GRN_OID)
    REFERENCES GRN_HEADER(GRN_OID)
;


-- 
-- TABLE: PO_INV_MAPPING 
--

ALTER TABLE PO_INV_MAPPING ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;

ALTER TABLE PO_INV_MAPPING ADD 
    FOREIGN KEY (INV_OID)
    REFERENCES INV_HEADER(INV_OID)
;


-- 
-- TABLE: PO_LOCATION 
--

ALTER TABLE PO_LOCATION ADD 
    FOREIGN KEY (PO_OID)
    REFERENCES PO_HEADER(PO_OID)
;

ALTER TABLE PO_LOCATION ADD 
    FOREIGN KEY (LOCATION_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;


-- 
-- TABLE: PO_LOCATION_DETAIL 
--

ALTER TABLE PO_LOCATION_DETAIL ADD 
    FOREIGN KEY (PO_OID, LOCATION_LINE_SEQ_NO)
    REFERENCES PO_LOCATION(PO_OID, LINE_SEQ_NO)
;

ALTER TABLE PO_LOCATION_DETAIL ADD 
    FOREIGN KEY (PO_OID, DETAIL_LINE_SEQ_NO)
    REFERENCES PO_DETAIL(PO_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: PO_LOCATION_DETAIL_EXTENDED 
--

ALTER TABLE PO_LOCATION_DETAIL_EXTENDED ADD 
    FOREIGN KEY (PO_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
    REFERENCES PO_LOCATION_DETAIL(PO_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
;


-- 
-- TABLE: ROLE 
--

ALTER TABLE ROLE ADD 
    FOREIGN KEY (USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;

ALTER TABLE ROLE ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: ROLE_GROUP 
--

ALTER TABLE ROLE_GROUP ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES ROLE(ROLE_OID)
;

ALTER TABLE ROLE_GROUP ADD 
    FOREIGN KEY (GROUP_OID)
    REFERENCES `GROUP`(GROUP_OID)
;


-- 
-- TABLE: ROLE_OPERATION 
--

ALTER TABLE ROLE_OPERATION ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES ROLE(ROLE_OID)
;

ALTER TABLE ROLE_OPERATION ADD 
    FOREIGN KEY (OPN_ID)
    REFERENCES OPERATION(OPN_ID)
;


-- 
-- TABLE: ROLE_USER 
--

ALTER TABLE ROLE_USER ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES ROLE(ROLE_OID)
;

ALTER TABLE ROLE_USER ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: RTV_DETAIL 
--

ALTER TABLE RTV_DETAIL ADD 
    FOREIGN KEY (RTV_OID)
    REFERENCES RTV_HEADER(RTV_OID)
;


-- 
-- TABLE: RTV_DETAIL_EXTENDED 
--

ALTER TABLE RTV_DETAIL_EXTENDED ADD 
    FOREIGN KEY (RTV_OID, LINE_SEQ_NO)
    REFERENCES RTV_DETAIL(RTV_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: RTV_HEADER 
--

ALTER TABLE RTV_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE RTV_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE RTV_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE RTV_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE RTV_HEADER ADD 
    FOREIGN KEY (RTV_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;


-- 
-- TABLE: RTV_HEADER_EXTENDED 
--

ALTER TABLE RTV_HEADER_EXTENDED ADD 
    FOREIGN KEY (RTV_OID)
    REFERENCES RTV_HEADER(RTV_OID)
;


-- 
-- TABLE: RTV_LOCATION 
--

ALTER TABLE RTV_LOCATION ADD 
    FOREIGN KEY (RTV_OID)
    REFERENCES RTV_HEADER(RTV_OID)
;

ALTER TABLE RTV_LOCATION ADD 
    FOREIGN KEY (LOCATION_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;


-- 
-- TABLE: RTV_LOCATION_DETAIL 
--

ALTER TABLE RTV_LOCATION_DETAIL ADD 
    FOREIGN KEY (RTV_OID, LOCATION_LINE_SEQ_NO)
    REFERENCES RTV_LOCATION(RTV_OID, LINE_SEQ_NO)
;

ALTER TABLE RTV_LOCATION_DETAIL ADD 
    FOREIGN KEY (RTV_OID, DETAIL_LINE_SEQ_NO)
    REFERENCES RTV_DETAIL(RTV_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: RTV_LOCATION_DETAIL_EXTENDED 
--

ALTER TABLE RTV_LOCATION_DETAIL_EXTENDED ADD 
    FOREIGN KEY (RTV_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
    REFERENCES RTV_LOCATION_DETAIL(RTV_OID, LOCATION_LINE_SEQ_NO, DETAIL_LINE_SEQ_NO)
;


-- 
-- TABLE: SALES_DATE 
--

ALTER TABLE SALES_DATE ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES SALES_HEADER(SALES_OID)
;


-- 
-- TABLE: SALES_DATE_LOCATION_DETAIL 
--

ALTER TABLE SALES_DATE_LOCATION_DETAIL ADD 
    FOREIGN KEY (SALES_OID, DETAIL_LINE_SEQ_NO)
    REFERENCES SALES_DETAIL(SALES_OID, LINE_SEQ_NO)
;

ALTER TABLE SALES_DATE_LOCATION_DETAIL ADD 
    FOREIGN KEY (SALES_OID, DATE_LINE_SEQ_NO)
    REFERENCES SALES_DATE(SALES_OID, LINE_SEQ_NO)
;

ALTER TABLE SALES_DATE_LOCATION_DETAIL ADD 
    FOREIGN KEY (SALES_OID, LOCATION_LINE_SEQ_NO)
    REFERENCES SALES_LOCATION(SALES_OID, LINE_SEQ_NO)
;

ALTER TABLE SALES_DATE_LOCATION_DETAIL ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES SALES_HEADER(SALES_OID)
;


-- 
-- TABLE: SALES_DATE_LOCATION_DETAIL_EXTENDED 
--

ALTER TABLE SALES_DATE_LOCATION_DETAIL_EXTENDED ADD 
    FOREIGN KEY (SALES_OID, DETAIL_LINE_SEQ_NO, DATE_LINE_SEQ_NO, LOCATION_LINE_SEQ_NO)
    REFERENCES SALES_DATE_LOCATION_DETAIL(SALES_OID, DETAIL_LINE_SEQ_NO, DATE_LINE_SEQ_NO, LOCATION_LINE_SEQ_NO)
;


-- 
-- TABLE: SALES_DETAIL 
--

ALTER TABLE SALES_DETAIL ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES SALES_HEADER(SALES_OID)
;


-- 
-- TABLE: SALES_DETAIL_EXTENDED 
--

ALTER TABLE SALES_DETAIL_EXTENDED ADD 
    FOREIGN KEY (SALES_OID, LINE_SEQ_NO)
    REFERENCES SALES_DETAIL(SALES_OID, LINE_SEQ_NO)
;


-- 
-- TABLE: SALES_HEADER 
--

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES MSG_TRANSACTIONS(DOC_OID)
;

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (BUYER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (SUPPLIER_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;

ALTER TABLE SALES_HEADER ADD 
    FOREIGN KEY (STORE_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;


-- 
-- TABLE: SALES_HEADER_EXTENDED 
--

ALTER TABLE SALES_HEADER_EXTENDED ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES SALES_HEADER(SALES_OID)
;


-- 
-- TABLE: SALES_LOCATION 
--

ALTER TABLE SALES_LOCATION ADD 
    FOREIGN KEY (SALES_OID)
    REFERENCES SALES_HEADER(SALES_OID)
;

ALTER TABLE SALES_LOCATION ADD 
    FOREIGN KEY (LOCATION_CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;


-- 
-- TABLE: SUBCLASS 
--

ALTER TABLE SUBCLASS ADD 
    FOREIGN KEY (CLASS_OID)
    REFERENCES CLASS(CLASS_OID)
;


-- 
-- TABLE: SUMMARY_FIELD 
--

ALTER TABLE SUMMARY_FIELD ADD 
    FOREIGN KEY (SETTING_OID)
    REFERENCES SUMMARY_PAGE_SETTING(SETTING_OID)
;


-- 
-- TABLE: SUPPLIER 
--

ALTER TABLE SUPPLIER ADD 
    FOREIGN KEY (CTRY_CODE)
    REFERENCES COUNTRY(CTRY_CODE)
;

ALTER TABLE SUPPLIER ADD 
    FOREIGN KEY (CURR_CODE)
    REFERENCES CURRENCY(CURR_CODE)
;

ALTER TABLE SUPPLIER ADD 
    FOREIGN KEY (SET_OID)
    REFERENCES SUPPLIER_SET(SET_OID)
;


-- 
-- TABLE: SUPPLIER_ADMIN_ROLLOUT 
--

ALTER TABLE SUPPLIER_ADMIN_ROLLOUT ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: SUPPLIER_MSG_SETTING 
--

ALTER TABLE SUPPLIER_MSG_SETTING ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: SUPPLIER_ROLE 
--

ALTER TABLE SUPPLIER_ROLE ADD 
    FOREIGN KEY (ROLE_OID)
    REFERENCES ROLE(ROLE_OID)
;


-- 
-- TABLE: SUPPLIER_SHARED 
--

ALTER TABLE SUPPLIER_SHARED ADD 
    FOREIGN KEY (SELF_SUP_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE SUPPLIER_SHARED ADD 
    FOREIGN KEY (GRANT_SUP_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: TERM_CONDITION 
--

ALTER TABLE TERM_CONDITION ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;


-- 
-- TABLE: TOOLTIP 
--

ALTER TABLE TOOLTIP ADD 
    FOREIGN KEY (FIELD_OID)
    REFERENCES SUMMARY_FIELD(FIELD_OID)
;

ALTER TABLE TOOLTIP ADD 
    FOREIGN KEY (TOOLTIP_FIELD_OID)
    REFERENCES SUMMARY_FIELD(FIELD_OID)
;


-- 
-- TABLE: TRADING_PARTNER 
--

ALTER TABLE TRADING_PARTNER ADD 
    FOREIGN KEY (TC_OID)
    REFERENCES TERM_CONDITION(TC_OID)
;

ALTER TABLE TRADING_PARTNER ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE TRADING_PARTNER ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: USER_CLASS 
--

ALTER TABLE USER_CLASS ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: USER_PROFILE 
--

ALTER TABLE USER_PROFILE ADD 
    FOREIGN KEY (USER_TYPE)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;

ALTER TABLE USER_PROFILE ADD 
    FOREIGN KEY (SUPPLIER_OID)
    REFERENCES SUPPLIER(SUPPLIER_OID)
;

ALTER TABLE USER_PROFILE ADD 
    FOREIGN KEY (BUYER_OID)
    REFERENCES BUYER(BUYER_OID)
;


-- 
-- TABLE: USER_SESSION 
--

ALTER TABLE USER_SESSION ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: USER_SUBCLASS 
--

ALTER TABLE USER_SUBCLASS ADD 
    FOREIGN KEY (USER_OID)
    REFERENCES USER_PROFILE(USER_OID)
;


-- 
-- TABLE: USER_TYPE_OPERATION 
--

ALTER TABLE USER_TYPE_OPERATION ADD 
    FOREIGN KEY (OPN_ID)
    REFERENCES OPERATION(OPN_ID)
;

ALTER TABLE USER_TYPE_OPERATION ADD 
    FOREIGN KEY (USER_TYPE_OID)
    REFERENCES USER_TYPE(USER_TYPE_OID)
;


