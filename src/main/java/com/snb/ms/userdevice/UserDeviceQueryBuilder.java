package com.snb.ms.userdevice;

import com.snb.ms.shared.constants.ListQueryDefaults;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public final class UserDeviceQueryBuilder {

  private static final String DEFAULT_SORT_DIRECTION = "ASC";

  public static final Map<String, String> ALLOWED_SORTS =
      Map.of(
          "userDeviceId", "userDeviceId",
          "userId", "user.userId",
          "deviceId", "deviceId",
          "deviceType", "deviceType",
          "status", "status",
          "registeredAt", "registeredAt",
          "lastActiveAt", "lastActiveAt",
          "lastLoginAt", "lastLoginAt",
          "createdAt", "createdAt",
          "updatedAt", "updatedAt");

  private UserDeviceQueryBuilder() {}

  public static Pageable buildPageable(
      Integer page, Integer size, String sortBy, String sortDirection) {
    int effectivePage =
        page == null
            ? ListQueryDefaults.DEFAULT_PAGE
            : Math.max(page, ListQueryDefaults.DEFAULT_PAGE);
    int requestedSize = size == null ? ListQueryDefaults.DEFAULT_SIZE : size;
    int effectiveSize = Math.min(Math.max(requestedSize, 1), ListQueryDefaults.MAX_PAGE_SIZE);
    return PageRequest.of(effectivePage, effectiveSize, buildSort(sortBy, sortDirection));
  }

  public static Sort buildSort(String sortBy, String sortDirection) {
    List<Sort.Order> orders = toSortOrders(sortBy, sortDirection);
    if (orders.isEmpty()) {
      return Sort.by(Sort.Order.desc("createdAt"));
    }
    return Sort.by(orders);
  }

  public static Specification<UserDevice> buildSpecification() {
    return (root, query, cb) -> cb.equal(root.get("deletedFlag"), "N");
  }

  private static List<Sort.Order> toSortOrders(String sortBy, String sortDirection) {
    List<String> fields = splitCsv(sortBy);
    List<String> directions = splitCsv(sortDirection);
    boolean singleDirection = directions.size() == 1;

    List<Sort.Order> orders = new ArrayList<>();
    for (int i = 0; i < fields.size(); i++) {
      String mappedField = ALLOWED_SORTS.get(fields.get(i));
      if (mappedField == null) {
        throw new IllegalArgumentException("Invalid sort field: " + fields.get(i));
      }
      String direction =
          directions.isEmpty()
              ? DEFAULT_SORT_DIRECTION
              : (singleDirection ? directions.get(0) : directions.get(i));
      Sort.Direction sortDirectionEnum =
          "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
      orders.add(new Sort.Order(sortDirectionEnum, mappedField));
    }
    return orders;
  }

  private static List<String> splitCsv(String value) {
    if (value == null || value.isBlank()) {
      return List.of();
    }
    return List.of(value.split(","));
  }
}
