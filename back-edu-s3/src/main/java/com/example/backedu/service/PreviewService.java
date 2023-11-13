package com.example.backedu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.backedu.entity.Preview;
import com.example.backedu.repository.PreviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * packageName : com.example.backedu.service
 * fileName : PreviewServiceImpl
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
public class PreviewService {

    private final AmazonS3 amazonS3;

    private final PreviewRepository previewRepository;
    private final ModelMapper modelMapper;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // TODO: dml 실행시 try ~ catch 없으면 에러발생시 자세한 에러내역을 볼수 없음 : 간단 에러내역
    public Preview save(Preview preview, MultipartFile multipartFile) throws IOException {

        Preview preview2 = null;

        try {
            //        기본키가 없으면 insert
            if (preview.getPuuid() == null) {

                // todo: s3 에 insert 하는 함수 호출
                insertObject(preview, multipartFile);

//              save the preview object
                preview2 = previewRepository.save(preview);
            } else {
//        기본키가 있으면 update
                // todo: s3 에 insert 하는 함수 호출
                updateObject(preview, multipartFile);

                //               db 에 s3 url 주소 update
                preview2 = previewRepository.save(preview);

            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return preview2;
    }

    public boolean removeById(String uuid) {

        if (previewRepository.existsById(uuid)) {
            //            DB 삭제 : 소프트삭제
            previewRepository.deleteById(uuid);
//            TODO: s3삭제하지 않음
//            amazonS3.deleteObject(bucket, uuid);
            return true;
        }
        return false;
    }


    /**
     * s3 에 첨부 파일 insert 및 replyQna 에 (uuid, 다운로드 url ) 저장 하는 함수
     *
     * @param preview
     * @param multipartFile
     * @throws IOException
     */
    public void insertObject(Preview preview, MultipartFile multipartFile) throws IOException {
        //        S3에 저장되는 파일의 이름이 중복되지 않기 위해서 UUID로 생성한 랜덤 값과 파일 이름을 연결하여 S3에 업로드
//        zip 압축 파일만 업로드됨
        String uuid = "image/" + UUID.randomUUID().toString();
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
            preview.setFileName(multipartFile.getOriginalFilename());
        }

        //              새로운 uuid 추가, fileDownload url 추가
        preview.setPuuid(uuid);
        preview.setFileUrl(fileDownloadUri);
    }


    /**
     * s3 에 첨부 파일 update 및 replyQna 에 다운로드 url 저장 하는 함수
     * todo: s3 는 수정이 없으므로 이미지 삭제 후 새로 insert 함, db 는 정보 수정함
     *
     * @param preview
     * @param multipartFile
     * @throws IOException
     */
    public void updateObject(Preview preview, MultipartFile multipartFile) throws IOException {

        String fileDownloadUri = "";

        //                TODO: s3 에는 수정이 없으므로 파일 업로드후 기존 파일 삭제를 함
        //  1) 기존 파일 삭제 : 있던 없던 삭제 시도 함 ( 없어도 성공메세지 호출 )
        amazonS3.deleteObject(bucket, preview.getPuuid());

        if (multipartFile != null) {
//              2) 기존 uuid 로 새로운 파일 업로드
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());

            //        S3 API 메소드인 putObject 를 이용하여 파일 Stream 을 열어서 S3에 파일을 업로드
            amazonS3.putObject(bucket, preview.getPuuid(), multipartFile.getInputStream(), objMeta);

//                file 다운로드 url : aws s3 주소임
            fileDownloadUri = amazonS3.getUrl(bucket, preview.getPuuid()).toString();

            //          업로드될 최초 파일명을 저장 : 나중에 uuid 파일명으로 변경됨
            preview.setFileName(multipartFile.getOriginalFilename());
        }

        //        fileDownload url 추가
        preview.setFileUrl(fileDownloadUri);
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
