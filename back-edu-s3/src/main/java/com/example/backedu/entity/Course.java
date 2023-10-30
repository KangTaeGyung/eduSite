package com.example.backedu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * packageName : com.example.backedu.entity
 * fileName : Courses
 * author : kangtaegyung
 * date : 2022/07/27
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/27         kangtaegyung          최초 생성
 */
@Entity
@Table(name = "TB_COURSE")
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
@SQLDelete(sql = "UPDATE TB_COURSE SET DELETE_YN = 'Y', DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE UUID = ?")
public class Course extends BaseTimeEntity {

    //    시퀀스 번호 생성
    @Id
    private String uuid;

    //    교육 과정 이름
    private String title;

    //    교육 과정 내용
    private String content;

    //    이미지 파일명
    private String eventYn;

    //    이미지 파일명
    private String eventTitle;

    //    이미지 파일명
    private String eventContent;

    //    이미지 파일명
    private String fileName;

    private String fileUrl;

    //    교육 Type : HTML, CSS, JAVASCRIPT, JAVA, SPRINGBOOT, ANDROID
//    private String code; 연관 관계만 있고, 실제 테이블에는 FK 가 없음
    //    교육 Type : HTML, CSS, JAVASCRIPT, JAVA, SPRINGBOOT, ANDROID
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "CODE_ID")
    private Code code;
}
