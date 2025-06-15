package ua.opnu.pract1shydl.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeDTO {
  private double amount;
  private String source;
  private LocalDate date;
  private Long userId;
}