package com.snb.ms.announcement;

import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.shared.commons.StringParsingUtils;
import com.snb.ms.shared.constants.ListQueryDefaults;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public final class AnnouncementQueryBuilder {

  public static final Map<String, String> ALLOWED_SORTS =
      Map.of(
          "announcementId", "announcementId",
          "title", "title",
          "priority", "priority",
          "type", "type",
          "startDate", "startDate",
          "endDate", "endDate",
          "status", "status",
          "createdAt", "createdAt",
          "updatedAt", "updatedAt");

  private AnnouncementQueryBuilder() {}

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

  public static Specification<Announcement> buildSpecification(
      String title,
      AnnouncementPriority priority,
      AnnouncementType type,
      AnnouncementStatus status,
      String description) {
    Specification<Announcement> specification = activeOnly();
    specification = specification.and(likeField("title", title));
    specification = specification.and(equalField("priority", priority));
    specification = specification.and(equalField("type", type));
    specification = specification.and(equalField("status", status));
    specification = specification.and(likeField("description", description));
    return specification;
  }

  private static Specification<Announcement> activeOnly() {
    return (root, query, cb) -> cb.equal(root.get("deletedFlag"), "N");
  }

  private static Specification<Announcement> likeField(String field, String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    String normalized = "%" + value.trim().toLowerCase() + "%";
    return (root, query, cb) -> cb.like(cb.lower(root.get(field)), normalized);
  }

  private static <T> Specification<Announcement> equalField(String field, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get(field), value);
  }

  private static List<Sort.Order> toSortOrders(String sortBy, String sortDirection) {
    List<String> fields = StringParsingUtils.splitCsv(sortBy);
    List<String> directions = StringParsingUtils.splitCsv(sortDirection);

    boolean singleDirection = directions.size() == 1;
    if (!directions.isEmpty() && !singleDirection && directions.size() != fields.size()) {
      throw BusinessValidationException.invalidSortDirectionCount(fields.size(), directions.size());
    }

    List<Sort.Order> orders = new ArrayList<>();
    for (int i = 0; i < fields.size(); i++) {
      String requestedField = fields.get(i);
      String mappedField = ALLOWED_SORTS.get(requestedField);
      if (mappedField == null) {
        throw BusinessValidationException.invalidSortField(requestedField);
      }

      String requestedDirection =
          directions.isEmpty() ? "ASC" : (singleDirection ? directions.get(0) : directions.get(i));
      if (!"ASC".equalsIgnoreCase(requestedDirection)
          && !"DESC".equalsIgnoreCase(requestedDirection)) {
        throw BusinessValidationException.invalidSortDirectionValue(requestedDirection);
      }

      Sort.Direction direction =
          "DESC".equalsIgnoreCase(requestedDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
      orders.add(new Sort.Order(direction, mappedField));
    }
    return orders;
  }
}
