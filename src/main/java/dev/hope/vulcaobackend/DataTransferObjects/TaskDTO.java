package dev.hope.vulcaobackend.DataTransferObjects;

import dev.hope.vulcaobackend.DataTransferObjects.Common.BaseModelDTO;
import dev.hope.vulcaobackend.Models.Properties.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO extends BaseModelDTO {
    private String title;
    private String description;
    private TaskPriority priority;

    private LocalDate startDate;
    private LocalDate deadline;
    private LocalDate completionDate;

    private Boolean pastDue;

    private String creatorId;
    private String assigneeId;

    private Duration duration;
    private Duration estimatedTime;
}
