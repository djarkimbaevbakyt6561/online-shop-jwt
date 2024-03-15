package peaksoft.dto.response;

import lombok.Builder;

@Builder
public record BasketProductResponse(
        ProductResponse data,
        Integer count
) {
}
