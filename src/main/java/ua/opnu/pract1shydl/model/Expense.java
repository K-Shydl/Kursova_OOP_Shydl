package ua.opnu.pract1shydl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private double amount;
  private String description;
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}