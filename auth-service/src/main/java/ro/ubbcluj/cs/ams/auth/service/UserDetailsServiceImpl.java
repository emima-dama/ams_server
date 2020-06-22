package ro.ubbcluj.cs.ams.auth.service;

import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.ubbcluj.cs.ams.auth.config.AuthConfiguration;
import ro.ubbcluj.cs.ams.auth.dao.roleDao.RoleDao;
import ro.ubbcluj.cs.ams.auth.dao.roleUserDao.RoleUserDao;
import ro.ubbcluj.cs.ams.auth.dto.RoleDto;
import ro.ubbcluj.cs.ams.auth.model.AuthUserDetail;
import ro.ubbcluj.cs.ams.auth.model.User;
import ro.ubbcluj.cs.ams.auth.dao.UserDetailsRepository;
import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleRecord;
import ro.ubbcluj.cs.ams.auth.model.tables.records.RoleUserRecord;

import java.util.Optional;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthConfiguration authProps;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private RoleUserDao roleUserDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Optional<User> optionalUser = userDetailsRepository.findByUsername(name);
        //TODO
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password incorrect!"));

        UserDetails userDetails = new AuthUserDetail(optionalUser.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public RoleDto findRoleByUsername(String username){

        logger.info("username: {}",username);
        //Optional<User> optionalUser = userDetailsRepository.findByUsername(username);

        UserDetails userDetails = this.loadUserByUsername(username);

        return RoleDto.builder()
                .role(userDetails.getAuthorities().toArray()[0].toString())
                .build();
    }
}
