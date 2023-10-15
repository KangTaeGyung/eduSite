package com.example.backedu.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * packageName : com.example.backedu.entity
 * fileName : ReplyQna
 * author : kangtaegyung
 * date : 2023/10/09
 * description : 답변형 QNA
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/10/09         kangtaegyung          최초 생성
 */
@Entity
@Table(name="TB_REPLY_QNA")
@SequenceGenerator(
        name = "SQ_REPLY_QNA_GENERATOR"
        , sequenceName = "SQ_REPLY_QNA"
        , initialValue = 1
        , allocationSize = 1
)
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
// soft delete
@Where(clause = "DELETE_YN = 'N'")
@SQLDelete(sql = "UPDATE TB_REPLY_QNA SET DELETE_YN = 'Y', DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE BID = ?")
public class ReplyQna extends BaseTimeEntity {
    //    게시판 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "SQ_REPLY_QNA_GENERATOR"
    )
    private Integer bid;

    //    제목
    private String boardTitle;

    //    본문
    private String boardContent;

    //    작성자
    private String boardWriter;

    //    조회수
    private Integer viewCnt;

    //    그룹 번호 : 댓글이 달리는 것들은 그룹번호가 같음
    private Integer boardGroup;

    //    그룹 에서 자신의 부모 게시판번호
    private Integer boardParent;

    private String uuid;   // 첨부파일 이름 생성용 uuid

    private String fileUrl; // 첨부파일 url : s3 다운로드 주소
}
