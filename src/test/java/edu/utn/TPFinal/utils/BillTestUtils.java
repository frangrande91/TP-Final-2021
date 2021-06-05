package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;

public class BillTestUtils {

    public static Bill aBill(){
        return Bill.builder().id(1).address(aAddress()).meter(aMeter()).userClient(aUser()).build();
    }

    public static Page<Bill> aBillPage(){
        return new PageImpl<>(List.of(aBill()));
    }

    public static Page<Bill> aBillPageEmpty() {
        List<Bill> billList = Collections.emptyList();
        return new PageImpl<>(billList);
    }

    public static BillDto aBillDto(){
        return new BillDto();
    }



}