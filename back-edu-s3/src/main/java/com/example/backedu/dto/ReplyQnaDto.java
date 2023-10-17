package com.example.backedu.dto;

/**
 * packageName : com.example.backedu.dto
 * fileName : ReplyQnaDto
 * author : kangtaegyung
 * date : 2023/10/09
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/10/09         kangtaegyung          최초 생성
 */
public interface ReplyQnaDto {
    //    게시판 번호
    public Integer getBid();

    //    제목 : 공백 붙임
    public String getBoardTitle();

    //    본문
    public String getBoardContent();

    //    작성자
    public String getBoardWriter();

    //    조회수
    public Integer getViewCnt();

    //    그룹 번호 : 댓글이 달리는 것들은 그룹번호가 같음
    public Integer getBoardGroup();

    //    그룹 에서 자신의 부모 게시판번호
    public Integer getBoardParent();

    public String getUuid();

    public String getFileUrl();

//     * todo: fileName 추가
    public String getFileName();
}
