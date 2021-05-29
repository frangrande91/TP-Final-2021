package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.Measurement;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeasurementToMeasurementDTOConverter implements Converter<Measurement, MeasurementDto> {

    private final ModelMapper modelMapper;

    public MeasurementToMeasurementDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MeasurementDto convert(Measurement source) {
        return modelMapper.map(source, MeasurementDto.class);
    }

}

