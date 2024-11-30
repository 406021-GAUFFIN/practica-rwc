package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.clients.RwcClient;
import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.services.RwcService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        //Primero obtengo todos los equipos y matches
        List<TeamResponseDTO> teamResponseDTOList = rwcClient.getAllTeams();


        List<MatchResponseDTO> matchResponseDTOList = rwcClient.getAllMatches();

        //Armo un mapa con el id de la pool y la lista de equipos en esa pool
        Map<String, List<TeamResponseDTO>> teamsByPools = new HashMap<>();

        //recorro todos los teams y los agrego a su pool, si todavia no estaba su pool en el mapa, la agrego
        for (TeamResponseDTO team : teamResponseDTOList) {

            if (!teamsByPools.containsKey(team.getPool())) {
                teamsByPools.put(team.getPool(), new ArrayList<>());
            }
                teamsByPools.get(team.getPool()).add(team);

        }

        //Armo el objeto que va a ser la respuesta
        List<PoolDTO> poolDTOList = new ArrayList<>();

        //recorro el mapa de pools a traves de su key que es el pool id (A, B, C,...)
        for (String pool : teamsByPools.keySet()) {

            //Me traigo los teams en ese pool
            List<TeamResponseDTO> teamsInPool = teamsByPools.get(pool);


            //creo el pool que voy a usar en la respuesta final, le asigno su valor
            PoolDTO poolDTO = new PoolDTO();
            poolDTO.setPoolId(pool);


            //a cada uno de los team en el pool que estoy parado, le calculo los resultados y lo sumo al pool que estoy armando
            for (TeamResponseDTO team : teamsInPool) {

                TeamDTO teamDTO = calculateTeamResults(team, matchResponseDTOList);

                poolDTO.getTeams().add(teamDTO);

            }


            //despues de armar el pool y agregarle sus teams con los resultados calculados, lo agrego al resultado final
            poolDTOList.add(poolDTO);

        }


        return poolDTOList;


    }

    private TeamDTO calculateTeamResults(TeamResponseDTO team, List<MatchResponseDTO> matchResponseDTOList) {


        //arranco crando el team dto (que alberga los resultados) y le seteo valores iniciales que no dependen de los partidos
        TeamDTO teamDTO = new TeamDTO();

        teamDTO.setTeamName(team.getName());
        teamDTO.setCountry(team.getCountry());
        teamDTO.setTeamId(team.getId());

        //recorro los matches y luego los equipos en cada match, y si encuentro el equipo con el que estoy trabajando hago todos sus calculos de resultados
        for (MatchResponseDTO match : matchResponseDTOList) {
            for (MatchTeamResponseDTO currentMatchTeamResponseDTO : match.getTeams()) {

                if (currentMatchTeamResponseDTO.getId().equals(team.getId())) {

                    //primero obtengo el oponente, que sería el equipo en la lista que no es con el que estoy trabajando (current)
                    MatchTeamResponseDTO opponentTeam;

                    if(match.getTeams().get(0).getId().equals(team.getId())){
                        opponentTeam = match.getTeams().get(1);
                    }else{
                        opponentTeam = match.getTeams().get(0);
                    }

                    //Actualizo los valores que no dependen de si gano o no ni puntos bonus
                    teamDTO.setMatchesPlayed(teamDTO.getMatchesPlayed() + 1);
                    teamDTO.setPointsFor(teamDTO.getPointsFor() + currentMatchTeamResponseDTO.getPoints());
                    teamDTO.setPointsAgainst(teamDTO.getPointsAgainst() + opponentTeam.getPoints());
                    teamDTO.setTriesMade(teamDTO.getTriesMade()+currentMatchTeamResponseDTO.getTries());
                    teamDTO.setPointsDifferential(teamDTO.getPointsDifferential()+currentMatchTeamResponseDTO.getPoints()-opponentTeam.getPoints());

                    teamDTO.setTotalRedCards(teamDTO.getTotalRedCards()+ currentMatchTeamResponseDTO.getRedCards());
                    teamDTO.setTotalYellowCards(teamDTO.getTotalYellowCards()+ currentMatchTeamResponseDTO.getYellowCards());




                    //Si perdio, le sumamos una pérdida, y si fue por 7 o menos, le sumamos el punto bonus
                    if(opponentTeam.getPoints()> currentMatchTeamResponseDTO.getPoints()){
                        teamDTO.setLoses(teamDTO.getLoses()+1);
                        if(opponentTeam.getPoints()-currentMatchTeamResponseDTO.getPoints()<=7){
//                            teamDTO.setPoints(teamDTO.getPoints()+1);
                            teamDTO.setBonusPoints(teamDTO.getBonusPoints()+1);


                        }
                    //si gano, le sumamos un win y sus 4 puntos por haber ganado
                    } else if (opponentTeam.getPoints() < currentMatchTeamResponseDTO.getPoints()) {
                        teamDTO.setWins(teamDTO.getWins()+1);
                        teamDTO.setPoints(teamDTO.getPoints()+4);

                    //si no, quiere decir que empato, sumamos empates y sumamos 2 puntos por haber empatado
                    }else{
                        teamDTO.setDraws(teamDTO.getDraws()+1);
                        teamDTO.setPoints(teamDTO.getPoints()+2);

                    }
                    //si gano el partido por 4 tries o mas, le sumamos un punto bonus
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
        List<PoolDTO> poolDTOS = this.getResults();

        for(PoolDTO poolDTO: poolDTOS){
            if(poolDTO.getPoolId().equals(pool)){
                return  poolDTO;
            }
        }

        throw new EntityNotFoundException();
    }
}
