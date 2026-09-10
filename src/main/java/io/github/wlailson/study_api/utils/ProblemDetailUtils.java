package io.github.wlailson.study_api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

public final class ProblemDetailUtils {
    private ProblemDetailUtils() {
    }

    public static ProblemDetail create(HttpStatus status, String detail, String errorType) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        URI typeUri = URI.create(baseUrl + "/errors/" + errorType);
        problem.setType(typeUri);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}