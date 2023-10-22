package com.example.backedu.repository;

import com.example.backedu.dto.CourseJoinCDto;
import com.example.backedu.dto.CourseJoinDto;
import com.example.backedu.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.example.backedu.repository
 * fileName : CoursesRepository
 * author : kangtaegyung
 * date : 2022/07/27
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/27         kangtaegyung          최초 생성
 */
@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
    public Page<Course> findAllByOrderByInsertTimeDesc(Pageable pageable);

//    event : 'Y' 인것만 가져오기 함수
    public Page<Course> findAllByEventYnOrderByInsertTimeDesc(String eventYn, Pageable pageable);

//    @Query(value = "SELECT UUID          AS uuid " +
//            "    ,CODE          AS code " +
//            "    ,CR.TITLE      AS title " +
//            "    ,CONTENT       AS content " +
//            "    ,FILE_NAME     AS fileName " +
//            "    ,EVENT_YN      AS eventYn " +
//            "    ,EVENT_TITLE   AS eventTitle " +
//            "    ,EVENT_CONTENT AS eventContent " +
//            "    ,FILE_URL      AS fileUrl " +
//            "    ,CO.CATEGORY   AS category " +
//            "FROM TB_COURSE CR " +
//            "    ,TB_CODE   CO " +
//            "WHERE CR.CODE = CO.CODE_ID " +
//            "AND  CR.DELETE_YN = 'N' " +
//            "ORDER BY CR.INSERT_TIME DESC "
//            ,countQuery = "SELECT count(*)  " +
//            "FROM TB_COURSE CR  " +
//            "    ,TB_CODE   CO  " +
//            "WHERE CR.CODE = CO.CODE_ID " +
//            "AND  CR.DELETE_YN = 'N' " +
//            "ORDER BY CR.INSERT_TIME DESC "
//            , nativeQuery = true)
//    public Page<CourseJoinDto> selectByCodeJoin(Pageable pageable);

    //    public Page<CourseJoinDto> selectByCodeJoin(Pageable pageable);

    @Query(value = "SELECT new com.example.backedu.dto.CourseJoinCDto(cr.uuid, cr.code.codeId, cr.title, cr.content, cr.fileName, cr.eventYn, cr.eventTitle, " +
            "   cr.eventContent, cr.fileUrl, co.category  ) " +
            "FROM Course cr inner join cr.code co " +
            "ORDER BY cr.insertTime desc ")
    public Page<CourseJoinCDto> selectByCodeJoin(Pageable pageable);
}
