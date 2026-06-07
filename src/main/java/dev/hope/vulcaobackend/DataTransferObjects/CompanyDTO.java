package dev.hope.vulcaobackend.DataTransferObjects;


import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Common.Address;
import dev.hope.vulcaobackend.Models.Properties.BusinessCategory;
import dev.hope.vulcaobackend.Models.Properties.CompanyStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO extends BaseModelDTO {
    private String name;
    private BusinessCategory category;
    private String taxIdNumber;
    private String description;
    private String phoneNumber;
    private String faxNumber;
    private Double revenue;
    private String email;
    private Address address;
    private CompanyStatuses status;
    private String creatorUsername;
    private List<String> imgS3UrlKeys;

}
