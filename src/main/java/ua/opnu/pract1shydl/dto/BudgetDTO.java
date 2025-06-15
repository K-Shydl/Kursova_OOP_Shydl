package ua.opnu.pract1shydl.dto;

import lombok.Data;

@Data
public class BudgetDTO {
  private double totalAmount;
  private String month;
  private Long userId;
}