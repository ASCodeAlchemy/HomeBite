package com.homebite.provider_service.DTOs.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderPayoutDetailsDTO {
    @NotBlank
    private String bankAccountNumber;
    @NotBlank
    private String bankIfsc;
    @NotBlank
    private String bankAccountHolderName;
    @NotBlank
    private String panNumber;
}
