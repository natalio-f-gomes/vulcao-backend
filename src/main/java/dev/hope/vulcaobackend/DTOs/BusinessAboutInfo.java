package dev.hope.vulcaobackend.DTOs;

import dev.hope.vulcaobackend.Properties.BusinessStatus;
import dev.hope.vulcaobackend.Properties.BusinessType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessAboutInfo {
    private String description;
    private BusinessStatus status;
    private String name;
    private BusinessType type;
}
