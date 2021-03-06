package ro.ubbcluj.cs.ams.subject.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.ams.subject.dto.*;
import ro.ubbcluj.cs.ams.subject.model.tables.pojos.Specialization;
import ro.ubbcluj.cs.ams.subject.model.tables.pojos.Subject;
import ro.ubbcluj.cs.ams.subject.service.Service;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectExceptionType;
import ro.ubbcluj.cs.ams.subject.service.exception.SubjectServiceException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class SubjectController {

    private final Logger logger = LogManager.getLogger(SubjectController.class);

    @Autowired
    private Service service;

    @Autowired
    private MicroserviceCall microserviceCall;

    /**
     * URL : http://localhost:8080/subject/
     */
    @ApiOperation(value = "Add given subject")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Subject.class),
            @ApiResponse(code = 400, message = "DUPLICATE_SUBJECT", response = SubjectExceptionType.class),
    })
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectResponseDto> addSubject(@RequestBody @Valid SubjectRequestDto subject, BindingResult result) {

        logger.info(" ========== LOGGING addSubject ========== ");
        loggingSubject(subject);

        if (result.hasErrors())
            throw new SubjectServiceException("Invalid subject " + subject, SubjectExceptionType.INVALID_SUBJECT, HttpStatus.BAD_REQUEST);

        SubjectResponseDto subjectResponseDto = service.addSubject(subject);

        logger.info(" ========== SUCCESSFUL LOGGING addSubject ========== ");
        return new ResponseEntity<>(subjectResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Activity from spLinks by professor username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = SpLinkResponseDto.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = SubjectExceptionType.class)
    })
    @RequestMapping(value = "/activities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivitiesResponseDto> findActivitiesOfTeacher(Principal principal) {

        logger.info("========== LOGGING findActivitiesOfTeacher ==========");

        ActivitiesResponseDto activitiesResponseDto = service.findActivitiesByTeacher(principal.getName());
        if (activitiesResponseDto == null)
            throw new SubjectServiceException("Activity types not found", SubjectExceptionType.ACTIVITY_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findActivitiesOfTeacher ==========");
        return new ResponseEntity<>(activitiesResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get SPLink by id - subject id, activity id, professor username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = SpLinkResponseDto.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = SubjectExceptionType.class)
    })
    @RequestMapping(value = "/assign", method = RequestMethod.GET, params = {"subjectId", "activityTypeId", "professorUsername"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpLinkResponseDto> findSpLink(@RequestParam(name = "subjectId") String subjectId, @RequestParam(name = "activityTypeId") Integer activityTypeId, @RequestParam(name = "professorUsername") String professorUsername) {

        logger.info("========== LOGGING findSpLink ==========");
        logger.info("SubjectId {}, ActivityTypeId {}, ProfessorUsername {}", subjectId, activityTypeId, professorUsername);

        SpLinkResponseDto spLinkResponseDto = service.findSpLink(subjectId, activityTypeId, professorUsername);
        if (spLinkResponseDto == null)
            throw new SubjectServiceException("Assignment not found", SubjectExceptionType.ASSIGNMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findSpLink ==========");
        return new ResponseEntity<>(spLinkResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get SPLink by professor username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = SpLinkResponseDto.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = SubjectExceptionType.class)
    })
    @RequestMapping(value = "/teacher", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectsResponseDto> findSubjectByTeacher(Principal principal) {

        logger.info("========== LOGGING findSubjectByTeacher ==========");

        String teacherUsername = principal.getName();
        //TODO : verify if username exists
        SubjectsResponseDto subjectsResponseDto = service.findSubjectsByTeacher(teacherUsername);
        if (subjectsResponseDto == null)
            throw new SubjectServiceException("Assignment not found", SubjectExceptionType.ASSIGNMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findSubjectByTeacher ==========");
        return new ResponseEntity<>(subjectsResponseDto, HttpStatus.OK);
    }

    //TODO: apel catre student-service
    @ApiOperation(value = "Get SPLink by student username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = SpLinkResponseDto.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = SubjectExceptionType.class)
    })
    @RequestMapping(value = "/student", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectsResponseDto> findSubjectByStudent(Principal principal) {

        logger.info("========== LOGGING findSubjectByStudent ==========");

        String studentId = principal.getName();

        SubjectsByStudentDto subjectsByStudent = microserviceCall.getSubjectsIdsByStudentId(studentId);
        SubjectsResponseDto subjectsResponseDto = service.findSubjectsByStudent(studentId,subjectsByStudent);

        if (subjectsResponseDto.getSubjects().isEmpty())
            throw new SubjectServiceException("The student "+studentId+" doesn't have any subjects", SubjectExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findSubjectByStudent ==========");
        return new ResponseEntity<>(subjectsResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value ="Get specialization by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Specialization.class),
            @ApiResponse(code = 404, message = "SPECIALIZATION_NOT_FOUND", response = SubjectExceptionType.class)
    })
    @RequestMapping(value = "/specialization", method = RequestMethod.GET, params = {"specId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecializationResponseDto> findSpecialization(@RequestParam(name = "specId") Integer specId){

        logger.info(" ======= LOGGING findSpecialization ====== ");
        logger.info("By SpecId {}",specId);

        SpecializationResponseDto specializationResponseDto = service.findSpecById(specId);
        if(specializationResponseDto == null){
            throw new SubjectServiceException("Specialization not found", SubjectExceptionType.SPECIALIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        logger.info("========== SUCCESSFUL LOGGING findSpecialization {} - {} - {} ==========",specializationResponseDto.getId(),specializationResponseDto.getName(),specializationResponseDto.getLanguage());
        return new ResponseEntity<>(specializationResponseDto,HttpStatus.OK);
    }

    @ApiOperation(value = "Find subject by id")
    @RequestMapping(value = "/by", method = RequestMethod.GET, params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectResponseDto> findSubjectById(@RequestParam(name = "id") String id){

        logger.info(" ============= LOGGING findSubjectById ===========");
        logger.info("Id {}",id);

        SubjectResponseDto subjectResponseDto = service.findSubjectById(id);
        if(subjectResponseDto == null){
            throw new SubjectServiceException("Subject not found", SubjectExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        logger.info("========== SUCCESSFUL LOGGING findSubjectById =========");
        return new ResponseEntity<>(subjectResponseDto,HttpStatus.OK);
    }

    @ApiOperation(value = "Find subject name and activity name by ids")
    @RequestMapping(value = "/course-activity", method = RequestMethod.GET, params = {"courseId","activityId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectActivityResponseDto> findSubjectActivityByIds(@RequestParam(name = "courseId") String courseId,@RequestParam(name = "activityId") Integer activityId){

        logger.info(" >>>>>>>>>> LOGGING findSubjectActivityByIds <<<<<<<<<<<<");
        logger.info("Subject id {}",courseId);
        logger.info("Activity id {}",activityId);

        SubjectActivityResponseDto subjectActivityByIds = service.findSubjectActivityByIds(courseId,activityId);
        if(subjectActivityByIds == null){
            throw new SubjectServiceException("Subject or Activity not found", SubjectExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        logger.info(">>>>>>>>>> SUCCESSFUL LOGGING findSubjectActivityByIds <<<<<<<<<<<<");
        return new ResponseEntity<>(subjectActivityByIds,HttpStatus.OK);
    }


    private void loggingSubject(SubjectRequestDto subject) {

        logger.info("Subject name: {}", subject.getName());
        logger.info("Subject credits: {}", subject.getCredits());
        logger.info("Subject specialization: {}", subject.getSpecId());
        logger.info("Subject year: {}", subject.getYear());
    }

    @ExceptionHandler({SubjectServiceException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(SubjectServiceException exception) {

        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), exception.getHttpStatus());
    }
}
