package account.api.dto.in;

import lombok.Getter;

import java.util.List;

@Getter
public class ListOfPayrollsDTO {
    private List<PayrollDTO> payrollDTOList;
}
