package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.Dto.ModelDto;
import edu.utn.TPFinal.model.Dto.RateDto;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.Rate;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RateToRateDTOConverter implements Converter<Rate, RateDto> {

    private final ModelMapper modelMapper;

    public RateToRateDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RateDto convert(Rate source) {
        return modelMapper.map(source, RateDto.class);
    }
}
