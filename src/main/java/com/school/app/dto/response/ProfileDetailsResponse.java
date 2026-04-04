package com.school.app.dto.response;

import java.util.List;

public record ProfileDetailsResponse(
        String profile,
        List<String> permisos
) {
}
