package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.dto.BrandDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrandToBrandDTOConverter implements Converter<Brand, BrandDto> {

    private final ModelMapper modelMapper;

    public BrandToBrandDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BrandDto convert(Brand source) {
        return modelMapper.map(source, BrandDto.class);
    }

}
