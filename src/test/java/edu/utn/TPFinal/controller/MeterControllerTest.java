package edu.utn.TPFinal.controller;
import edu.utn.TPFinal.AbstractController;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repositories.ClassRepository;
import edu.utn.TPFinal.repository.MeterRepository;
import edu.utn.TPFinal.service.MeterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import static edu.utn.TPFinal.utils.TestUtils.*;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Assert.*;



@SpringBootTest(classes = MeterController.class)
@Slf4j
public class MeterControllerTest extends AbstractController {


    @MockBean
    MeterRepository meterRepository;
    @MockBean
    ClassRepository classRepository;
    @MockBean
    private MeterService meterService;
    @MockBean
    private FormattingConversionService formattingConversionService;
    @Mock
    Root<Meter> root;
	@Mock
    CriteriaQuery<?> query;
	@Mock
    CriteriaBuilder builder;
	@Mock
    Predicate predicate;

    @Mock Specification<Meter> meterSpecification;

    @Test
    public void getAllMeters() throws Exception {

        Mockito.when(meterService.getAllMeters(any())).thenReturn(aMeterPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/meters/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSpec() throws Exception {

      /*  Mockito.when(meterSpecification.toPredicate(root, query, builder)).thenReturn(predicate);
        String searchString = "123456";

        List<Meter> meterList = meterRepository.findAll(Specification.where(hasString(searchString)));
                .or(hasClasses(searchString,classRepository))))

        System.out.println(meterList);

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/meter/spec?serialNumber=123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                ;



        assertEquals(0, meterList.size());
        assertEquals(meterSpecification, is(notNullValue()));
        assertEquals(meterSpecification.toPredicate(root,query,builder), is(notNullValue()));*/

      //  assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }



    @Test
    public void getAllSorted() throws Exception {

        Mockito.when(meterService.getAllSort(any(),any(),anyList())).thenReturn(aMeterPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/meters/sort?field1=serialNumber&field2=id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                ;

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getMeterById() throws Exception {
        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .get("/meters/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void addMeter() throws Exception{

        Mockito.when(meterService.addMeter(aMeter())).thenReturn(aMeter());

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .post("/meters/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aMeterJSON()))
                .andExpect(status().isCreated())
                ;

        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void deleteMeterById() throws Exception{

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .delete("/meters/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assertEquals(HttpStatus.ACCEPTED.value(), resultActions.andReturn().getResponse().getStatus());
    }

}
