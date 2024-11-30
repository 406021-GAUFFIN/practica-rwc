package ar.edu.utn.frc.tup.lciii.clients;

import ar.edu.utn.frc.tup.lciii.dtos.common.MatchResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.MatchTeamResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.TeamResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RwcClient {


    private final RestTemplate restTemplate;

    String baseUrl = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023";

    public List<TeamResponseDTO> getAllTeams(){

            List<TeamResponseDTO> teams = List.of(Objects.requireNonNull(restTemplate.getForEntity(baseUrl+ "/teams", TeamResponseDTO[].class).getBody()));
        return teams;

    }

    public List<MatchResponseDTO> getAllMatches(){

        List<MatchResponseDTO> matches = List.of(Objects.requireNonNull(restTemplate.getForEntity(baseUrl + "/matches", MatchResponseDTO[].class).getBody()));
        return matches;

    }


}