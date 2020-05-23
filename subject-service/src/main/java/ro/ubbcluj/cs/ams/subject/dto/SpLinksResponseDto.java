package ro.ubbcluj.cs.ams.subject.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class SpLinksResponseDto {

    private List<SpLinkResponseDto> spLinks;
}
