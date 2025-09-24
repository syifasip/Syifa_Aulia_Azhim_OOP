package Service;

import Model.Player;
import Repository.PlayerRepository;
import java.util.*;

public abstract class PlayerService {
    private PlayerRepository playerRepository;
    public  PlayerService(PlayerRepository playerRepository){
        this.playerRepository = PlayerRepository;
    }

    public boolean existsByUsername(String username) {
        return playerRepository.existByUsername(username);
    }

    public Player createPlayer(Player player) {
        if (existsByUsername(player.getUsername()))
            throw new
    }

    public getPlayerById(UUID playerId) {
        return playerId;
    }

    public getPlayerByUsername(String username) {
        return
    }

    public getAllPlayers() {
        return
    }

    public updatePlayer(UUID playerId, Player updatedPlayer) {
        return new
    }

    public deletePlayer(UUID playerId) {
        return
    }

    public deletePlayerByUsername(String username) {
        return
    }

    public updatePlayerStats(UUID playerId, int scoreValue, int coinsCollected, int distanceTravelled) {
        return
    }

    public getLeaderboardByHighScore(int limit) {
        return
    }

    public getLeaderboardByTotalCoins() {
        return
    }

    public getLeaderboardByTotalDistance() {

    }
}

