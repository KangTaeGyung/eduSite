package com.example.backedu.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName : com.example.jpaexam.configuration
 * fileName : RootContext
 * author : kangtaegyung
 * date : 2022/10/21
 * description : 기본 설정을 위한 클래스
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/21         kangtaegyung          최초 생성
 */
@Configuration
public class RootContext {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}
