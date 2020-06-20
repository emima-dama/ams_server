package ro.ubbcluj.cs.ams.attendance.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.ams.attendance.dto.AttendanceInfoReq;
import ro.ubbcluj.cs.ams.attendance.dto.AttendanceInfoResponse;
import ro.ubbcluj.cs.ams.attendance.dto.AttendanceRequest;
import ro.ubbcluj.cs.ams.attendance.dto.AttendanceResponse;
import ro.ubbcluj.cs.ams.attendance.service.Service;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceExceptionType;
import ro.ubbcluj.cs.ams.attendance.service.exception.AttendanceServiceException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Timed
public class  AttendanceController {

    @Autowired
    Service service;

    private Logger logger = LogManager.getLogger(AttendanceController.class);

    private final Counter counter;

    public AttendanceController(MeterRegistry registry) {

        this.counter = registry.counter("attendance.messages","type","requests");

    }

    @Scheduled(fixedRate = 5000)
    @Timed(description = "Time spent serving orders")
    public void verifyNrOfReq() throws InterruptedException {
        if (counter.count() > 2.0) {
            logger.info("!!!!Needs to create another instance!!!!!");
            Thread.sleep(1000);
        }
    }


    @ApiOperation(value = "Add attendance info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = AttendanceExceptionType.class),
            @ApiResponse(code = 400, message = "DUPLICATE_ATTENDANCE_INFO", response = AttendanceExceptionType.class),
    })
    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AttendanceInfoResponse> addAttendanceInfo(Principal principal, @RequestBody @Valid AttendanceInfoReq attendanceInfoReq, BindingResult result) {

        if (result.hasErrors())
            throw new AttendanceServiceException("Invalid attendance_info " + attendanceInfoReq, AttendanceExceptionType.INVALID_ATTENDANCE_INFO, HttpStatus.BAD_REQUEST);

        logger.info(principal.getName());
        AttendanceInfoResponse attendanceInfoResponse = service.addAttendanceInfo(attendanceInfoReq, principal.getName());

        return new ResponseEntity<>(attendanceInfoResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Add attendance by student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = AttendanceExceptionType.class),
            @ApiResponse(code = 400, message = "INVALID_TIME", response = AttendanceExceptionType.class),
    })
    @RequestMapping(value = "/student", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AttendanceResponse> addAttendance(Principal principal, @RequestBody @Valid AttendanceRequest attendanceRequest, BindingResult result){

        if (result.hasErrors())
            throw new AttendanceServiceException("Invalid attendance " + attendanceRequest, AttendanceExceptionType.INVALID_ATTENDANCE, HttpStatus.BAD_REQUEST);

        logger.info("++++++ addAttendace with attendance info id : "+attendanceRequest.getAttendanceInfoCode()+"++++++++++++++");

        AttendanceResponse attendanceResponse = service.addAttendance(attendanceRequest,principal.getName());

        return new ResponseEntity<>(attendanceResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Add attendance by teacher")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = AttendanceExceptionType.class),
            @ApiResponse(code = 400, message = "INVALID_TIME", response = AttendanceExceptionType.class),
    })
    @RequestMapping(value = "/teacher", method = RequestMethod.POST,params={"studentId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AttendanceResponse> addAttendance(@RequestParam(name = "studentId") String studentId, @RequestBody @Valid AttendanceRequest attendanceRequest, BindingResult result){

        if (result.hasErrors())
            throw new AttendanceServiceException("Invalid attendance " + attendanceRequest, AttendanceExceptionType.INVALID_ATTENDANCE, HttpStatus.BAD_REQUEST);

        logger.info("++++++ addAttendace with attendance info id : {}++++++++++++++",attendanceRequest.getAttendanceInfoCode());

        AttendanceResponse attendanceResponse = service.addAttendance(attendanceRequest,studentId);

        return new ResponseEntity<>(attendanceResponse, HttpStatus.OK);
    }

    @ExceptionHandler({AttendanceServiceException.class})
    @ResponseBody
    public ResponseEntity<AttendanceExceptionType> handleException(AttendanceServiceException exception) {

        return new ResponseEntity<>(exception.getType(), new HttpHeaders(), exception.getHttpStatus());
    }

}
