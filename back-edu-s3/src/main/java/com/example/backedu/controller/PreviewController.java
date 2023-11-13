package com.example.backedu.controller;

import com.example.backedu.entity.Code;
import com.example.backedu.entity.Preview;
import com.example.backedu.service.PreviewService;
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

/**
 * packageName : com.example.backedu.controller
 * fileName : PreviewsController
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
public class PreviewController {

    @Autowired
    PreviewService previewService;

    ModelMapper modelMapper = new ModelMapper();

    /**
     * 저장하기 : 파일첨부
     *
     * @param blobFile
     * @return
     */
    //    생성 처리 : image update 포함
    @PostMapping("/preview")
    public ResponseEntity<Object> createUploadFile(
            @RequestParam MultipartFile blobFile
    ) {

        try {
//            create entity object using builder
            Preview preview = Preview.builder()
                    .build();

            Preview preview2 = previewService.save(preview, blobFile);

            return new ResponseEntity<Object>(preview2, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 수정하기 : 파일 첨부 포함
     *
     * @param blobFile
     * @return
     */
    //    수정 처리 : image update 포함
    @PutMapping("/preview/{puuid}")
    public ResponseEntity<Object> updateUploadFile(@PathVariable String puuid,
                                                   @RequestParam MultipartFile blobFile
    ) {

        try {
//            create entity object using builder
            Preview preview = Preview.builder()
                    .puuid(puuid)
                    .build();

            Preview preview2 = previewService.save(preview, blobFile);

            return new ResponseEntity<Object>(preview2, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * file 자동 다운로드 메뉴 : 내부적으로 이미지 URL 접근 시 다운로드됨
     * 다운로드 헤더 형식 : HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
     *
     * @param puuid
     * @return
     */
    @GetMapping("/preview/file/{puuid}")
    public ResponseEntity<byte[]> findFile(@PathVariable String puuid) {
//        Preview preview = previewService.findById(uuid).get();
        try {
            byte[] bytes = previewService.getObject(puuid);

            return ResponseEntity.ok()
//           Todo : attachment: => attachment;
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + puuid + "\"")
                    .body(bytes);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    삭제 처리
    @DeleteMapping("/preview/deletion/{puuid}")
    public ResponseEntity<Object> deleteDept(@PathVariable String puuid) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            boolean bSuccess = previewService.removeById(puuid);

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
