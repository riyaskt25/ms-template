package com.snb.ms.rbac.privilege;

import com.snb.ms.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rbac/privileges")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivilegeController implements PrivilegeApi {

  private final PrivilegeService privilegeService;

  @Override
  @GetMapping
  public List<PrivilegeResponse> findAll() {
    log.debug("Received request to fetch all privileges");
    List<PrivilegeResponse> privileges = privilegeService.findAll();
    log.info("Fetched {} privileges", privileges.size());
    return privileges;
  }

  @Override
  @GetMapping("/{privilegeCode}")
  public PrivilegeResponse findByCode(@PathVariable String privilegeCode) {
    log.debug("Received request to fetch privilege by code={}", privilegeCode);
    PrivilegeResponse result =
        privilegeService
            .findByCode(privilegeCode)
            .orElseThrow(() -> ResourceNotFoundException.privilegeByCode(privilegeCode));
    log.info("Privilege found for code={}", privilegeCode);
    return result;
  }
}
