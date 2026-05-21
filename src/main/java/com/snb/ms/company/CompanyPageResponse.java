package com.snb.ms.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated company response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyPageResponse extends PaginatedResponseDTO<CompanyResponse> {

	@Schema(description = "Lazy-loading metadata. Present when cursor mode is used.")
	private LazyLoadingMeta lazyLoading;

	public LazyLoadingMeta getLazyLoading() {
		return lazyLoading;
	}

	public void setLazyLoading(LazyLoadingMeta lazyLoading) {
		this.lazyLoading = lazyLoading;
	}

	public static CompanyPageResponse fromOffsetPage(Page<CompanyResponse> page) {
		PaginatedResponseDTO<CompanyResponse> paged = PaginatedResponseDTO.fromPage(page);
		CompanyPageResponse response = new CompanyPageResponse();
		response.setData(paged.getData());
		response.setPagination(paged.getPagination());
		return response;
	}

	public static CompanyPageResponse fromCursor(List<CompanyResponse> data,
												 Integer limit,
												 boolean hasNext,
												 String nextCursor) {
		CompanyPageResponse response = new CompanyPageResponse();
		response.setData(data);
		response.setLazyLoading(new LazyLoadingMeta(limit, hasNext, nextCursor));
		return response;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "Cursor metadata for lazy loading")
	public static class LazyLoadingMeta {
		@Schema(description = "Requested lazy-loading limit", example = "20")
		private Integer limit;

		@Schema(description = "Whether more items are available", example = "true")
		private boolean hasNext;

		@Schema(description = "Opaque cursor token to request the next slice", example = "eyJjb21wYW55SWQiOiIxMjAifQ")
		private String nextCursor;
	}
}