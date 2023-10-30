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

    public boolean save(Code code) {

        codeRepository.save(code);

        return true;
    }

    public Page<Code> findAllTitleDesc(String title, Pageable pageable) {

        Page<Code> codePage = codeRepository.findAllByTitleContaining(title, pageable);
        return codePage;
    }

    public List<Code> findAll() {

        List<Code> codeList = codeRepository.findAll();
        return codeList;
    }
}
