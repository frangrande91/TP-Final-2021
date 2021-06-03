package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressTestUtils {

    public static Address aAddress() {
        return Address.builder().id(1).address("False Street 123").build();
    }

    public static AddressDto aAddressDto(){
        return new AddressDto();
    }

    public static List<AddressDto> aListAddressDto(){
        return new ArrayList<AddressDto>();
    }

    public static Page<Address> aAddressPage() {
        return new PageImpl<>(List.of(aAddress()));
    }

    public static Page<Address> aAddressPageEmpty() {
        List<Address> addressList = Collections.emptyList();
        return new PageImpl<>(addressList);
    }

}