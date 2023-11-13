package com.example.backedu.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * packageName : com.example.backedu.entity
 * fileName : PreView
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
@Table(name = "TB_PREVIEW")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Preview {
    @Id
    private String  puuid; // 기본키

    private String  fileName;

    private String  fileUrl;
}
