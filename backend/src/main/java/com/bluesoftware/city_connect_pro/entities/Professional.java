package com.bluesoftware.city_connect_pro.entities;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "reviews" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "professionals", indexes = {
                @Index(name = "idx_professional_city", columnList = "city"),
                @Index(name = "idx_professional_specialty", columnList = "specialty"),
                @Index(name = "idx_professional_active", columnList = "active")
})
public class Professional {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        @NotBlank(message = "First name cannot be empty")
        @Size(max = 50)
        private String firstName;

        @NotBlank(message = "Last name cannot be empty")
        @Size(max = 50)
        private String lastName;

        @Size(max = 15)
        private String phone;

        @Column(nullable = false, unique = true)
        @NotBlank
        private String dni;

        @NotBlank
        @Email
        @Column(nullable = false, unique = true)
        private String email;

        @ElementCollection
        @CollectionTable(name = "professional_skills", joinColumns = @JoinColumn(name = "professional_id"))
        @Column(name = "skill", length = 100)
        private List<String> skills;

        @ElementCollection
        @CollectionTable(name = "professional_certifications", joinColumns = @JoinColumn(name = "professional_id"))
        @Column(name = "certification", length = 150)
        private List<String> certifications;

        private String city;

        private String address;

        /** Coordenadas para mapa (Google Maps en el frontend). */
        @Column(nullable = false)
        private Double latitude;

        @Column(nullable = false)
        private Double longitude;

        // ⚠️ A futuro conviene modelarlo como entidad o JSON
        private String availability;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        @Column(nullable = false)
        private BigDecimal hourlyRate;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private CurrencyType currency;

        @Min(0)
        @Max(50)
        private int yearsOfExperience;

        @Column(columnDefinition = "TEXT")
        private String experienceDescription;

        @Column(nullable = false)
        private BigDecimal averageRating = BigDecimal.ZERO;

        @Column(nullable = false)
        private int reviewCount = 0;

        @Column(nullable = false)
        private Boolean active = true;

        @Column(nullable = false)
        private Boolean verified = false;

        @Size(max = 500)
        private String profileImage;

        @Size(max = 500)
        private String introVideo;

        @OneToMany(mappedBy = "professional", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        @OrderBy("reviewDate DESC")
        private List<Review> reviews;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Specialty specialty;
}