package org.personalDev;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fixture {

    @Id
    private UUID id;

    @Column
    private LocalDateTime date;

    @Column
    private String homeTeam;
    @Column
    private int homeGoals;
    @Column
    private String awayTeam;
    @Column
    private int awayGoals;
    @Column
    private int matchDay;

    @Column
    private String seriesName;

    public List<String> printToList() {
        List<String> data = new ArrayList<>();

        data.add(this.matchDay+"");
        data.add(this.date.toString());
        data.add(this.homeTeam);
        if(this.date.isBefore(LocalDateTime.now())) {
            data.add(this.homeGoals+"");
            data.add(this.awayGoals+"");
        } else {
            data.add("");
            data.add("");
        }
        data.add(this.awayTeam);

        return data;
    }
}
