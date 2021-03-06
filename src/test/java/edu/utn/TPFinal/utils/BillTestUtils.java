package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;

public class BillTestUtils {

    public static Bill aBill(){
        return Bill.builder().id(1).address(aAddress()).meter(aMeter()).userClient(aUser()).measurementList(new ArrayList<>()).build();
    }

    public static Page<Bill> aBillPage(){
        return new PageImpl<>(List.of(aBill()));
    }

    public static BillDto aBillDto(){
        return BillDto.builder().id(1).build();
    }

    public static Page<BillDto> aBillDtoPage(){
        return new PageImpl<>(List.of(aBillDto()));
    }


    public static Page<Bill> aBillPageEmpty() {
        List<Bill> billList = Collections.emptyList();
        return new PageImpl<>(billList);
    }

    public static Page<BillDto> aBillDtoPageEmpty() {
        List<BillDto> billDtoList = Collections.emptyList();
        return new PageImpl<>(billDtoList);
    }


    public static Specification<Bill> aSpecBill(String value) {
        if(value == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("bill.address.address"), "%" + value + "%");
            };
        }
    }



}
