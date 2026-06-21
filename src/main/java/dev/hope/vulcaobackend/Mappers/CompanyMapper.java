package dev.hope.vulcaobackend.Mappers;



import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.Models.CompanyModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyMapper {

    public CompanyDTO transformData(CompanyModel company) {
        CompanyDTO dto = new CompanyDTO();

        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setCategory(company.getCategory());
        dto.setTaxIdNumber(company.getTaxIdNumber());
        dto.setDescription(company.getDescription());
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setFaxNumber(company.getFaxNumber());
        dto.setEmail(company.getEmail());
        dto.setAddress(company.getAddress());
        dto.setStatus(company.getStatus());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        dto.setCreatorId(company.getCreatorId());
        dto.setRevenue(company.getRevenue());
        dto.setImgS3UrlKeys(company.getImgS3UrlKeys());
        dto.setOrderList(company.getOrderList());
        dto.setWeeklySchedule(company.getWeeklySchedule());
        return dto;
    }
}
