package com.pm.patientservice.model.mapper;

import com.pm.patientservice.model.dto.PatientDTO;
import com.pm.patientservice.model.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PatientMapper {

  /**
   * Converts a PatientDTO to a Patient entity. Maps string date fields to LocalDate.
   */
  Patient toEntity(final PatientDTO patientDTO);

  /**
   * Converts a Patient entity to a PatientDTO. Maps LocalDate fields to string.
   */

  PatientDTO toDto(final Patient patient);
}