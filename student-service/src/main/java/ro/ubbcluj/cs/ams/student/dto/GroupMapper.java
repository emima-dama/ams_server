package ro.ubbcluj.cs.ams.student.dto;

import org.mapstruct.Mapper;
import ro.ubbcluj.cs.ams.student.model.tables.records.GroupUniversityRecord;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponseDto groupUniversityRecordToGroupResponseDto(GroupUniversityRecord groupUniversityRecord);
}
