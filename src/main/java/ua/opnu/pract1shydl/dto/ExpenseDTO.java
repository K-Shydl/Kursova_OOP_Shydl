package ua.opnu.pract1shydl.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseDTO {
  private double amount;
  private String description;
  private LocalDate date;
  private Long userId;
  private Long categoryId;
}