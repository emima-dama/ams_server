package ro.ubbcluj.cs.ams.attendance.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ro.ubbcluj.cs.ams.attendance.dto.SubjectResponseDto;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceExceptionType;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceServiceException;

import java.util.Objects;

@Component
public class MicroserviceCall {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final Logger logger = LogManager.getLogger(MicroserviceCall.class);

    public SubjectResponseDto getSubjectActivityNames(String subjectId,Integer activityId){

        logger.info(" >>>>>>>>>>> LOGGING getSubjectActivityNames with subject id {} and activity id {}",subjectId,activityId);
        logger.info("Call subject microservice <<<<<<<<<<< ");
        SubjectResponseDto info = null;
        try {
            String path = "http://subject-service/subject/course-activity?courseId=" + subjectId+"&activityId="+activityId;
            info = webClientBuilder
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .get()
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(SubjectResponseDto.class)
                    .block();
            if (Objects.isNull(info)) {
                throw new AttendanceServiceException("There is no subjects with id "+subjectId + "or activity with id"+activityId+"!\n",
                        AttendanceExceptionType.INFO_SUBJECTS_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("getSubjectActivityNames: call to student microservice failed");
            handleWebClientResponseException(e,
                    "There is no subjects with id "+subjectId + "or activity with id"+activityId+"!\n",
                    AttendanceExceptionType.INFO_SUBJECTS_NOT_FOUND);
        }
        return info;
    }

    private void handleWebClientResponseException(WebClientResponseException e, String message, AttendanceExceptionType type) {

        loggingWebClientResponseException(e);
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new AttendanceServiceException(e.getMessage(), AttendanceExceptionType.ERROR, e.getStatusCode());
        }
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw new AttendanceServiceException(e.getMessage(), AttendanceExceptionType.ERROR, HttpStatus.BAD_GATEWAY);
        }
        throw new AttendanceServiceException(message, type, HttpStatus.NOT_FOUND);
    }

    private void loggingWebClientResponseException(WebClientResponseException e) {

        logger.error(">>>>>>>>> ERROR LOGGING <<<<<<<");
        logger.error("Message: {}", e.getMessage());
        logger.error("Status: {} ", e.getStatusCode());
        logger.error("Code: {} ", e.getStatusCode().value());
        logger.error(">>>>>>>>> FINAL ERROR LOGGING <<<<<<<<");
    }


}
