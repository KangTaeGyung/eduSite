--------------------------------------------------------
--  파일이 생성됨 - 금요일-7월-29-2022
--------------------------------------------------------

DROP TABLE TB_COURSE CASCADE CONSTRAINT;
DROP TABLE TB_CODE CASCADE CONSTRAINT;

--------------------------------------------------------
--  DDL for Table TB_COURSE
--------------------------------------------------------
CREATE TABLE TB_COURSE
(
    UUID          VARCHAR2(1000) NOT NULL
        CONSTRAINT PK_COURSE PRIMARY KEY, -- 파일 UUID
    CODE          VARCHAR2(256),
    TITLE         VARCHAR2(1000),
    CONTENT       VARCHAR2(4000),
    EVENT_YN      CHAR(1) DEFAULT 'N',
    EVENT_TITLE   VARCHAR2(1000),
    EVENT_CONTENT VARCHAR2(1000),
    FILE_NAME     VARCHAR2(1000),
    FILE_URL      VARCHAR2(1000),          -- 파일 다운로드 URL
    DELETE_YN     CHAR(1) DEFAULT 'N',
    INSERT_TIME   VARCHAR2(1000),
    UPDATE_TIME   VARCHAR2(1000),
    DELETE_TIME   VARCHAR2(1000)
);

--------------------------------------------------------
--  DDL for Table TB_CODE
-------------------------------------------------------
CREATE TABLE TB_CODE
(
    CODE_ID     CHAR(4) CONSTRAINT PK_CODE PRIMARY KEY, -- 파일 UUID,
    CATEGORY    VARCHAR2(10),
    TITLE       VARCHAR2(1000),
    DELETE_YN   CHAR(1) DEFAULT 'N',
    INSERT_TIME VARCHAR2(1000),
    UPDATE_TIME VARCHAR2(1000),
    DELETE_TIME VARCHAR2(1000)
);
