package git_kkalnane.starbucksbackenv2.domain.item.domain.beverage;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "beverage_supported_sizes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BeverageSupportedSize {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 20, nullable = false)
  private String name; // Tall, Grande, Venti 등

  @Column(name = "price", nullable = false)
  private Integer price;

  @Column(name = "volume", nullable = false)
  private Integer volume; // ml 등 단위

  @ManyToMany(mappedBy = "supportedSizes")
  private List<BeverageItem> items;
}
