package com.snb.ms.shared.request;

import com.snb.ms.shared.constants.ListQueryDefaults;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseListQuery {

  @PositiveOrZero private Integer page = ListQueryDefaults.DEFAULT_PAGE;

  @Positive @Max(ListQueryDefaults.MAX_PAGE_SIZE)
  private Integer size = ListQueryDefaults.DEFAULT_SIZE;

  private String sortBy;

  private String sortDirection = "ASC";

  @Positive @Max(ListQueryDefaults.MAX_PAGE_SIZE)
  private Integer limit;

  private String cursor;

  private String select;
}
