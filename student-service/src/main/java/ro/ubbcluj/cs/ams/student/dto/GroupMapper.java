package ro.ubbcluj.cs.ams.student.dto;

import org.mapstruct.Mapper;
import ro.ubbcluj.cs.ams.student.model.tables.pojos.GroupUniversity;
import ro.ubbcluj.cs.ams.student.model.tables.records.GroupUniversityRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupResponseDto groupUniversityRecordToGroupResponseDto(GroupUniversityRecord groupUniversityRecord);

    List<GroupUniversity> groupUniversityRecordsToGroupUniversities(List<GroupUniversityRecord> groupUniversityRecords);
}
