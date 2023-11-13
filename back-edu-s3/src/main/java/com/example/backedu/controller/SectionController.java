package com.example.backedu.controller;

import com.example.backedu.entity.Section;
import com.example.backedu.service.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.example.backedu.controller
 * fileName : SectionsController
 * author : kangtaegyung
 * date : 2022/07/27
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/27         kangtaegyung          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SectionController {

    @Autowired
    SectionService sectionService;

    //  Todo:
    @GetMapping("/section")
    public ResponseEntity<Object> findAllByUuidAndTitleContaining(
            @RequestParam(defaultValue = "") String uuid,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            //            페이지 변수 저장
            Pageable pageable = PageRequest.of(page, size);

//        다운로드 url 을 만들어 DTO 에 저장함
            Page<Section> sectionPage = sectionService.findAllByUuidAndTitleContaining(uuid, title, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("section", sectionPage.getContent());
            response.put("currentPage", sectionPage.getNumber());
            response.put("totalItems", sectionPage.getTotalElements());
            response.put("totalPages", sectionPage.getTotalPages());

            if (sectionPage.isEmpty() == false) {
//                성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/section/{sno}")
    public ResponseEntity<Object> findById(@PathVariable int sno) {

        try {
            Optional<Section> optionalSection = sectionService.findById(sno);

            if (optionalSection.isPresent()) {
//                성공
                return new ResponseEntity<>(optionalSection.get(), HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/section")
    public ResponseEntity<Object> create(@RequestBody Section section) {

        try {
            Section section2 = sectionService.save(section);

            return new ResponseEntity<>(section2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/section/{sno}")
    public ResponseEntity<Object> update(@PathVariable int sno, @RequestBody Section section) {

        try {
            Section section2 = sectionService.save(section);

            return new ResponseEntity<>(section2, HttpStatus.OK);

        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //    삭제 처리
    @DeleteMapping("/section/deletion/{sno}")
    public ResponseEntity<Object> removeById(@PathVariable Integer sno) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            log.debug("sno : {}", sno);
            boolean bSuccess = sectionService.removeById(sno);

            if (bSuccess == true) {
//                delete 문이 성공했을 경우
                return new ResponseEntity<>(HttpStatus.OK);
            }
//            delete 실패했을 경우( 0건 삭제가 될경우 )
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
//            DB 에러가 날경우
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
