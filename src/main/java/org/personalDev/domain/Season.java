package org.personalDev.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.personalDev.domain.domain.Serie;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID seasonId;

    @Column
    private String name;

    @OneToMany
    private List<Serie> series;
}
