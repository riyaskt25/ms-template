package com.snb.ms.tier;

import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated tier response")
public class TierPageResponse extends PaginatedResponseDTO<TierResponse> {

  public static TierPageResponse fromOffsetPage(Page<TierResponse> page) {
    PaginatedResponseDTO<TierResponse> paged = PaginatedResponseDTO.fromPage(page);
    TierPageResponse response = new TierPageResponse();
    response.setData(paged.getData());
    response.setPagination(paged.getPagination());
    return response;
  }
}
