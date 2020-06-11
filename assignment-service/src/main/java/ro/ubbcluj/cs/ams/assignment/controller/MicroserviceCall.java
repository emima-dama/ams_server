package ro.ubbcluj.cs.ams.assignment.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceException;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceExceptionType;

import java.util.Objects;

@Component
public class MicroserviceCall {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final Logger logger = LogManager.getLogger(MicroserviceCall.class);

    public void checkIfProfessorTeachesSpecificActivityTypeForASubject(String subjectId, int activityTypeId, String professorUsername) {

        logger.info(" ========= LOGGING check if a professor teaches specific activity type for a subject");
        logger.info("Call subject microservice ========== ");
        try {
            String path = "http://subject-service/subject/assign?subjectId=" + subjectId + "&activityTypeId=" + activityTypeId + "&professorUsername=" + professorUsername;
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
                throw new AssignmentServiceException("There is no professor with username " + professorUsername + " that teaches activity type with id " + activityTypeId + " for subject with id " + subjectId + "!\n",
                        AssignmentServiceExceptionType.TEACHER_ACTIVITY_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (WebClientResponseException e) {
            logger.error("checkIfProfessorTeachesSpecificActivityTypeForASubject: call to subject microservice failed");
            handleWebClientResponseException(e,
                    "There is no teacher with username " + professorUsername + " that teaches activity type with id " + activityTypeId + " for subject with id " + subjectId + "!\n",
                    AssignmentServiceExceptionType.TEACHER_ACTIVITY_TYPE_NOT_FOUND);
        }
    }

    private void handleWebClientResponseException(WebClientResponseException e, String message, AssignmentServiceExceptionType type) {

        loggingWebClientResponseException(e);
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new AssignmentServiceException(e.getMessage(), AssignmentServiceExceptionType.ERROR, e.getStatusCode());
        }
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw new AssignmentServiceException(e.getMessage(), AssignmentServiceExceptionType.ERROR, HttpStatus.BAD_GATEWAY);
        }
        throw new AssignmentServiceException(message, type, HttpStatus.NOT_FOUND);
    }

    private void loggingWebClientResponseException(WebClientResponseException e) {

        logger.error("========== ERROR LOGGING ==========");
        logger.error("Message: {}", e.getMessage());
        logger.error("Status: {} ", e.getStatusCode());
        logger.error("Code: {} ", e.getStatusCode().value());
        logger.error("========== FINAL ERROR LOGGING ==========");
    }
}
