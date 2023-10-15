package com.example.backedu.repository;

import com.example.backedu.dto.ReplyQnaDto;
import com.example.backedu.entity.ReplyQna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : com.example.backedu.repository
 * fileName : ReplyQnaRepository
 * author : kangtaegyung
 * date : 2023/10/09
 * description : 답변형 Qna 레포지토리
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/10/09         kangtaegyung          최초 생성
 */
@Repository
public interface ReplyQnaRepository extends JpaRepository<ReplyQna, Integer> {
    @Query(value = "SELECT BID                     AS bid " +
            "      , LPAD('⤵', (LEVEL-1))|| BOARD_TITLE AS BoardTitle " +
            "      , BOARD_CONTENT          AS boardContent " +
            "    ,BOARD_WRITER              AS boardWriter " +
            "    ,VIEW_CNT                  AS viewCnt " +
            "    ,BOARD_GROUP               AS boardGroup " +
            "    ,BOARD_PARENT              AS boardParent " +
            "    ,UUID                      AS uuid " +
            "    ,FILE_URL                  AS fileUrl " +
            "FROM TB_REPLY_QNA " +
            "WHERE BOARD_TITLE LIKE %:boardTitle% " +
            "AND   DELETE_YN = 'N' " +
            "START WITH BOARD_PARENT = 0 " +
            "CONNECT BY PRIOR BID = BOARD_PARENT " +
            "ORDER SIBLINGS BY BOARD_GROUP DESC "
            , countQuery = "SELECT count(*) FROM TB_REPLY_BOARD " +
            "WHERE BOARD_TITLE LIKE %:boardTitle% " +
            "AND   DELETE_YN = 'N' " +
            "START WITH BOARD_PARENT = 0 " +
            "CONNECT BY BOARD_PARENT = PRIOR BID " +
            "ORDER SIBLINGS BY BOARD_GROUP DESC "
            , nativeQuery = true)
    Page<ReplyQnaDto> selectByConnectByPage(
            @Param("boardTitle") String boardTitle,
            Pageable pageable
    );

    //    insert 쿼리 : 게시물 최초 생성
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO TB_REPLY_QNA " +
            "VALUES (SQ_REPLY_QNA.nextval, " +
            ":#{#replyQna.boardTitle}, " +
            ":#{#replyQna.boardContent}, " +
            ":#{#replyQna.boardWriter}, " +
            "0, " +
            "SQ_REPLY_QNA.currval, " +
            "0, " +
            ":#{#replyQna.uuid}, " +
            ":#{#replyQna.fileUrl}," +
            "'N', " +
            "TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS')," +
            "NULL, " +
            "NULL) "
            , nativeQuery = true
    )
    int insertByQna(@Param("replyQna") ReplyQna replyQna);

    //    BoardGroup 있는지 조  사
    boolean existsAllByBoardGroup(int BoardGroup);

    //    게시물 삭제 : 자식도 같이 삭제 (그룹 번호로 삭제)
    @Transactional
    @Modifying
    @Query(value = "UPDATE TB_REPLY_QNA SET DELETE_YN = 'Y', " +
            "DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') " +
            "WHERE BOARD_GROUP = :boardGroup "
            , nativeQuery = true
    )
    int removeAllByBoardGroup(@Param("boardGroup") int boardGroup);

}
