package edu.utn.TPFinal.converter;

import edu.utn.TPFinal.model.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.AddressTestUtils.aAddressDto;
import static edu.utn.TPFinal.utils.BillTestUtils.aBill;
import static edu.utn.TPFinal.utils.BillTestUtils.aBillDto;
import static edu.utn.TPFinal.utils.MeasurementTestUtils.aMeasurement;
import static edu.utn.TPFinal.utils.MeasurementTestUtils.aMeasurementDto;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterDto;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static edu.utn.TPFinal.utils.RateTestUtils.aRateDto;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;
import static edu.utn.TPFinal.utils.UserTestUtils.aUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllConverter {

    private ModelMapper modelMapper;


    private AddressToAddressDTOConverter addressToAddressDTOConverter;
    private BillToBillDTOConverter billToBillDTOConverter;
    //private BrandToBrandDTOConverter brandToBrandDTOConverter;
    private MeasurementToMeasurementDTOConverter measurementToMeasurementDTOConverter;
    private MeterToMeterDTOConverter meterToMeterDTOConverter;
    //private ModelToModelDTOConverter modelToModelDTOConverter;
    private RateToRateDTOConverter rateToRateDTOConverter;
    private UserToUserDTOConverter userToUserDTOConverter;


    @BeforeEach
    public void setUp() {
        modelMapper = mock(ModelMapper.class);

        addressToAddressDTOConverter = new AddressToAddressDTOConverter(modelMapper);
        billToBillDTOConverter = new BillToBillDTOConverter(modelMapper);
        //brandToBrandDTOConverter = new BrandToBrandDTOConverter(modelMapper);
        measurementToMeasurementDTOConverter = new MeasurementToMeasurementDTOConverter(modelMapper);
        meterToMeterDTOConverter = new MeterToMeterDTOConverter(modelMapper);
        //modelToModelDTOConverter = new ModelToModelDTOConverter(modelMapper);
        rateToRateDTOConverter = new RateToRateDTOConverter(modelMapper);
        userToUserDTOConverter = new UserToUserDTOConverter(modelMapper);
    }

    @Test
    public void convertAddress() {
        when(modelMapper.map(aAddress(), AddressDto.class)).thenReturn(aAddressDto());
        AddressDto addressDto = addressToAddressDTOConverter.convert(aAddress());
        assertEquals(aAddressDto(), addressDto);
    }

    @Test
    public void convertBill() {
        when(modelMapper.map(aBill(), BillDto.class)).thenReturn(aBillDto());
        BillDto billDto = billToBillDTOConverter.convert(aBill());
        assertEquals(aBillDto(), billDto);
    }


    @Test
    public void convertMeasurement() {
        when(modelMapper.map(aMeasurement(), MeasurementDto.class)).thenReturn(aMeasurementDto());
        MeasurementDto measurementDto = measurementToMeasurementDTOConverter.convert(aMeasurement());
        assertEquals(aMeasurementDto(),measurementDto);
    }

    @Test
    public void convertMeter() {
        when(modelMapper.map(aMeter(), MeterDto.class)).thenReturn(aMeterDto());
        MeterDto meterDto = meterToMeterDTOConverter.convert(aMeter());
        assertEquals(aMeterDto(),meterDto);
    }

    @Test
    public void convertRate() {
        when(modelMapper.map(aRate(), RateDto.class)).thenReturn(aRateDto());
        RateDto rateDto = rateToRateDTOConverter.convert(aRate());
        assertEquals(aRateDto(),rateDto);
    }

    @Test
    public void convertUser() {
        when(modelMapper.map(aUser(),UserDto.class)).thenReturn(aUserDto());
        UserDto userDto = userToUserDTOConverter.convert(aUser());
        assertEquals(aUserDto(),userDto);
    }

}
