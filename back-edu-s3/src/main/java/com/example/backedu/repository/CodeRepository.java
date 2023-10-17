package com.example.backedu.repository;

import com.example.backedu.entity.Code;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.example.backedu.repository
 * fileName : CodeRepository
 * author : kangtaegyung
 * date : 2022/07/28
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/28         kangtaegyung          최초 생성
 */
@Repository
public interface CodeRepository extends JpaRepository<Code, String> {
    public Page<Code> findAllByTitleContainingOrderByInsertTimeDesc(String title, Pageable pageable);
}