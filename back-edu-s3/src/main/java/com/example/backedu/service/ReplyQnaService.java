package com.example.backedu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.backedu.dto.ReplyQnaDto;
import com.example.backedu.entity.Course;
import com.example.backedu.entity.ReplyQna;
import com.example.backedu.repository.ReplyQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * packageName : com.example.simpledms.service
 * fileName : ReplyBoardService
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
@RequiredArgsConstructor
@Service
public class ReplyQnaService {

    private final ReplyQnaRepository replyQnaRepository; // 샘플데이터 DB에 접근하는 객체

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 전체 조회
     *
     * @param pageable
     * @return
     */
    public Page<ReplyQna> findAll(Pageable pageable) {
        Page<ReplyQna> page = replyQnaRepository.findAll(pageable);

        return page;
    }

    /**
     * id 로 조회
     *
     * @param bid
     * @return
     */
    public Optional<ReplyQna> findById(int bid) {
        Optional<ReplyQna> optionalReplyQna = replyQnaRepository.findById(bid);

        return optionalReplyQna;
    }

    /**
     * 답변 저장하기 : 파일 첨부(s3)
     * TODO: dml 실행시 try ~ catch 없으면 에러발생시 자세한 에러내역을 볼수 없음 : 간단 에러내역
     *
     * @param replyQna
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public ReplyQna save(ReplyQna replyQna, MultipartFile multipartFile) throws IOException {

        ReplyQna replyQna2 = null;

        try {
            //        기본키가 없으면 insert
            if (replyQna.getBid() == null) {

                // todo: s3 에 insert 하는 함수 호출
                setInsertObject(replyQna, multipartFile);

//               db 에 s3 url 주소 insert
                replyQna2 = replyQnaRepository.save(replyQna);
            } else {
//        기본키가 있으면 update
                // todo: s3 에 insert 하는 함수 호출
                setUpdateObject(replyQna, multipartFile);

                //               db 에 s3 url 주소 update
                replyQna2 = replyQnaRepository.save(replyQna);

            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return replyQna2;
    }

    /**
     * 답변만 삭제 함수
     *
     * @param bid
     * @return
     */
    public boolean removeById(int bid) {

//        그룹 번호 == 부모 게시물번호임
        if (replyQnaRepository.existsById(bid)) {
            replyQnaRepository.deleteById(bid);
            return true;
        }
        return false;
    }

    /**
     * 그룹 번호로 삭제 : 게시물 + 답변 모두 삭제
     *
     * @param boardGroup
     * @return
     */
    public boolean removeAllByBoardGroup(int boardGroup) {

//        그룹 번호 == 부모 게시물번호임

        int deleteCount = replyQnaRepository.removeAllByBoardGroup(boardGroup);

        if (deleteCount > 0) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * 전체 삭제
     */
    public void removeAll() {

        replyQnaRepository.deleteAll();
    }

    /**
     * 계층형 쿼리 like 검색
     *
     * @param boardTitle
     * @param pageable
     * @return
     */
    public Page<ReplyQnaDto> selectByConnectByPage(String boardTitle, Pageable pageable) {

        Page<ReplyQnaDto> page = replyQnaRepository.selectByConnectByPage(boardTitle, pageable);

        return page;
    }

    /**
     * 게시물 저장 : 답변 제외
     *
     * @param replyQna
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public int saveBoard(ReplyQna replyQna, MultipartFile multipartFile) throws IOException {

        int insertCount = 0;

        try {
            //        기본키가 없으면 insert
            if (replyQna.getBid() == null) {

                // todo: s3 에 insert 하는 함수 호출
                setInsertObject(replyQna, multipartFile);

//               db 에 s3 url 주소 insert
                insertCount = replyQnaRepository.insertByQna(replyQna);
            } else {
//        기본키가 있으면 update

                // todo: s3 에 insert 하는 함수 호출
                setUpdateObject(replyQna, multipartFile);
                //               db 에 s3 url 주소 update
                insertCount = replyQnaRepository.insertByQna(replyQna);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return insertCount;
    }

    /**
     * s3 에 첨부 파일 insert 및 replyQna 에 (uuid, 다운로드 url ) 저장 하는 함수
     *
     * @param replyQna
     * @param multipartFile
     * @throws IOException
     */
    public void setInsertObject(ReplyQna replyQna, MultipartFile multipartFile) throws IOException {
        //        S3에 저장되는 파일의 이름이 중복되지 않기 위해서 UUID로 생성한 랜덤 값과 파일 이름을 연결하여 S3에 업로드
//        zip 압축 파일만 업로드됨
        String uuid = UUID.randomUUID().toString() + ".zip";
        String fileDownloadUri = "";

        if (multipartFile != null) {
            //        Spring Server 에서 S3로 파일을 업로드해야 하는데, 이 때 파일의 사이즈를 ContentLength 로 S3에 알려주기 위해서 ObjectMetadata 를 사용
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

//        S3 API 메소드인 putObject 를 이용하여 파일 Stream 을 열어서 S3에 파일을 업로드
            amazonS3.putObject(bucket, uuid, multipartFile.getInputStream(), objMeta);

//      return : file 다운로드 url : aws s3 주소임
            fileDownloadUri = amazonS3.getUrl(bucket, uuid).toString();

//          업로드될 최초 파일명을 저장 : 나중에 uuid 파일명으로 변경됨
            replyQna.setFileName(multipartFile.getOriginalFilename());
        }

        //              새로운 uuid 추가, fileDownload url 추가
        replyQna.setUuid(uuid);
        replyQna.setFileUrl(fileDownloadUri);
    }


    /**
     * s3 에 첨부 파일 update 및 replyQna 에 다운로드 url 저장 하는 함수
     * todo: s3 는 수정이 없으므로 이미지 삭제 후 새로 insert 함, db 는 정보 수정함
     *
     * @param replyQna
     * @param multipartFile
     * @throws IOException
     */
    public void setUpdateObject(ReplyQna replyQna, MultipartFile multipartFile) throws IOException {

        String fileDownloadUri = "";

        //                TODO: s3 에는 수정이 없으므로 파일 업로드후 기존 파일 삭제를 함
        //  1) 기존 파일 삭제 : 있던 없던 삭제 시도 함 ( 없어도 성공메세지 호출 )
        amazonS3.deleteObject(bucket, replyQna.getUuid());

        if (multipartFile != null) {

//                String s3FileName = uuid + "-" + multipartFile.getOriginalFilename(); // uuid + file 명 붙여서 사용할 수 있음(로직이 길어져서 생략)
//              2) 기존 uuid 로 새로운 파일 업로드
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            //        S3 API 메소드인 putObject 를 이용하여 파일 Stream 을 열어서 S3에 파일을 업로드
            amazonS3.putObject(bucket, replyQna.getUuid(), multipartFile.getInputStream(), objMeta);

//                file 다운로드 url : aws s3 주소임
            fileDownloadUri = amazonS3.getUrl(bucket, replyQna.getUuid()).toString();

            //          업로드될 최초 파일명을 저장 : 나중에 uuid 파일명으로 변경됨
            replyQna.setFileName(multipartFile.getOriginalFilename());
        }

        //        fileDownload url 추가
        replyQna.setFileUrl(fileDownloadUri);
    }

    /**
     * 다운로드 url 에 사용할 s3 객체 가져오기 함수
     *
     * @param uuid
     * @return
     * @throws IOException
     */
    public byte[] getObject(String uuid) throws IOException {

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uuid));

        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
//        aws 클래스 : IOUtils
        byte[] bytes = IOUtils.toByteArray(s3ObjectInputStream);

        return bytes;
    }

}
