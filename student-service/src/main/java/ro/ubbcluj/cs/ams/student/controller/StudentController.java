package ro.ubbcluj.cs.ams.student.controller;

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
import ro.ubbcluj.cs.ams.student.dto.*;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.Enrollment;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;
import ro.ubbcluj.cs.ams.student.service.Service;
import ro.ubbcluj.cs.ams.student.service.exception.StudentExceptionType;
import ro.ubbcluj.cs.ams.student.service.exception.StudentServiceException;

import javax.validation.Valid;

@RestController
public class StudentController {

    @Autowired
    private Service service;

    private final Logger logger = LogManager.getLogger(StudentController.class);

    @Autowired
    private MicroserviceCall microserviceCall;

    @ApiOperation(value = "Add a specific group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = GroupUniversity.class),
            @ApiResponse(code = 400, message = "DUPLICATE_GROUP", response = StudentExceptionType.class),
            @ApiResponse(code = 400, message = "INVALID_GROUP", response = StudentExceptionType.class),
            @ApiResponse(code = 404, message = "SPECIALIZATION_NOT_FOUND", response = StudentExceptionType.class),
            @ApiResponse(code = 502, message = "ERROR", response = StudentExceptionType.class)
    })
    @RequestMapping(value = "/group", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupResponseDto> addGroup(@Valid @RequestBody GroupRequestDto groupDto, BindingResult result) {

        logger.info("========= LOGGING addGroup ============ ");
        loggingGroupRequestDto(groupDto);

        if (result.hasErrors())
            throw new StudentServiceException("Username and group id can not be null!", StudentExceptionType.INVALID_GROUP, HttpStatus.BAD_REQUEST);

        microserviceCall.checkIfExistsASpecialization(groupDto.getSpecId());
        GroupResponseDto savedGroup = service.addGroup(groupDto);
        logger.info("Saved group id: {}", savedGroup.getIdGroup());
        logger.info(" =========== SUCCESSFUL LOGGING addGroup ==============");
        return new ResponseEntity<>(savedGroup, HttpStatus.OK);
    }

    @ApiOperation(value = "Enroll logged in student to a specific subject")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Enrollment.class),
            @ApiResponse(code = 400, message = "DUPLICATE_ENROLLMENT", response = StudentExceptionType.class),
            @ApiResponse(code = 400, message = "INVALID_ENROLLMENT", response = StudentExceptionType.class),
            @ApiResponse(code = 404, message = "USERNAME_NOT_FOUND", response = StudentExceptionType.class),
            @ApiResponse(code = 404, message = "SUBJECT_NOT_FOUND", response = StudentExceptionType.class),
            @ApiResponse(code = 502, message = "ERROR", response = StudentExceptionType.class)
    })
    @RequestMapping(value = "/enrollment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEnrollment(@Valid @RequestBody EnrollmentRequestDto enrollmentRequestDto, BindingResult result){//}), Principal principal) {

        logger.info(" ============ LOGGING addEnrollment =============");
        logger.info("Student: {}", enrollmentRequestDto.getStudentUsername());
        logger.info("Subject: {}", enrollmentRequestDto.getSubjectId());

        if (result.hasErrors())
            throw new StudentServiceException("Subject id can not be empty", StudentExceptionType.INVALID_ENROLLMENT, HttpStatus.BAD_REQUEST);

        microserviceCall.checkIfExistsStudentUsername(enrollmentRequestDto.getStudentUsername());
        microserviceCall.checkIfExistsSubjectById(enrollmentRequestDto.getSubjectId());
        boolean response = service.addEnrollment(enrollmentRequestDto);

        if(!response){
            logger.info("=========== ERROR addEnrollment ==========");
            return new ResponseEntity<>("Cannot add enrollment for student "+enrollmentRequestDto.getStudentUsername()+" at subject " +enrollmentRequestDto.getSubjectId()+" !!!!!",HttpStatus.BAD_REQUEST);
        }

        logger.info("============== SUCCESSFUL LOGGING addEnrollment ============");
        return new ResponseEntity<>("Add enrollment with SUCCESS :)", HttpStatus.OK);
    }

    @ApiOperation(value = "Get students by subject enrollments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Enrollment.class),
            @ApiResponse(code = 502, message = "ERROR", response = StudentExceptionType.class)
    })
    @RequestMapping(value = "/students", method = RequestMethod.GET, params = {"subjectId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentsBySubjectsResponseDto> getStudentsBySubjects(@RequestParam(name = "subjectId") String subjectId){

        logger.info("========== LOGGING getStudentBySubjects ============");
        logger.info("Subject id : {} ",subjectId);
//        if (result.hasErrors())
//            throw new StudentServiceException("Subjects ids can not be empty", StudentExceptionType.ERROR, HttpStatus.BAD_REQUEST);

        StudentsBySubjectsResponseDto students = service.getStudentsBySubjects(subjectId);

        if(students == null)
            throw new StudentServiceException("The response is null !", StudentExceptionType.ERROR, HttpStatus.BAD_REQUEST);

        logger.info("========== SUCCESSFUL LOGGING getStudentsBySubjects =============");
        return new ResponseEntity<>(students,HttpStatus.OK);

    }

    @ApiOperation(value = "Get subjects by student username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = StudentExceptionType.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = StudentExceptionType.class)
    })
    @RequestMapping(value = "/subjects", method = RequestMethod.GET, params = {"studentId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectsByStudentDto> findSubjectsByStudent(@RequestParam(name = "studentId") String studentId) {

        logger.info("========== LOGGING findSubjectByStudent ==========");

        //TODO : verify if username exists
        SubjectsByStudentDto subjectsByStudentDto = service.findSubjectsByStudent(studentId);
        if (subjectsByStudentDto == null)
            throw new StudentServiceException("There is no one subject for student "+studentId, StudentExceptionType.SUBJECT_NOT_FOUND, HttpStatus.NOT_FOUND);

        logger.info("========== SUCCESSFUL LOGGING findSubjectByStudent ==========");
        return new ResponseEntity<>(subjectsByStudentDto, HttpStatus.OK);
    }

    private void loggingGroupRequestDto(GroupRequestDto groupDto) {

        logger.info("name: {}", groupDto.getName());
        logger.info("specId: {}", groupDto.getSpecId());
        logger.info("year: {}", groupDto.getYear());
    }

    @ExceptionHandler({StudentServiceException.class})
    @ResponseBody
    public ResponseEntity<StudentExceptionType> handleException(StudentServiceException exception) {

        return new ResponseEntity<>(exception.getType(), new HttpHeaders(), exception.getHttpStatus());
    }
}
