package com.example.cinema.cinemaws.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_studio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Studio extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studioId;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    private String studioName;
    private Integer totalSeats;
}
