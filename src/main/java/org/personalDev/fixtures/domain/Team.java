package org.personalDev.fixtures.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id
    private UUID id;
    private String name;

    @ManyToOne
    private Season season;

    @ManyToMany
    private Set<Athlete> athletes;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, ENDED
    }
}
