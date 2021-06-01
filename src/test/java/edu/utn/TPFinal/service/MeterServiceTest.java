package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.repository.MeterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static edu.utn.TPFinal.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MeterServiceTest {

    @InjectMocks
    private MeterService meterService;

    @Mock
    private MeterRepository meterRepository;

    @Test
    public void getMeterByIdOk() {

        try {
            Integer id = 1234;
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            meterService.getMeterById(id);
            Mockito.verify(meterRepository,Mockito.times(1)).findById(id);
        }
        catch (MeterNotExistsException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void getMeterByIdNotFound() throws MeterNotExistsException{
            Integer id = 1234;
        System.out.println("Llegue");
            Mockito.when(meterRepository.findById(id).orElseThrow(() -> new MeterNotExistsException("Meter not exists"))).thenReturn(null);

            Assertions.assertThrows(MeterNotExistsException.class, () -> meterService.getMeterById(id));
    }

}
