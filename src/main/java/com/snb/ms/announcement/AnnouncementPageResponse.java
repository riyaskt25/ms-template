package com.snb.ms.announcement;

import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated announcement response")
public class AnnouncementPageResponse extends PaginatedResponseDTO<AnnouncementResponse> {

  public static AnnouncementPageResponse fromOffsetPage(Page<AnnouncementResponse> page) {
    PaginatedResponseDTO<AnnouncementResponse> paged = PaginatedResponseDTO.fromPage(page);
    AnnouncementPageResponse response = new AnnouncementPageResponse();
    response.setData(paged.getData());
    response.setPagination(paged.getPagination());
    return response;
  }

  public static AnnouncementPageResponse fromData(List<AnnouncementResponse> data, Page<?> page) {
    AnnouncementPageResponse response = new AnnouncementPageResponse();
    response.setData(data);
    response.setPagination(
        PaginatedResponseDTO.fromPage((Page<AnnouncementResponse>) page).getPagination());
    return response;
  }
}
