package com.example.backedu.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

/**
 * packageName : com.example.backedu.entity
 * fileName : Section
 * author : kangtaegyung
 * date : 11/12/23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 11/12/23         kangtaegyung          최초 생성
 */
@Entity
@Table(name = "TB_SECTION")
@SequenceGenerator(
        name = "SQ_SECTION_GENERATOR"
        , sequenceName = "SQ_SECTION"
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
@SQLDelete(sql = "UPDATE TB_SECTION SET DELETE_YN = 'Y', DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE SNO = ?")
public class Section extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "SQ_SECTION_GENERATOR"
    )
    private Integer sno; // 기본키, 시퀀스

    private String uuid; // FK

    private String  title;

    @Lob
    private String  content; // clob
}
