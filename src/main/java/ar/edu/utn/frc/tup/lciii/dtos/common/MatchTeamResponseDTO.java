package ar.edu.utn.frc.tup.lciii.dtos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchTeamResponseDTO {
    private Integer id;

    private Integer points;

    private Integer tries;

    @JsonProperty("yellow_cards")
    private Integer yellowCards;

    @JsonProperty("red_cards")
    private Integer redCards;


}
