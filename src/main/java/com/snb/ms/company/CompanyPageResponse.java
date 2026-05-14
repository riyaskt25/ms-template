package com.snb.ms.company;

import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paginated company response")
public class CompanyPageResponse extends PaginatedResponseDTO<CompanyResponse> {
}