package ro.ubbcluj.cs.ams.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GroupsResponseDto {

    List<GroupUniversity> groups;
}
