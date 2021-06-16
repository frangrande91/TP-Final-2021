package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.dto.MeterDto;
import edu.utn.TPFinal.model.dto.ReceivedMeasurementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static edu.utn.TPFinal.utils.BillTestUtils.aBill;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterDto;

public class MeasurementTestUtils {

    public static Measurement aMeasurement() {
        return Measurement.builder().id(1).meter(aMeter()).bill(aBill()).quantityKw(1.0).date(LocalDate.of(2021,5,5)).priceMeasurement(100.0).build();
    }

    public static Page<Measurement> aMeasurementPage() {
        return new PageImpl<>(List.of(aMeasurement()));
    }

    public static MeasurementDto aMeasurementDto() {
        return MeasurementDto.builder().id(1).meter(aMeterDto()).bill(aBill()).quantityKw(1.0).date(LocalDate.of(2021,5,5)).priceMeasurement(100.0).build();
    }

    public static ReceivedMeasurementDto aReceivedMeasurementDto() {
        return ReceivedMeasurementDto.builder().serialNumber("1234").value(2).date("2020-05-05").password("1234").build();
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


}
