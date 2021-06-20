package edu.utn.TPFinal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedMeasurementDto {
    String serialNumber;
    float value;
    String date;
    String password;
}
