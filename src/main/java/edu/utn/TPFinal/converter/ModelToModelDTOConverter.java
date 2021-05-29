package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.dto.ModelDto;
import edu.utn.TPFinal.model.Model;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ModelToModelDTOConverter implements Converter<Model, ModelDto> {

    private final ModelMapper modelMapper;

    public ModelToModelDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ModelDto convert(Model source) {
        return modelMapper.map(source, ModelDto.class);
    }

}
