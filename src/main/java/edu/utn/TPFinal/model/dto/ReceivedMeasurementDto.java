package edu.utn.TPFinal.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceivedMeasurementDto {
    String serialNumber;
    float value;
    String date;
    String password;
}
