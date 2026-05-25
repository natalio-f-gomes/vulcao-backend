package dev.hope.vulcaobackend.Mappers;

import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.Common.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO transformData(UserModel user){
        Address address = user.getAddress();
        boolean hasAddress = address != null && (
                address.getCity() != null ||
                        address.getIsland() != null ||
                        address.getZone() != null ||
                        address.getStreet() != null ||
                        address.getPostalCode() != null
        );
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(hasAddress ? address : null)
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
