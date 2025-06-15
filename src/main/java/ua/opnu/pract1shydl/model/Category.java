package ua.opnu.pract1shydl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String type;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Expense> expenses;
}