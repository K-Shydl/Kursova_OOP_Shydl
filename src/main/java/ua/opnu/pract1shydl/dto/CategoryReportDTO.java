package ua.opnu.pract1shydl.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryReportDTO {
  private String categoryName;
  private BigDecimal totalSpent;
}