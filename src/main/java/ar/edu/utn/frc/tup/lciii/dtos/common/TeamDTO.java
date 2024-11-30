package ar.edu.utn.frc.tup.lciii.dtos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {

    @JsonProperty("team_id")
    private Integer teamId;

    @JsonProperty("team_name")
    private String teamName;

    private String country;

    @JsonProperty("matches_played")
    private Integer matchesPlayed = 0;

    private Integer wins = 0;

    private Integer draws = 0;

    private Integer loses = 0;

    @JsonProperty("points_for")
    private Integer pointsFor = 0;

    @JsonProperty("points_against")
    private Integer pointsAgainst = 0;

    @JsonProperty("points_differential")
    private Integer pointsDifferential = 0;

    @JsonProperty("tries_made")
    private Integer triesMade = 0;

    @JsonProperty("bonus_points")
    private Integer bonusPoints = 0;

    private Integer points = 0;

    @JsonProperty("total_yellow_cards")
    private Integer totalYellowCards = 0;

    @JsonProperty("total_red_cards")
    private Integer totalRedCards = 0;
}
