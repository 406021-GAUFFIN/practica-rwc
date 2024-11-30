package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchResponseDTO {
    private String pool;
    private List<MatchTeamResponseDTO> teams;

}
