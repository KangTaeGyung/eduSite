package com.example.backedu.service;

import com.amazonaws.services.s3.AmazonS3;

import com.example.backedu.entity.Code;
import com.example.backedu.entity.ReplyQna;
import com.example.backedu.entity.Section;
import com.example.backedu.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * packageName : com.example.backedu.service
 * fileName : SectionServiceImpl
 * author : kangtaegyung
 * date : 2022/07/31
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/31         kangtaegyung          최초 생성
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SectionService {

    private final SectionRepository sectionsRepository;

    public Page<Section> findAllByUuidAndTitleContaining(String uuid, String title, Pageable pageable) {

        Page<Section> sectionList = sectionsRepository.findAllByUuidAndTitleContaining(uuid, title, pageable);

        return sectionList;
    }

    public Optional<Section> findById(int bid) {
        Optional<Section> optionalSection = sectionsRepository.findById(bid);

        return optionalSection;
    }

    public Section save(Section section) {

        Section section2 = sectionsRepository.save(section);

        return section2;
    }

    public boolean removeById(Integer sno) {

        if (sectionsRepository.existsById(sno)) {
            sectionsRepository.deleteById(sno);
            return true;
        }
        return false;
    }

}
