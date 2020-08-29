package ro.ubbcluj.cs.ams.assignment.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.ams.assignment.dto.GradeDto;
import ro.ubbcluj.cs.ams.assignment.dto.GradeResponseDto;
import ro.ubbcluj.cs.ams.assignment.dto.GradesResponseDto;
import ro.ubbcluj.cs.ams.assignment.service.Service;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceException;
import ro.ubbcluj.cs.ams.assignment.service.exception.AssignmentServiceExceptionType;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class AssignmentController {

    @Autowired
    private Service service;

    @Autowired
    private MicroserviceCall microserviceCall;

    private final Logger logger = LogManager.getLogger(AssignmentController.class);

    @ApiOperation(value = "Assign given grade to the specified student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = GradeResponseDto.class),
            @ApiResponse(code = 400, message = "INVALID_GRADE", response = AssignmentServiceExceptionType.class),
    })
    @RequestMapping(value = "/grades", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GradeResponseDto> assignGrade(@RequestBody @Valid GradeDto gradeDto, BindingResult result, Principal principal) {

        logger.info(">>>>>>>>>>>>> LOGGING assignGrade <<<<<<<<<<<<<<<<");
        logger.info("GradeDto {}", gradeDto);
        if (result.hasErrors()) {
            logger.error("Invalid json object!");
            throw new AssignmentServiceException("Invalid grade" + gradeDto, AssignmentServiceExceptionType.INVALID_GRADE, HttpStatus.BAD_REQUEST);
        }

        String teacherUsername = principal.getName();
        microserviceCall.checkIfProfessorTeachesSpecificActivityTypeForASubject(gradeDto.getSubjectId(), gradeDto.getTypeId(), teacherUsername);
        GradeResponseDto gradeResponseDto = service.addGrade(gradeDto, teacherUsername);

        logger.info(">>>>>>>>>>>>> SUCCESSFUL LOGGING assignGrade <<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(gradeResponseDto, HttpStatus.OK);
    }


    @ApiOperation(value = "Find all grades of a student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = GradeResponseDto.class),
            @ApiResponse(code = 400, message = "INVALID_GRADE", response = AssignmentServiceExceptionType.class),
    })
    @RequestMapping(value = "/student", method = RequestMethod.GET,params={"studentId"},consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GradesResponseDto> findGradesByStudent(@RequestParam(name = "studentId")String studentId) {

        logger.info(">>>>>>>>>>>>> LOGGING findGradesByStudent {} <<<<<<<<<<<<<<<<",studentId);

        GradesResponseDto gradeResponseDto = service.findGradesByStudent(studentId);

        logger.info(">>>>>>>>>>>>> SUCCESSFUL LOGGING assignGrade <<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(gradeResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Find all grades of a student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = GradeResponseDto.class),
            @ApiResponse(code = 400, message = "INVALID_GRADE", response = AssignmentServiceExceptionType.class),
    })
    @RequestMapping(value = "/student", method = RequestMethod.GET,params={"id"},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GradesResponseDto> findGradesByStudent2(@RequestParam(name = "id")String id, Principal principal) {

        logger.info(">>>>>>>>>>>>> LOGGING findGradesByStudent2 {} <<<<<<<<<<<<<<<<",principal.getName());

        GradesResponseDto gradeResponseDto = service.findGradesByStudent(id);

        logger.info(">>>>>>>>>>>>> SUCCESSFUL LOGGING findGradesByStudent2 <<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(gradeResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Find all grades of a student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = GradeResponseDto.class),
            @ApiResponse(code = 400, message = "INVALID_GRADE", response = AssignmentServiceExceptionType.class),
    })
    @RequestMapping(value = "/grades", method = RequestMethod.GET,params={"subject"},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GradesResponseDto> findGradesBySubject(@RequestParam(name = "subject")String subject, Principal principal) {

        logger.info(">>>>>>>>>>>>> LOGGING findGradesBySubject {} <<<<<<<<<<<<<<<<",principal.getName());

        GradesResponseDto gradeResponseDto = service.findGradesBySubject(subject,principal.getName());

        logger.info(">>>>>>>>>>>>> SUCCESSFUL LOGGING findGradesBySubject <<<<<<<<<<<<<<<<");
        return new ResponseEntity<>(gradeResponseDto, HttpStatus.OK);
    }
}
