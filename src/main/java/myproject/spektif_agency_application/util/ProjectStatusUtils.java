package myproject.spektif_agency_application.util;

import myproject.spektif_agency_application.model.ProjectStatus;

public class ProjectStatusUtils {

    public static ProjectStatus parse(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return switch (input.trim().toUpperCase()) {
            case "ON_PROGRESS" -> ProjectStatus.ON_PROGRESS;
            case "COMPLETED" -> ProjectStatus.COMPLETED;
            case "BLOCKED" -> ProjectStatus.BLOCKED;
            default -> throw new IllegalArgumentException("Unknown status: " + input);
        };
    }
}
