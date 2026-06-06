package dev.hope.vulcaobackend.Mappers;

import dev.hope.vulcaobackend.DataTransferObjects.TaskDTO;
import dev.hope.vulcaobackend.Models.Properties.TaskPriority;
import dev.hope.vulcaobackend.Models.TaskModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskMapper {
    public TaskDTO transformData(TaskModel taskModel) {
        if (taskModel == null) {
            return null;
        }

        TaskDTO dto = new TaskDTO();

        // Core Metadata & Auditing
        dto.setId(taskModel.getId());
        dto.setCreatedAt(taskModel.getCreatedAt());
        dto.setTitle(taskModel.getTitle());
        dto.setDescription(taskModel.getDescription());
        dto.setPriority(taskModel.getPriority());

        // Chronological Metrics
        dto.setStartDate(taskModel.getStartDate());
        dto.setDeadline(taskModel.getDeadline());
        dto.setCompletionDate(taskModel.getCompletionDate());

        dto.setCreatorId(taskModel.getCreatorId());


        // Execution Intervals
        dto.setDuration(taskModel.getDuration());
        dto.setEstimatedTime(taskModel.getEstimatedTime());

        // Dynamic State Derivation (Past Due Logic)
        if (taskModel.getDeadline() != null && taskModel.getCompletionDate() == null) {
            dto.setPastDue(LocalDate.now().isAfter(taskModel.getDeadline()));
        } else {
            // If there's no deadline, or it was completed, map directly or default to false
            dto.setPastDue(taskModel.getPastDue() != null ? taskModel.getPastDue() : false);
        }

        return dto;
    }

}
