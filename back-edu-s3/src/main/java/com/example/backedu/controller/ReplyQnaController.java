package com.example.backedu.controller;

import com.example.backedu.dto.ReplyQnaDto;
import com.example.backedu.entity.ReplyQna;
import com.example.backedu.service.ReplyQnaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.example.simpledms.controller
 * fileName : ReplyQnaController
 * author : kangtaegyung
 * date : 2023/06/21
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/21         kangtaegyung          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ReplyQnaController {

    @Autowired
    ReplyQnaService replyQnaService;

    /**
     * 전체 조회 : 게시판 제목 like 검색
     * @param boardTitle
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/reply-qna")
    public ResponseEntity<Object> selectByConnectByPage(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {

        try {
//            페이지 변수 저장
            Pageable pageable = PageRequest.of(page, size);

            Page<ReplyQnaDto> replyQnaPage;

            replyQnaPage = replyQnaService.selectByConnectByPage(boardTitle, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("replyQna", replyQnaPage.getContent());
            response.put("currentPage", replyQnaPage.getNumber());
            response.put("totalItems", replyQnaPage.getTotalElements());
            response.put("totalPages", replyQnaPage.getTotalPages());

            if (replyQnaPage.isEmpty() == false) {
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

    /**
     * 상세 조회
     * @param bid
     * @return
     */
    @GetMapping("/reply-qna/{bid}")
    public ResponseEntity<Object> findById(@PathVariable int bid) {

        try {
            Optional<ReplyQna> optionalReplyQna = replyQnaService.findById(bid);

            if (optionalReplyQna.isPresent()) {
//                성공
                return new ResponseEntity<>(optionalReplyQna.get(), HttpStatus.OK);
            } else {
//                데이터 없음
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
//            서버 에러
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 저장하기 : 첨부파일 포함
     * @param boardTitle
     * @param boardContent
     * @param boardWriter
     * @param viewCnt
     * @param blobFile
     * @return
     */
    @PostMapping("/reply-qna")
    public ResponseEntity<Object> createBoardQna(
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "") String boardContent,
            @RequestParam(defaultValue = "") String boardWriter,
            @RequestParam Integer viewCnt,
            @RequestParam(required = false) MultipartFile blobFile) {

        try {
//            전달받은 값을 객체에 넣기
            ReplyQna replyQna = ReplyQna.builder()
                    .boardTitle(boardTitle)
                    .boardContent(boardContent)
                    .boardWriter(boardWriter)
                    .viewCnt(viewCnt)
                    .build();

            int insertCount = replyQnaService.saveBoard(replyQna, blobFile);

            return new ResponseEntity<>(insertCount, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 답변 저장하기
     * @param boardTitle
     * @param boardContent
     * @param boardWriter
     * @param viewCnt
     * @param blobFile
     * @return
     */
    @PostMapping("/reply")
    public ResponseEntity<Object> createReply(
            @RequestParam String boardTitle,
            @RequestParam(defaultValue = "") String boardContent,
            @RequestParam String boardWriter,
            @RequestParam Integer viewCnt,
            @RequestParam Integer boardGroup,
            @RequestParam Integer boardParent,
            @RequestParam(required = false) MultipartFile blobFile) {

        try {
            //            전달받은 값을 객체에 넣기
            ReplyQna replyQna = ReplyQna.builder()
                    .boardTitle(boardTitle)
                    .boardContent(boardContent)
                    .boardWriter(boardWriter)
                    .viewCnt(viewCnt)
                    .boardGroup(boardGroup)
                    .boardParent(boardParent)
                    .build();

            ReplyQna replyQna2 = replyQnaService.save(replyQna, blobFile);

            return new ResponseEntity<>(replyQna2, HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 수정하기
     * @param bid
     * @param boardTitle
     * @param boardContent
     * @param boardWriter
     * @param viewCnt
     * @param boardGroup
     * @param boardParent
     * @param uuid
     * @param blobFile
     * @return
     */
    @PutMapping("/reply-qna/{bid}")
    public ResponseEntity<Object> update(
            @PathVariable int bid,
            @RequestParam(defaultValue = "") String boardTitle,
            @RequestParam(defaultValue = "") String boardContent,
            @RequestParam(defaultValue = "") String boardWriter,
            @RequestParam Integer viewCnt,
            @RequestParam Integer boardGroup,
            @RequestParam Integer boardParent,
            @RequestParam String uuid,
            @RequestParam(required = false) MultipartFile blobFile) {

        try {
            //            전달받은 값을 객체에 넣기
            ReplyQna replyQna = ReplyQna.builder()
                    .bid(bid)
                    .boardTitle(boardTitle)
                    .boardContent(boardContent)
                    .boardWriter(boardWriter)
                    .viewCnt(viewCnt)
                    .boardGroup(boardGroup)
                    .boardParent(boardParent)
                    .uuid(uuid)
                    .build();

            ReplyQna replyQna2 = replyQnaService.save(replyQna, blobFile);

            return new ResponseEntity<>(replyQna2, HttpStatus.OK);

        } catch (Exception e) {
//            DB 에러가 났을경우 : INTERNAL_SERVER_ERROR 프론트엔드로 전송
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * file 자동 다운로드 메뉴 : 내부적으로 이미지 URL 접근 시 다운로드됨
     * 다운로드 헤더 형식 : HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
     * @param uuid
     * @return
     */
    @GetMapping("/reply-qna/file/{uuid}")
    public ResponseEntity<byte[]> findFile(@PathVariable String uuid) {
//        Course course = courseService.findById(uuid).get();
        try {
            byte[] bytes = replyQnaService.getObject(uuid);

            return ResponseEntity.ok()
//           Todo : attachment: => attachment;
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uuid + "\"")
                    .body(bytes);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 게시물 - 답변 모두 삭제
     * @param boardGroup
     * @return
     */
    @DeleteMapping("/reply-qna/deletion/{boardGroup}")
    public ResponseEntity<Object> deleteBoardQna(@PathVariable int boardGroup) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            boolean bSuccess = replyQnaService.removeAllByBoardGroup(boardGroup);

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

    /**
     * 답변 삭제
     * @param bid
     * @return
     */
    @DeleteMapping("/reply/deletion/{bid}")
    public ResponseEntity<Object> deleteReply(@PathVariable int bid) {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            boolean bSuccess = replyQnaService.removeById(bid);

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


    /**
     * 게시물 모두 삭제
     * @return
     */
    @DeleteMapping("/reply-qna/all")
    public ResponseEntity<Object> deleteAll() {

//        프론트엔드 쪽으로 상태정보를 보내줌
        try {
            replyQnaService.removeAll();

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
//            DB 에러가 날경우
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
