package com.tu.libraryManagementSystemBackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "config")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    private String description;
}
