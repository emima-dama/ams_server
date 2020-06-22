package ro.ubbcluj.cs.ams.subject.dto;

import org.mapstruct.Mapper;
import ro.ubbcluj.cs.ams.subject.model.tables.records.SubjectRecord;
//import ro.ubbcluj.cs.ams.subject.model.tables.records.SubjectRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectResponseDto subjectRecordToSubjectResponseDto(SubjectRecord subjectRecord);

    List<SubjectResponseDto> subjectRecordsToSubjectResponseDtos(List<SubjectRecord> subjectRecords);
}
