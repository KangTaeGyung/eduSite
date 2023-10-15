-- -- TB_COURSE 데이터
-- todo: fileUrl : noImage.jpg 로 넘겨서 이미지 에러 이벤트 발생시키면 됨 : '' 또는 null 로 넘기면 안됨
Insert into TB_COURSE (UUID,CODE,CONTENT,INSERT_TIME,DELETE_TIME,FILE_NAME,FILE_URL, TITLE,UPDATE_TIME)
values ('d3728f4f03d14e078e2e9d1b760fd553','L001','■ HTML/CSS/Javascript/JQuery 예제를 따라 배우는 기초 강좌<br>
■ Deep Dive : Modern Javascript<br>
■ Blog Site 웹 디자인 구축',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'course-1.jpg','noImage.jpg','Web Design',null);


-- TB_CODE 데이터
Insert into TB_CODE (CODE_ID,CATEGORY,INSERT_TIME,DELETE_TIME,TITLE,UPDATE_TIME)
values ('L001','FRONT',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'Frontend : Web Classic',null);
Insert into TB_CODE (CODE_ID,CATEGORY,INSERT_TIME,DELETE_TIME,TITLE,UPDATE_TIME)
values ('L002','FRONT',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'Frontend : Web Modern',null);
Insert into TB_CODE (CODE_ID,CATEGORY,INSERT_TIME,DELETE_TIME,TITLE,UPDATE_TIME)
values ('L003','BACK',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'Backend : Web Application',null);
Insert into TB_CODE (CODE_ID,CATEGORY,INSERT_TIME,DELETE_TIME,TITLE,UPDATE_TIME)
values ('L004','TOTAL',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'Frontend & Backend',null);
Insert into TB_CODE (CODE_ID,CATEGORY,INSERT_TIME,DELETE_TIME,TITLE,UPDATE_TIME)
values ('L005','BACK',TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),null,'Android & Backend',null);
--------------------------------------------------------
commit;