package ua.opnu.pract1shydl.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private double totalAmount;
  private String month;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}