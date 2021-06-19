package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.dto.ReceivedMeasurementDto;
import edu.utn.TPFinal.model.response.ClientConsumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static edu.utn.TPFinal.utils.BillTestUtils.aBill;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterDto;
import static edu.utn.TPFinal.utils.UserTestUtils.aUserDto;

public class MeasurementTestUtils {

    public static Measurement aMeasurement() {
        return Measurement.builder()
                .id(1)
                .meter(aMeter())
                .bill(aBill())
                .quantityKw(2.0)
                .date(LocalDateTime.of(2021,5,5,0,0,0))
                .priceMeasurement(100.0)
                .build();
    }

    public static Page<Measurement> aMeasurementPage() {
        return new PageImpl<>(List.of(aMeasurement()));
    }

    public static MeasurementDto aMeasurementDto() {
        return MeasurementDto.builder().id(1).meter(aMeterDto()).bill(aBill()).quantityKw(2.0).date(LocalDateTime.of(2021,5,5,0,0,0)).priceMeasurement(100.0).build();
    }

    public static ReceivedMeasurementDto aReceivedMeasurementDto() {
        return ReceivedMeasurementDto.builder().serialNumber(null).value(2).date("2021-05-05T00:00").password("1235").build();
    }

    public static Page<MeasurementDto> aMeasurementDtoPage(){
        return new PageImpl<>(List.of(aMeasurementDto()));
    }

    public static Specification<Measurement> specMeasurement(Double quantityKw) {
        if(quantityKw == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("measurements.quantity_kw"), "%" + quantityKw + "%");
            };
        }
    }

    public static ClientConsumption aClientConsumption(){
        return ClientConsumption
                .builder()
                .consumptionKw(5.5)
                .consumptionMoney(100.0)
                .from(LocalDateTime.of(2021, 5, 5,0,0,0))
                .to(LocalDateTime.of(2021, 5, 9,0,0,0))
                .quantityMeasurements(10)
                .clientUser(aUserDto())
                .build();
    }
}


