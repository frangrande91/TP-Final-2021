package edu.utn.TPFinal.Converter;

import edu.utn.TPFinal.model.Dto.UserDto;
import edu.utn.TPFinal.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDto> {

    private final ModelMapper modelMapper;

    public UserToUserDTOConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto convert(User source) {
        return modelMapper.map(source,UserDto.class);
    }


}
