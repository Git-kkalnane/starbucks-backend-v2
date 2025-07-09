package git_kkalnane.starbucksbackenv2.domain.store.domain;

import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import git_kkalnane.starbucksbackenv2.domain.merchant.domain.Merchant;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.Notification;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "stores")
public class Store extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Builder
    public Store(Long id, Merchant merchant, String name, String address, String phone, String openingHours, Boolean hasDriveThrough, Integer seatingCapacity, BigDecimal latitude, BigDecimal longitude, String imageUrl, String crowdLevel) {
        this.id = id;
        this.merchant = merchant;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.openingHours = openingHours;
        this.hasDriveThrough = hasDriveThrough;
        this.seatingCapacity = seatingCapacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.crowdLevel = crowdLevel;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = true)
    private Merchant merchant;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private List<Inquiry> inquiries;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "has_drive_through")
    private Boolean hasDriveThrough = false;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "crowd_level")
    private String crowdLevel;

    // Getters and Setters
}
