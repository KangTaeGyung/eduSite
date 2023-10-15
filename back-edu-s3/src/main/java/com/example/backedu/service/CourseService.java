package com.example.backedu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.backedu.dto.CourseJoinDto;
import com.example.backedu.entity.Course;
import com.example.backedu.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * packageName : com.example.backedu.service
 * fileName : CourseServiceImpl
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
public class CourseService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final CourseRepository coursesRepository;

    // TODO: dml 실행시 try ~ catch 없으면 에러발생시 자세한 에러내역을 볼수 없음 : 간단 에러내역
    public Course save(String uuid, String code, String title, String content, String eventYn, String eventTitle, String eventContent, MultipartFile multipartFile) throws IOException {

        Course course2 = null;

        try {
            //        기본키가 없으면 insert
            if (uuid == null) {
                //        S3에 저장되는 파일의 이름이 중복되지 않기 위해서 UUID로 생성한 랜덤 값과 파일 이름을 연결하여 S3에 업로드
                String tmpUuid = UUID.randomUUID().toString();
//                String s3FileName = tmpUuid + "-" + multipartFile.getOriginalFilename();

//        Spring Server 에서 S3로 파일을 업로드해야 하는데, 이 때 파일의 사이즈를 ContentLength 로 S3에 알려주기 위해서 ObjectMetadata 를 사용
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentLength(multipartFile.getInputStream().available());

//        S3 API 메소드인 putObject 를 이용하여 파일 Stream 을 열어서 S3에 파일을 업로드
                amazonS3.putObject(bucket, tmpUuid, multipartFile.getInputStream(), objMeta);

//                file 다운로드 url : aws s3 주소임
                String fileDownloadUri = amazonS3.getUrl(bucket, tmpUuid).toString();

                Course course = new Course(tmpUuid, code, title, content, eventYn, eventTitle, eventContent, uuid, fileDownloadUri);
//               db 에 s3 url 주소 insert
                course2 = coursesRepository.save(course);
            } else {
//        기본키가 있으면 update
//                TODO: s3 에는 수정이 없으므로 파일 업로드후 기존 파일 삭제를 함
                //  1) 기존 파일 삭제
                amazonS3.deleteObject(bucket, uuid);

//                String s3FileName = uuid + "-" + multipartFile.getOriginalFilename(); // uuid + file 명 붙여서 사용할 수 있음(로직이 길어져서 생략)
//              2) 기존 uuid 로 새로운 파일 업로드
                ObjectMetadata objMeta = new ObjectMetadata();
                objMeta.setContentLength(multipartFile.getInputStream().available());

                //        S3 API 메소드인 putObject 를 이용하여 파일 Stream 을 열어서 S3에 파일을 업로드
                amazonS3.putObject(bucket, uuid, multipartFile.getInputStream(), objMeta);

//                file 다운로드 url : aws s3 주소임
                String fileDownloadUri = amazonS3.getUrl(bucket, uuid).toString();

                Course course = new Course(uuid, code, title, content, eventYn, eventTitle, eventContent, uuid, fileDownloadUri);
                //               db 에 s3 url 주소 update
                course2 = coursesRepository.save(course);

            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return course2;
    }


    public Page<Course> findAllDesc(Pageable pageable) {

        Page<Course> courseList = coursesRepository.findAllByOrderByInsertTimeDesc(pageable);

        return courseList;
    }

    public Page<CourseJoinDto> findAllCodeJoin(Pageable pageable) {

        Page<CourseJoinDto> courseList = coursesRepository.selectByCodeJoin(pageable);

        return courseList;
    }

    public Page<Course> findAllEventDesc(Pageable pageable) {

        Page<Course> courseList = coursesRepository.findAllByEventYnOrderByInsertTimeDesc("Y", pageable);

        return courseList;
    }

    public Optional<Course> findById(String uuid) {

        //            findById : parameter 값 - ID, return  값 - Optional
        Optional<Course> courseOptional = coursesRepository.findById(uuid);

        return courseOptional;
    }

    public boolean removeById(String uuid) {

        if (coursesRepository.existsById(uuid)) {
            //            DB 삭제 : 소프트삭제
            coursesRepository.deleteById(uuid);
//            TODO: s3삭제하지 않음
//            amazonS3.deleteObject(bucket, uuid);
            return true;
        }
        return false;
    }

    /**
     * 다운로드 url 에 사용할 s3 객체 가져오기 함수
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
