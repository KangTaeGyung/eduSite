package com.example.backedu.controller;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.backedu.dto.CourseJoinCDto;
import com.example.backedu.dto.CourseJoinDto;
import com.example.backedu.entity.Code;
import com.example.backedu.entity.Course;
import com.example.backedu.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
 * fileName : CoursesController
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
public class CourseController {

    @Autowired
    CourseService courseService;

    ModelMapper modelMapper = new ModelMapper();

    /**
     * 저장하기 : 파일첨부
     *
     * @param code
     * @param title
     * @param content
     * @param eventYn
     * @param eventTitle
     * @param eventContent
     * @param blobFile
     * @return
     */
    //    생성 처리 : image update 포함
    @PostMapping("/course")
    public ResponseEntity<Object> createUploadFile(
            @RequestParam String code,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String eventYn,
            @RequestParam String eventTitle,
            @RequestParam String eventContent,
            @RequestParam MultipartFile blobFile
    ) {

        try {
            //            create code object using builder
            Code code2 = Code.builder()
                    .codeId(code)
                    .build();

//            create entity object using builder
            Course course = Course.builder()
                    .code(code2)
                    .title(title)
                    .content(content)
                    .eventYn(eventYn)
                    .eventTitle(eventTitle)
                    .eventContent(eventContent)
                    .build();

            courseService.save(course, blobFile);

            return new ResponseEntity<Object>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 수정하기 : 파일 첨부 포함
     *
     * @param uuid
     * @param code
     * @param title
     * @param content
     * @param eventYn
     * @param eventTitle
     * @param eventContent
     * @param blobFile
     * @return
     */
    //    수정 처리 : image update 포함
    @PutMapping("/course/{uuid}")
    public ResponseEntity<Object> updateUploadFile(@PathVariable String uuid,
                                                   @RequestParam String code,
                                                   @RequestParam String title,
                                                   @RequestParam String content,
                                                   @RequestParam String eventYn,
                                                   @RequestParam String eventTitle,
                                                   @RequestParam String eventContent,
                                                   @RequestParam MultipartFile blobFile
    ) {

        try {
            //            create code object using builder
            Code code2 = Code.builder()
                    .codeId(code)
                    .build();

//            create entity object using builder
            Course course = Course.builder()
                    .code(code2)
                    .title(title)
                    .content(content)
                    .eventYn(eventYn)
                    .eventTitle(eventTitle)
                    .eventContent(eventContent)
                    .build();

            return new ResponseEntity<Object>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * file 자동 다운로드 메뉴 : 내부적으로 이미지 URL 접근 시 다운로드됨
     * 다운로드 헤더 형식 : HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
     *
     * @param uuid
     * @return
     */
    @GetMapping("/course/file/{uuid}")
    public ResponseEntity<byte[]> findFile(@PathVariable String uuid) {
//        Course course = courseService.findById(uuid).get();
        try {
            byte[] bytes = courseService.getObject(uuid);

            return ResponseEntity.ok()
//           Todo : attachment: => attachment;
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uuid + "\"")
                    .body(bytes);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  Todo:
    @GetMapping("/course")
    public ResponseEntity<Object> getListFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) throws SQLException {
        try {
            //            페이지 변수 저장
            Pageable pageable = PageRequest.of(page, size);

//        다운로드 url 을 만들어 DTO 에 저장함
            Page<CourseJoinCDto> coursePage = courseService.findAllCodeJoin(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("course", coursePage.getContent());
            response.put("currentPage", coursePage.getNumber());
            response.put("totalItems", coursePage.getTotalElements());
            response.put("totalPages", coursePage.getTotalPages());

            if (coursePage.isEmpty() == false) {
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

    //  Todo:
    @GetMapping("/course/event")
    public ResponseEntity<Object> getEventFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            //            페이지 변수 저장
            Pageable pageable = PageRequest.of(page, size);

//        다운로드 url 을 만들어 DTO 에 저장함
            Page<Course> coursePage = courseService.findAllEventDesc(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("course", coursePage.getContent());
            response.put("currentPage", coursePage.getNumber());
            response.put("totalItems", coursePage.getTotalElements());
            response.put("totalPages", coursePage.getTotalPages());

            if (coursePage.isEmpty() == false) {
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

    //    과목 id로 조회
    @GetMapping("/course/{uuid}")
    public ResponseEntity<Object> findById(@PathVariable String uuid) {
        try {
            //            Vue에서 전송한 매개변수 데이터 확인
            log.info("uuid {}", uuid);

            Optional<Course> courseOptional = courseService.findById(uuid);

            if (courseOptional.isPresent()) {
                return new ResponseEntity<Object>(courseOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    삭제 처리
    @DeleteMapping("/course/deletion/{uuid}")
    public ResponseEntity<Object> deleteDept(@PathVariable String uuid) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            log.debug("uuid : {}", uuid);
            boolean bSuccess = courseService.removeById(uuid);

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
