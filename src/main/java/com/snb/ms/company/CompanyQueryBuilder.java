package com.snb.ms.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.shared.commons.StringParsingUtils;
import com.snb.ms.shared.constants.ListQueryDefaults;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Query builder utility for Company entity. Encapsulates pagination, sorting, and filtering logic.
 * Can be reused across services, controllers, or repositories.
 */
public final class CompanyQueryBuilder {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String DEFAULT_SORT_DIRECTION = "ASC";

  private static final Map<String, Class<? extends Comparable<?>>> FIELD_TYPES =
      Map.of(
          "companyId", Long.class,
          "registrationNumber", String.class,
          "companyStatus", String.class,
          "companyType", String.class,
          "user.emailAddress", String.class,
          "user.mobileNumber", String.class,
          "createdAt", LocalDateTime.class,
          "updatedAt", LocalDateTime.class);

  /**
   * Whitelist of sortable fields for Company entities. Maps API field names to JPA entity paths for
   * safe dynamic sorting.
   */
  public static final Map<String, String> ALLOWED_SORTS =
      Map.of(
          "companyId", "companyId",
          "registrationNumber", "registrationNumber",
          "companyStatus", "companyStatus",
          "companyType", "companyType",
          "emailAddress", "user.emailAddress",
          "mobileNumber", "user.mobileNumber",
          "createdAt", "createdAt",
          "updatedAt", "updatedAt");

  private CompanyQueryBuilder() {
    // Utility class
  }

  /** Builds a Pageable with pagination and sorting parameters. */
  public static Pageable buildPageable(
      Integer page, Integer size, String sortBy, String sortDirection) {
    int effectivePage =
        page == null
            ? ListQueryDefaults.DEFAULT_PAGE
            : Math.max(page, ListQueryDefaults.DEFAULT_PAGE);
    int requestedSize = size == null ? ListQueryDefaults.DEFAULT_SIZE : size;
    int effectiveSize = Math.min(Math.max(requestedSize, 1), ListQueryDefaults.MAX_PAGE_SIZE);

    Sort sort = buildSort(sortBy, sortDirection);
    return PageRequest.of(effectivePage, effectiveSize, sort);
  }

  /**
   * Builds a Pageable for cursor-based lazy loading. Supports the same multi-sort inputs as offset
   * mode and appends companyId as tie-breaker when missing.
   */
  public static Pageable buildCursorPageable(Integer limit, String sortBy, String sortDirection) {
    int requestedLimit = limit == null ? ListQueryDefaults.DEFAULT_SIZE : limit;
    int effectiveLimit = Math.min(Math.max(requestedLimit, 1), ListQueryDefaults.MAX_PAGE_SIZE);
    return PageRequest.of(0, effectiveLimit + 1, buildCursorSort(sortBy, sortDirection));
  }

  /**
   * Builds a Sort from comma-separated field and direction strings. Validates fields against
   * ALLOWED_SORTS whitelist.
   */
  public static Sort buildSort(String sortBy, String sortDirection) {
    List<Sort.Order> orders = toSortOrders(sortBy, sortDirection);

    if (orders.isEmpty()) {
      return Sort.by(Sort.Order.asc("companyId"));
    }
    return Sort.by(orders);
  }

  /**
   * Builds sort orders for cursor mode and guarantees deterministic traversal using companyId
   * tie-breaker.
   */
  public static Sort buildCursorSort(String sortBy, String sortDirection) {
    List<Sort.Order> orders = toSortOrders(sortBy, sortDirection);

    if (orders.isEmpty()) {
      orders.add(Sort.Order.asc("companyId"));
      return Sort.by(orders);
    }

    boolean hasCompanyId =
        orders.stream().anyMatch(order -> "companyId".equals(order.getProperty()));
    if (!hasCompanyId) {
      Sort.Direction tieBreakerDirection = orders.get(0).getDirection();
      orders.add(new Sort.Order(tieBreakerDirection, "companyId"));
    }
    return Sort.by(orders);
  }

  /** Builds a Specification for filtering Company entities. */
  public static Specification<Company> buildSpecification(
      String registrationNumber,
      String companyStatus,
      String companyType,
      String emailAddress,
      String mobileNumber) {
    Specification<Company> specification = activeOnly();
    specification = specification.and(likeCompanyField("registrationNumber", registrationNumber));
    specification = specification.and(likeCompanyField("companyStatus", companyStatus));
    specification = specification.and(likeCompanyField("companyType", companyType));
    specification = specification.and(likeUserField("emailAddress", emailAddress));
    specification = specification.and(likeUserField("mobileNumber", mobileNumber));
    return specification;
  }

  /** Adds cursor constraint to an existing specification for lazy loading. */
  public static Specification<Company> withCursor(
      Specification<Company> baseSpec, String cursor, String sortBy, String sortDirection) {
    if (cursor == null || cursor.trim().isEmpty()) {
      return baseSpec;
    }

    Map<String, String> cursorValues = decodeCursor(cursor);
    Sort cursorSort = buildCursorSort(sortBy, sortDirection);

    return baseSpec.and(
        (root, query, cb) -> buildCursorPredicate(root, cb, cursorSort, cursorValues));
  }

  private static Predicate buildCursorPredicate(
      Root<Company> root, CriteriaBuilder cb, Sort cursorSort, Map<String, String> cursorValues) {
    List<Sort.Order> orders = cursorSort.stream().toList();
    List<Predicate> orBlocks = new ArrayList<>();

    for (int i = 0; i < orders.size(); i++) {
      Predicate orBranch = buildCursorOrBranch(root, cb, orders, cursorValues, i);
      if (orBranch == null) {
        return cb.conjunction();
      }
      orBlocks.add(orBranch);
    }

    if (orBlocks.isEmpty()) {
      return cb.conjunction();
    }
    return cb.or(orBlocks.toArray(new Predicate[0]));
  }

  private static Predicate buildCursorOrBranch(
      Root<Company> root,
      CriteriaBuilder cb,
      List<Sort.Order> orders,
      Map<String, String> cursorValues,
      int branchIndex) {
    List<Predicate> andParts = new ArrayList<>();

    for (int j = 0; j < branchIndex; j++) {
      Sort.Order priorOrder = orders.get(j);
      String priorValueRaw = cursorValues.get(priorOrder.getProperty());
      if (priorValueRaw == null) {
        return null;
      }

      Comparable<?> priorValue = parseCursorValue(priorOrder.getProperty(), priorValueRaw);
      Path<? extends Comparable<?>> priorPath =
          resolveComparablePath(root, priorOrder.getProperty());
      andParts.add(cb.equal(priorPath, priorValue));
    }

    Sort.Order currentOrder = orders.get(branchIndex);
    String currentValueRaw = cursorValues.get(currentOrder.getProperty());
    if (currentValueRaw == null) {
      return null;
    }

    Comparable<?> currentValue = parseCursorValue(currentOrder.getProperty(), currentValueRaw);
    Path<? extends Comparable<?>> currentPath =
        resolveComparablePath(root, currentOrder.getProperty());
    Predicate compare =
        buildComparisonPredicate(cb, currentPath, currentValue, currentOrder.isAscending());
    andParts.add(compare);
    return cb.and(andParts.toArray(new Predicate[0]));
  }

  /** Builds the next cursor token from the last entity in the current lazy-loading slice. */
  public static String buildNextCursor(Company lastCompany, String sortBy, String sortDirection) {
    Sort cursorSort = buildCursorSort(sortBy, sortDirection);
    Map<String, String> values = new HashMap<>();

    for (Sort.Order order : cursorSort) {
      Object rawValue = extractFieldValue(lastCompany, order.getProperty());
      values.put(order.getProperty(), rawValue == null ? "" : rawValue.toString());
    }

    try {
      String json = OBJECT_MAPPER.writeValueAsString(values);
      return Base64.getUrlEncoder()
          .withoutPadding()
          .encodeToString(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new IllegalStateException("Failed to encode cursor token", e);
    }
  }

  /** Specification: Only active (not deleted) companies. */
  private static Specification<Company> activeOnly() {
    return (root, query, cb) -> cb.equal(root.get("deletedFlag"), "N");
  }

  /** Specification: Case-insensitive LIKE filter on company fields. */
  private static Specification<Company> likeCompanyField(String fieldName, String value) {
    if (isBlank(value)) {
      return null;
    }
    String normalized = normalizeLikeValue(value);
    return (root, query, cb) -> cb.like(cb.lower(root.get(fieldName)), normalized);
  }

  /** Specification: Case-insensitive LIKE filter on associated user fields (INNER JOIN). */
  private static Specification<Company> likeUserField(String fieldName, String value) {
    if (isBlank(value)) {
      return null;
    }
    String normalized = normalizeLikeValue(value);
    return (root, query, cb) -> {
      Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
      return cb.like(cb.lower(userJoin.get(fieldName)), normalized);
    };
  }

  /** Normalizes a string for LIKE comparison: wraps with % and converts to lowercase. */
  private static String normalizeLikeValue(String value) {
    return "%" + value.trim().toLowerCase() + "%";
  }

  /** Checks if a string is null or empty (after trimming). */
  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
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
          directions.isEmpty()
              ? DEFAULT_SORT_DIRECTION
              : (singleDirection ? directions.get(0) : directions.get(i));

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

  private static Map<String, String> decodeCursor(String cursor) {
    try {
      byte[] decoded = Base64.getUrlDecoder().decode(cursor);
      String json = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
      return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, String>>() {});
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid cursor token", e);
    }
  }

  private static Comparable<?> parseCursorValue(String mappedField, String rawValue) {
    Class<? extends Comparable<?>> type = FIELD_TYPES.get(mappedField);
    if (type == null) {
      throw new IllegalArgumentException("Unsupported cursor field: " + mappedField);
    }
    if (Objects.equals(type, Long.class)) {
      return Long.valueOf(rawValue);
    }
    if (Objects.equals(type, LocalDateTime.class)) {
      try {
        return LocalDateTime.parse(rawValue);
      } catch (DateTimeParseException ex) {
        throw new IllegalArgumentException(
            "Invalid cursor datetime value for field " + mappedField, ex);
      }
    }
    return rawValue;
  }

  @SuppressWarnings("unchecked")
  private static Path<? extends Comparable<?>> resolveComparablePath(
      Root<Company> root, String mappedField) {
    String[] parts = mappedField.split("\\.");
    Path<?> path = root;
    for (String part : parts) {
      path = path.get(part);
    }
    return (Path<? extends Comparable<?>>) path;
  }

  private static Object extractFieldValue(Company company, String mappedField) {
    return switch (mappedField) {
      case "companyId" -> company.getCompanyId();
      case "registrationNumber" -> company.getRegistrationNumber();
      case "companyStatus" -> company.getCompanyStatus();
      case "companyType" -> company.getCompanyType();
      case "createdAt" -> company.getCreatedAt();
      case "updatedAt" -> company.getUpdatedAt();
      case "user.emailAddress" ->
          company.getUser() == null ? null : company.getUser().getEmailAddress();
      case "user.mobileNumber" ->
          company.getUser() == null ? null : company.getUser().getMobileNumber();
      default -> throw new IllegalArgumentException("Unsupported cursor field: " + mappedField);
    };
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static Predicate buildComparisonPredicate(
      jakarta.persistence.criteria.CriteriaBuilder cb,
      Path<? extends Comparable<?>> path,
      Comparable<?> value,
      boolean ascending) {
    Path comparablePath = path;
    Comparable comparableValue = value;
    return ascending
        ? cb.greaterThan(comparablePath, comparableValue)
        : cb.lessThan(comparablePath, comparableValue);
  }
}
