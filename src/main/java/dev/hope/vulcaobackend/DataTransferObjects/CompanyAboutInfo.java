package dev.hope.vulcaobackend.DataTransferObjects;

import dev.hope.vulcaobackend.Models.Properties.CompanyStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAboutInfo {
    private String description;
    private CompanyStatuses status;
    private String taxIdNumber;
}
