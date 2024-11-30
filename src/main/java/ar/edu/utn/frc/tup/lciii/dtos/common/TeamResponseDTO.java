package ar.edu.utn.frc.tup.lciii.dtos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponseDTO {

    private Integer id;
    private String name;
    private String country;
    @JsonProperty("world_ranking")
    private Integer worldRanking;

    private String pool;


}
