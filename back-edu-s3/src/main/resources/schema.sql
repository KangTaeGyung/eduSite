--------------------------------------------------------
--  파일이 생성됨 - 금요일-7월-29-2022
--------------------------------------------------------
DROP SEQUENCE SQ_REPLY_QNA;
CREATE SEQUENCE SQ_REPLY_QNA START WITH 1 INCREMENT BY 1;

DROP SEQUENCE SQ_SECTION;
CREATE SEQUENCE SQ_SECTION START WITH 1 INCREMENT BY 1;

DROP TABLE TB_CODE CASCADE CONSTRAINT;

DROP TABLE TB_COURSE CASCADE CONSTRAINT;
DROP TABLE TB_SECTION CASCADE CONSTRAINT;
DROP TABLE TB_PREVIEW CASCADE CONSTRAINT;

DROP TABLE TB_REPLY_QNA CASCADE CONSTRAINT;

--------------------------------------------------------
-- todo:  DDL for Table TB_COURSE
--------------------------------------------------------
CREATE TABLE TB_COURSE
(
    UUID          VARCHAR2(1000) NOT NULL
        CONSTRAINT PK_COURSE PRIMARY KEY, -- 파일 UUID
    CODE_ID       VARCHAR2(256),
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
-- todo:  DDL for Table TB_SECTION : TB_COURSE 의 자식 테이블
--------------------------------------------------------
CREATE TABLE TB_SECTION
(
    SNO          NUMBER NOT NULL PRIMARY KEY,
    UUID          VARCHAR2(1000),         -- FK 코딩으로 참조 무결성 해결( 참조 무결성 생략 ) : TB_COURSE
    TITLE         VARCHAR2(1000),         -- 제목
    CONTENT       CLOB,                   -- 블로그 내용
    DELETE_YN     CHAR(1) DEFAULT 'N',
    INSERT_TIME   VARCHAR2(1000),
    UPDATE_TIME   VARCHAR2(1000),
    DELETE_TIME   VARCHAR2(1000)
);

--------------------------------------------------------
-- todo:  DDL for Table TB_SECTION : TB_SECTION 의 자식 테이블
-- todo: 파일 이력 남기지 않음 : 하드 삭제함
--------------------------------------------------------
CREATE TABLE TB_PREVIEW
(
    PUUID          VARCHAR2(1000) NOT NULL PRIMARY KEY,
    FILE_NAME     VARCHAR2(1000),         -- UUID 로 파일명 저장
    FILE_URL      VARCHAR2(1000)          -- 다운로드 URL
);

--------------------------------------------------------
-- todo: DDL for Table TB_CODE
-------------------------------------------------------
CREATE TABLE TB_CODE
(
    CODE_ID     CHAR(4) CONSTRAINT PK_CODE PRIMARY KEY, -- 파일 UUID,
    CATEGORY    VARCHAR2(10),
    TITLE       VARCHAR2(1000)
);


-- todo: TB_REPLY_QNA : 질문 답변형 게시판
-- todo: fileName 추가
CREATE TABLE TB_REPLY_QNA
(
    BID           NUMBER NOT NULL              -- 게시판번호
        CONSTRAINT PK_REPLY_QNA PRIMARY KEY,
    BOARD_TITLE   VARCHAR2(256),               -- 제목
    BOARD_CONTENT VARCHAR2(255),               -- 내용
    BOARD_WRITER  VARCHAR2(255),               -- 작성자
    VIEW_CNT      NUMBER DEFAULT 0,            -- 조회수
    BOARD_GROUP   NUMBER,                      -- 트리구조 최상위 부모 노드( 부모가 있을 경우 : 부모번호, 없을 경우 : 자신의 게시판번호 ), 계층형 쿼리 정렬시(Order by) 사용될 컬럼
    BOARD_PARENT  NUMBER,                      -- 자신의 부모 노드 ( 부모가 있을 경우 : 부모번호, 없을 경우 : 0 )
    UUID          VARCHAR2(1000)  NOT NULL,    -- file 첨부시 사용할 유일한 id : uuid
    FILE_URL      VARCHAR2(1000),              -- 파일 다운로드 URL
    FILE_NAME     VARCHAR2(1000),              -- 업로드 할때의 파일명 (업로드 후 내부적으로 변경된 uuid 이름으로 파일명이 재작성됨)
    DELETE_YN     VARCHAR2(1) DEFAULT 'N',
    INSERT_TIME   VARCHAR2(255),
    UPDATE_TIME   VARCHAR2(255),
    DELETE_TIME   VARCHAR2(255)
);
