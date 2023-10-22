package com.example.backedu.dto;

import com.example.backedu.entity.Course;
import lombok.*;

/**
 * packageName : com.example.backedu.dto
 * fileName : CourseDto
 * author : kangtaegyung
 * date : 10/22/23
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/22/23         kangtaegyung          최초 생성
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
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
}
