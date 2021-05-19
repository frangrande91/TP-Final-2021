package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.Dto.MeterDto;
import edu.utn.TPFinal.model.Meter;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeterToMeterDTOConverter implements Converter<Meter, MeterDto> {

    private final ModelMapper modelMapper;

    public MeterToMeterDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MeterDto convert(Meter source) {
        return modelMapper.map(source, MeterDto.class);
    }

}
