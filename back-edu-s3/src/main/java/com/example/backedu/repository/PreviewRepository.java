package com.example.backedu.repository;

import com.example.backedu.entity.Preview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName : com.example.backedu.repository
 * fileName : PreviewRepository
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
public interface PreviewRepository extends JpaRepository<Preview, String > {
}
