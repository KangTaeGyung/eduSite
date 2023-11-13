package com.example.backedu.repository;

import com.example.backedu.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.example.backedu.repository
 * fileName : SectionRepository
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
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
//    like
    Page<Section> findAllByUuidAndTitleContaining(String uuid, String title, Pageable pageable);
}
