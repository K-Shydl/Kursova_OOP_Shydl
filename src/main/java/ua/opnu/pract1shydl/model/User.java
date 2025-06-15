package ua.opnu.pract1shydl.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Expense> expenses;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Income> incomes;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Budget> budgets;
}