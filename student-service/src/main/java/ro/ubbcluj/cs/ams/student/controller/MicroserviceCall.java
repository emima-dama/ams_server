package ro.ubbcluj.cs.ams.student.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ro.ubbcluj.cs.ams.student.service.exception.StudentExceptionType;
import ro.ubbcluj.cs.ams.student.service.exception.StudentServiceException;

import java.util.Objects;

@Component
public class MicroserviceCall {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final Logger logger = LogManager.getLogger(MicroserviceCall.class);

    public void checkIfExistsASpecialization(Integer specId){

        logger.info(" ========= LOGGING check if exists specialization with id "+specId);
        logger.info("\nCall subject microservice ========== ");
        try {
            String path = "http://subject-service/subject/specialization?specId=" + specId;
            String response = webClientBuilder
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (Objects.isNull(response)) {
                throw new StudentServiceException("There is no specialization with id "+specId + "!\n",
                        StudentExceptionType.SPECIALIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("checkIfSpecialization: call to subject microservice failed");
            handleWebClientResponseException(e,
                    "There is no specialization with id "+specId + "!\n",
                    StudentExceptionType.SPECIALIZATION_NOT_FOUND);
        }
    }

    protected void checkIfExistsSubjectById(String subjectId){

        logger.info(" ========= LOGGING check if exists subject with id "+subjectId);
        logger.info("\nCall subject microservice ========== ");
        try {
            String path = "http://subject-service/subject/by?id=" + subjectId;
            String response = webClientBuilder
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (Objects.isNull(response)) {
                throw new StudentServiceException("There is no subject with id "+subjectId+ "!\n",
                        StudentExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("checkIfExistsSubjectById: call to subject microservice failed");
            handleWebClientResponseException(e,
                    "There is no subject with id "+subjectId+ "!\n",
                    StudentExceptionType.SUBJECT_NOT_FOUND);
        }
    }

    protected void checkIfExistsStudentUsername(String studentId){

        logger.info(" ========= LOGGING check if exists student username "+studentId);
        logger.info("\nCall auth microservice ========== ");
        try {
            String path = "http://auth-service/authentication/username?username=" + studentId;
            String response = webClientBuilder
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (Objects.isNull(response)) {
                throw new StudentServiceException("There is no student with username "+studentId+ "!\n",
                        StudentExceptionType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("checkIfProfessorTeachesSpecificActivityTypeForASubject: call to subject microservice failed");
            handleWebClientResponseException(e,
                    "There is no student with username "+studentId+ "!\n",
                    StudentExceptionType.USERNAME_NOT_FOUND);
        }
    }

    private void handleWebClientResponseException(WebClientResponseException e, String message, StudentExceptionType type) {

        loggingWebClientResponseException(e);
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new StudentServiceException(e.getMessage(), StudentExceptionType.ERROR, e.getStatusCode());
        }
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw new StudentServiceException(e.getMessage(), StudentExceptionType.ERROR, HttpStatus.BAD_GATEWAY);
        }
        throw new StudentServiceException(message, type, HttpStatus.NOT_FOUND);
    }

    private void loggingWebClientResponseException(WebClientResponseException e) {

        logger.error("========== ERROR LOGGING ==========");
        logger.error("Message: {}", e.getMessage());
        logger.error("Status: {} ", e.getStatusCode());
        logger.error("Code: {} ", e.getStatusCode().value());
        logger.error("========== FINAL ERROR LOGGING ==========");
    }


}
