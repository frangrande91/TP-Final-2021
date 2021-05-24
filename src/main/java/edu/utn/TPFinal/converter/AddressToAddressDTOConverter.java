package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressToAddressDTOConverter implements Converter<Address, AddressDto> {

    private final ModelMapper modelMapper;

    public AddressToAddressDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDto convert(Address source) {
        return modelMapper.map(source,AddressDto.class);
    }

}
