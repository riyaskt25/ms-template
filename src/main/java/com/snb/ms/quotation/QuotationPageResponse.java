package com.snb.ms.quotation;

import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated quotation response")
public class QuotationPageResponse extends PaginatedResponseDTO<QuotationResponse> {

  public static QuotationPageResponse fromOffsetPage(Page<QuotationResponse> page) {
    PaginatedResponseDTO<QuotationResponse> paged = PaginatedResponseDTO.fromPage(page);
    QuotationPageResponse response = new QuotationPageResponse();
    response.setData(paged.getData());
    response.setPagination(paged.getPagination());
    return response;
  }
}
