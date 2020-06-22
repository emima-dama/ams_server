package ro.ubbcluj.cs.ams.auth.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ro.ubbcluj.cs.ams.auth.config.AuthConfiguration;
import ro.ubbcluj.cs.ams.auth.dto.RoleDto;
import ro.ubbcluj.cs.ams.auth.dto.UserDto;
import ro.ubbcluj.cs.ams.auth.dto.UserResponseDto;
import ro.ubbcluj.cs.ams.auth.model.Role;
import ro.ubbcluj.cs.ams.auth.service.UserDetailsServiceImpl;
import ro.ubbcluj.cs.ams.auth.service.exception.AuthExceptionType;
import ro.ubbcluj.cs.ams.auth.service.exception.AuthServiceException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Timed
public class  AuthController {

    @Autowired
    private AuthConfiguration authProps;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private UserDetailsServiceImpl service;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

//    private Counter steveCounter;
//    private Timer findPersonTimer;

//    public AuthController(MeterRegistry registry) {
//        // constructs a gauge to monitor the size of the population
////        registry.mapSize("accounts", people);
//
//        // register a counter of questionable usefulness
//        steveCounter = registry.counter("find_steve");//, /* optional tags here */);
//
//        // register a timer -- though for request timing it is easier to use @Timed
//        findPersonTimer = registry.timer("http_requests", "method", "POST");
//    }

    private final Counter counter;

    public AuthController(MeterRegistry registry) {
        this.counter = registry.counter("received.messages");
    }

    public void handleMessage(String message) {
        this.counter.increment();
        // handle message implementation
    }


    @GetMapping("/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"username", "password"}
    )
//    @Timed(extraTags = { "region", "us-east-1" })
//    @Timed(value = "all.people", longTask = true)
    public ResponseEntity<OAuth2AccessToken> login(@Valid UserDto userDto, BindingResult result) {

        logger.info("==========login==========");
        logger.info("===========username: {}==========", userDto.getUsername());
        logger.info("===========password: {}==========", userDto.getPassword());

        if (result.hasErrors()) {
            logger.error("==========login failed==========");
            logger.error("Unexpected data!");
            throw new AuthServiceException("Invalid credentials!", AuthExceptionType.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST);
        }

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "password");
        bodyParams.add("username", userDto.getUsername());
        bodyParams.add("password", userDto.getPassword());

        String auth = authProps.getUser() + ":" + authProps.getPass();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);

        try{
            OAuth2AccessToken accessToken = webClientBuilder
                    .build()
                    .post()
                    .uri("http://auth-service/authentication/oauth/token")
                    .header("Authorization", authHeader)
                    .body(BodyInserters.fromValue(bodyParams))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(OAuth2AccessToken.class)
                    .block();
            this.counter.increment();

            logger.info("==========login successful==========");
            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        }catch (Exception e){
            throw new AuthServiceException("The account data "+userDto.getUsername()+" ; "+userDto.getPassword()+" isn\'t available!",AuthExceptionType.INVALID_CREDENTIALS,HttpStatus.BAD_REQUEST);
        }
//        steveCounter.increment();
    }

    @RequestMapping(value = "/role",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RoleDto> findRoleOfUser(Principal principal) {

        logger.info("======== findRoleOfUser =========");
        logger.info("User: {}", principal.getName());

        RoleDto roleDto = service.findRoleByUsername(principal.getName());
        logger.info("========= SUCCESSFUL findRoleOfUser ==========");
        return new ResponseEntity<>(roleDto,HttpStatus.OK);
    }

    @ExceptionHandler({AuthServiceException.class})
    @ResponseBody
    public ResponseEntity<String> handleException(AuthServiceException exception) {

        return new ResponseEntity<>(exception.getMessage(), new HttpHeaders(), exception.getHttpStatus());
    }

    @ApiOperation(value = "Find account by username")
//    @ApiResponses(value = {
//    })
    @RequestMapping(value = "/username", method = RequestMethod.GET,params = {"username"},produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAccountByUsername(@RequestParam(name = "username") String username){

        logger.info("========= LOGGING findAccountByUsername {} ==========",username);

        UserDetails userDetails = service.loadUserByUsername(username);
        if(userDetails == null){
            logger.info("========== findAccountByUsername {} ===========",username);
        }

        logger.info("========= SUCCESSFUL LOGGING findAccountByUsername {} ===========",username);
        return new ResponseEntity<>("Account with username "+username+" exists ",HttpStatus.OK);
    }

}
