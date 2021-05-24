package edu.utn.TPFinal.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponse<T> implements Serializable {

    private List<T> response;
    private Integer totalPages;
    private Long totalElements;
}
