package dev.hope.vulcaobackend.Authentication;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LoginRequest(@Max(50) @Min(8) String username, @Max(128) @Min(8) String password) {
}
