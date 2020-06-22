package ro.ubbcluj.cs.ams.subject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ro.ubbcluj.cs.ams.subject.dto.SubjectsByStudentDto;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectExceptionType;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectServiceException;

import java.util.Objects;

@Component
public class MicroserviceCall {

    @Autowired //TODO: Qualifier
    private WebClient.Builder webClientBuilder;

    private final Logger logger = LogManager.getLogger(MicroserviceCall.class);

    public SubjectsByStudentDto getSubjectsIdsByStudentId(String studentId){

        logger.info(" ========= LOGGING getSubjectsIdsByStudentId with id "+studentId);
        logger.info("\nCall student microservice ========== ");
        SubjectsByStudentDto subjects = null;
        try {
            String path = "http://student-service/student/subjects?studentId=" + studentId;
            subjects = webClientBuilder
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(SubjectsByStudentDto.class)
                    .block();
            if (Objects.isNull(subjects)) {
                throw new SubjectServiceException("There is no subjects for student with id "+studentId + "!\n",
                        SubjectExceptionType.ENROLLMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("getSubjectsIdsByStudentId: call to student microservice failed");
            handleWebClientResponseException(e,
                    "There is no subjects for student with id "+studentId + "!\n",
                    SubjectExceptionType.ENROLLMENT_NOT_FOUND);
        }
        return subjects;
    }

    private void handleWebClientResponseException(WebClientResponseException e, String message, SubjectExceptionType type) {

        loggingWebClientResponseException(e);
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new SubjectServiceException(e.getMessage(), SubjectExceptionType.ERROR, e.getStatusCode());
        }
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw new SubjectServiceException(e.getMessage(), SubjectExceptionType.ERROR, HttpStatus.BAD_GATEWAY);
        }
        throw new SubjectServiceException(message, type, HttpStatus.NOT_FOUND);
    }

    private void loggingWebClientResponseException(WebClientResponseException e) {

        logger.error("========== ERROR LOGGING ==========");
        logger.error("Message: {}", e.getMessage());
        logger.error("Status: {} ", e.getStatusCode());
        logger.error("Code: {} ", e.getStatusCode().value());
        logger.error("========== FINAL ERROR LOGGING ==========");
    }


}
