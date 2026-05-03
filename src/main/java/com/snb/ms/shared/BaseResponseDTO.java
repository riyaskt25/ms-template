package com.snb.ms.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO {
    private List<ErrorInfo> errors = new ArrayList<>();
}