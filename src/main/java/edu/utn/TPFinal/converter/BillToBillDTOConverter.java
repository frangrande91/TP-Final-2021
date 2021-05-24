package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Dto.BillDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BillToBillDTOConverter implements Converter<Bill, BillDto> {

    private final ModelMapper modelMapper;

    public BillToBillDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BillDto convert(Bill source) {
        return modelMapper.map(source, BillDto.class);
    }

}
