package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.clients.RwcClient;
import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.services.RwcService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class RwcServiceImpl implements RwcService {

    private final RwcClient rwcClient;

    @Override
    public List<PoolDTO> getResults() {
        List<TeamResponseDTO> teamResponseDTOList = rwcClient.getAllTeams();


        List<MatchResponseDTO> matchResponseDTOList = rwcClient.getAllMatches();


        Map<String, List<TeamResponseDTO>> teamsByPools = new HashMap<>();

        for (TeamResponseDTO team : teamResponseDTOList) {



            if (!teamsByPools.containsKey(team.getPool())) {
                teamsByPools.put(team.getPool(), new ArrayList<>());
            }
                teamsByPools.get(team.getPool()).add(team);

        }


        List<PoolDTO> poolDTOList = new ArrayList<>();

        for (String pool : teamsByPools.keySet()) {

            List<TeamResponseDTO> teamsInPool = teamsByPools.get(pool);

            PoolDTO poolDTO = new PoolDTO();
            poolDTO.setPoolId(pool);


            for (TeamResponseDTO team : teamsInPool) {

                TeamDTO teamDTO = calculateTeamRsults(team, matchResponseDTOList);

                poolDTO.getTeams().add(teamDTO);

            }

            poolDTOList.add(poolDTO);

        }


        return poolDTOList;


    }

    private TeamDTO calculateTeamRsults(TeamResponseDTO team, List<MatchResponseDTO> matchResponseDTOList) {

        TeamDTO teamDTO = new TeamDTO();

        teamDTO.setTeamName(team.getName());
        teamDTO.setCountry(team.getCountry());
        teamDTO.setTeamId(team.getId());


        for (MatchResponseDTO match : matchResponseDTOList) {
            for (MatchTeamResponseDTO currentMatchTeamResponseDTO : match.getTeams()) {

                if (currentMatchTeamResponseDTO.getId().equals(team.getId())) {


                    MatchTeamResponseDTO opponentTeam;

                    if(match.getTeams().get(0).getId().equals(team.getId())){
                        opponentTeam = match.getTeams().get(1);
                    }else{
                        opponentTeam = match.getTeams().get(0);
                    }


                    teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                    teamDTO.setPointsFor(teamDTO.getPointsFor() + currentMatchTeamResponseDTO.getPoints());
                    teamDTO.setPointsAgainst(teamDTO.getPointsAgainst() + opponentTeam.getPoints());
                    teamDTO.setTriesMade(teamDTO.getTriesMade()+currentMatchTeamResponseDTO.getTries());
                    teamDTO.setPointsDifferential(teamDTO.getPointsDifferential()+currentMatchTeamResponseDTO.getPoints()-opponentTeam.getPoints());

                    teamDTO.setTotalRedCards(teamDTO.getTotalRedCards()+ currentMatchTeamResponseDTO.getRedCards());
                    teamDTO.setTotalYellowCards(teamDTO.getTotalYellowCards()+ currentMatchTeamResponseDTO.getYellowCards());





                    if(opponentTeam.getPoints()> currentMatchTeamResponseDTO.getPoints()){
                        teamDTO.setLoses(teamDTO.getLoses()+1);
                        if(opponentTeam.getPoints()-currentMatchTeamResponseDTO.getPoints()<=7){
//                            teamDTO.setPoints(teamDTO.getPoints()+1);
                            teamDTO.setBonusPoints(teamDTO.getBonusPoints()+1);


                        }

                    } else if (opponentTeam.getPoints() < currentMatchTeamResponseDTO.getPoints()) {
                        teamDTO.setWins(teamDTO.getWins()+1);
                        teamDTO.setPoints(teamDTO.getPoints()+4);


                    }else{
                        teamDTO.setDraws(teamDTO.getDraws()+1);
                        teamDTO.setPoints(teamDTO.getPoints()+2);

                    }

                    if(currentMatchTeamResponseDTO.getTries()>=4){
//                        teamDTO.setPoints(teamDTO.getPoints()+1);
                        teamDTO.setBonusPoints(teamDTO.getBonusPoints()+1);

                    }

                }
            }

        }
        return teamDTO;
    }

    @Override
    public PoolDTO getOneResult(String pool) {
        return null;
    }
}
