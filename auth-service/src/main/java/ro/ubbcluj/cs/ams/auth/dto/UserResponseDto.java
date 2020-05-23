package ro.ubbcluj.cs.ams.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class UserResponseDto {

    private String username;
    private String password;
}
