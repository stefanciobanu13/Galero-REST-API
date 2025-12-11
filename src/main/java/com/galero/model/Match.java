package com.galero.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_id", nullable = false)
    @NotNull(message = "Edition cannot be null")
    private Edition edition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('group', 'small_final', 'big_final') DEFAULT 'group'")
    private MatchType matchType;

    @Column(length = 20)
    private String stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id", nullable = false)
    @NotNull(message = "Team 1 cannot be null")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id", nullable = false)
    @NotNull(message = "Team 2 cannot be null")
    private Team team2;

    @Column(name = "team1_score")
    private Integer team1Score;
    
    @Column(name = "team2_score")
    private Integer team2Score;

    public enum MatchType {
        group, small_final, big_final
    }
}
