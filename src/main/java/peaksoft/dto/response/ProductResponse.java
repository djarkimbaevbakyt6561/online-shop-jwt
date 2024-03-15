package peaksoft.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String image,
        String name,
        BigDecimal price) {
}