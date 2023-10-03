package com.example.backedu.controller;

import com.example.backedu.entity.Code;
import com.example.backedu.service.CodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName : com.example.backedu.controller
 * fileName : CodeController
 * author : kangtaegyung
 * date : 2022/07/28
 * description :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/28         kangtaegyung          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CodeController {

    @Autowired
    CodeService codeService;

    @PostMapping("/code")
    public ResponseEntity<Object> createCode(@RequestBody Code code) {
        log.info("code {}", code);

        try {
            boolean bResult = codeService.createCode(code);
            if (bResult) {
                return new ResponseEntity<Object>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Object>(HttpStatus.CONFLICT);
            }

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code")
    public ResponseEntity<Object> findAllPageDesc(@RequestParam(required = false) String searchKeyword,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size
    ) {

        try {

//            페이지 변수 저장
            Pageable pageable = PageRequest.of(page, size);

            Page<Code> codePage;

//            dname 이 없으면 전체 검색 실행
            codePage = codeService.findAllTitleDesc(searchKeyword, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("code", codePage.getContent());
            response.put("currentPage", codePage.getNumber());
            response.put("totalItems", codePage.getTotalElements());
            response.put("totalPages", codePage.getTotalPages());

            if (codePage.isEmpty() == false) {
//                성공
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/all")
    public ResponseEntity<Object> findAll() {

        try {
            List<Code> codeList = codeService.findAll();

            if (codeList.isEmpty() == false) {
//                성공
                return new ResponseEntity<>(codeList, HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/code/{codeId}")
    public ResponseEntity<Object> deleteId(@PathVariable String codeId) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            boolean bSuccess = codeService.removeById(codeId);

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
