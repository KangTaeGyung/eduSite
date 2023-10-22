package com.example.backedu.service;

import com.example.backedu.entity.Code;
import com.example.backedu.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * packageName : com.example.backedu.service
 * fileName : CodeServiceImpl
 * author : kangtaegyung
 * date : 2022/07/29
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/29         kangtaegyung          최초 생성
 */
@Service
public class CodeService {

    @Autowired
    CodeRepository codeRepository;

    public boolean createCode(Code code) {

        //            id 중복 체크
        if( code.getCodeId() != null) {
            boolean bResult = codeRepository.existsById(code.getCodeId());

//            id 중복 체크
            if(bResult) return false;

        }

//        생성 날짜 지정을 위한 날짜 라이브러리 가져오기
        LocalDateTime localDateTime = LocalDateTime.now();
//            시간 형태 지정 : 예) 2022-07-28 15:02:45
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//        생성 날짜 지정
        code.setInsertTime(localDateTime.format(formatter));

        codeRepository.save(code);

        return true;
    }

    public Page<Code> findAllTitleDesc(String title, Pageable pageable) {

        Page<Code> codePage = codeRepository.findAllByTitleContainingOrderByInsertTimeDesc(title, pageable);
        return codePage;
    }

    public List<Code> findAll() {

        List<Code> codeList = codeRepository.findAll();
        return codeList;
    }

    //    삭제 시 실행될 메소드
    public boolean removeById(String codeId) {

        if (codeRepository.existsById(codeId)) {
            codeRepository.deleteById(codeId);
            return true;
        }
        return false;
    }
}
