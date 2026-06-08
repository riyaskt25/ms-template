package com.snb.ms.userdevice;

import com.snb.ms.shared.PaginatedResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated user device response")
public class UserDevicePageResponse extends PaginatedResponseDTO<UserDeviceResponse> {

  public static UserDevicePageResponse fromOffsetPage(Page<UserDeviceResponse> page) {
    PaginatedResponseDTO<UserDeviceResponse> paged = PaginatedResponseDTO.fromPage(page);
    UserDevicePageResponse response = new UserDevicePageResponse();
    response.setData(paged.getData());
    response.setPagination(paged.getPagination());
    return response;
  }
}
