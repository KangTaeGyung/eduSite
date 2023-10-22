package com.example.backedu.dto;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.io.Serializable;
import java.sql.Blob;

/**
 * packageName : com.example.backedu.dto
 * fileName : CourseJoinDto
 * author : kangtaegyung
 * date : 2023/06/29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/29         kangtaegyung          최초 생성
 */
public interface CourseJoinDto extends Serializable {

    String getUuid();

    String getCode();

    String getTitle();

    String getContent();

    String getFileName();

    String getEventYn();

    String getEventTitle();

    String getEventContent();

//    blob -> byte[] 자동 형변환 안됨 : 수동으로 컨트롤러에서 변환하거나 Blob 에서 getLength() 함수 호출해서 길이 가져옴
//    TODO: 굳이 frontend 로 보내줄 필요없음 : url 만 보내면 됨
//    Blob getBlobFile();

    String getFileUrl();

    String getCategory();
}
