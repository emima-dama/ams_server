package ro.ubbcluj.cs.ams.assignment.dto;

import org.mapstruct.Mapper;
import ro.ubbcluj.cs.ams.assignment.model.tables.pojos.Grade;
import ro.ubbcluj.cs.ams.assignment.model.tables.records.GradeRecord;

import java.util.List;


@Mapper(componentModel = "spring")
public interface GradeMapper {

    Grade gradeDtoToGrade(GradeDto gradeDto);

    GradeResponseDto gradeRecordToGradeResponseDto(GradeRecord gradeRecord);

    List<GradeResponseDto> gradeRecordstoGradeResponseDtos(List<GradeRecord> gradeRecords);
}
