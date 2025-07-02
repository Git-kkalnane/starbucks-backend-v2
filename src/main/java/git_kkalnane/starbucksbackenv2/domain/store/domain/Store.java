package git_kkalnane.starbucksbackenv2.domain.store.domain;

import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import git_kkalnane.starbucksbackenv2.domain.merchant.domain.Merchant;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.Notification;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stores")
public class Store extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @OneToMany(mappedBy = "store")
    private java.util.List<Order> orders;

    @OneToMany(mappedBy = "store")
    private java.util.List<Inquiry> inquiries;

    @OneToMany(mappedBy = "store")
    private java.util.List<Notification> notifications;

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
