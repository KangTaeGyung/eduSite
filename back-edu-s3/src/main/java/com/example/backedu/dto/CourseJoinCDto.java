package com.example.backedu.dto;

import lombok.*;

import java.io.Serializable;

/**
 * packageName : com.example.backedu.dto
 * fileName : CourseJoinCDto
 * author : kangtaegyung
 * date : 10/21/23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/21/23         kangtaegyung          최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CourseJoinCDto implements Serializable {
    private String uuid;

    //    교육 Type : HTML, CSS, JAVASCRIPT, JAVA, SPRINGBOOT, ANDROID
    private String code;

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

    private String category;
}
