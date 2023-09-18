package com.example.backedu.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * packageName : com.example.backedu.entity
 * fileName : Code
 * author : kangtaegyung
 * date : 2022/07/28
 * description : 공통 날짜 인터페이스 상속받지 않음, 생성/삭제만 로직만 사용함
 *               코드 데이터는 업무적으로 수정하면 안됨 ( 생성 / 필요시 삭제 만 사용함 )
 *               생성일자는 공통 날짜 인터페이스를 사용하지 않고 자바코드로 생성함수 호출 시 직접 코딩을 진행함(서비스쪽에 있음)
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/28         kangtaegyung          최초 생성
 */
@Entity(name="TB_CODE")
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
@SQLDelete(sql = "UPDATE TB_CODE SET DELETE_YN = 'Y', DELETE_TIME=TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE CODE_ID = ?")
public class Code {
//    대/중/소분류 첫글자(L/M/S) + 3자리숫자 : 예) L001, M002
    @Id
    @Column
    private String codeId;

    //    대분류 / 중분류 / 소분류 : LARGE / MIDDLE / SMALL
    @Column
    private String category;

    //    교육 과정 이름
    @Column
    private String title;

    //    생성일시
    @Column
    private String insertTime;

    //    삭제여부
    @Column
    private String deleteYn;

    //    삭제일시
    @Column
    private String deleteTime;
}
