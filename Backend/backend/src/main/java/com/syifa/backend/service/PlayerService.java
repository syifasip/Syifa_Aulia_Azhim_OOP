package com.syifa.backend.service;

import com.syifa.backend.model.Player;
import com.syifa.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Create a new player
     * @param player the player to create
     * @return the created player
     * @throws RuntimeException if username already exists
     */
    public Player createPlayer(Player player) {
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new RuntimeException("Username already exists: " + player.getUsername());
        }
        return playerRepository.save(player);
    }

    /**
     * Get player by ID
     */
    public Optional<Player> getPlayerById(UUID playerId) {
        return playerRepository.findById(playerId);
    }

    /**
     * Get player by username
     */
    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    /**
     * Get all players
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Update player profile
     */
    public Player updatePlayer(UUID playerId, Player updatedPlayer) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // Update username
        if (updatedPlayer.getUsername() != null &&
                !updatedPlayer.getUsername().equals(existingPlayer.getUsername())) {

            if (playerRepository.existsByUsername(updatedPlayer.getUsername())) {
                throw new RuntimeException("Username already exists: " + updatedPlayer.getUsername());
            }
            existingPlayer.setUsername(updatedPlayer.getUsername());
        }

        // Update high score
        if (updatedPlayer.getHighScore() != null) {
            existingPlayer.setHighScore(updatedPlayer.getHighScore());
        }

        // Update coins
        if (updatedPlayer.getTotalCoins() != null) {
            existingPlayer.setTotalCoins(updatedPlayer.getTotalCoins());
        }

        // Update distance
        if (updatedPlayer.getTotalDistance() != null) {
            existingPlayer.setTotalDistance(updatedPlayer.getTotalDistance());
        }

        return playerRepository.save(existingPlayer);
    }

    /**
     * Delete player by ID
     */
    public void deletePlayer(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }

    /**
     * Update player statistics (nullable param)
     */
    public Player updatePlayerStats(UUID playerId, Integer scoreValue, Integer coinsCollected, Integer distanceTravelled) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        if (scoreValue != null) {
            player.updateHighScore(scoreValue);
        }
        if (coinsCollected != null) {
            player.addCoins(coinsCollected);
        }
        if (distanceTravelled != null) {
            player.addDistance(distanceTravelled);
        }

        return playerRepository.save(player);
    }

    /**
     * Leaderboards
     */
    public List<Player> getLeaderboardByHighScore(int limit) {
        return playerRepository.findTOpPlayersByHighScore(limit);
    }

    public List<Player> getLeaderboardByTotalCoins() {
        return playerRepository.findAllByOrderByTotalCoinsDesc();
    }

    public List<Player> getLeaderboardByTotalDistance() {
        return playerRepository.findAllByOrderByTotalDistanceDesc();
    }

    /**
     * Check if username exists
     */
    public boolean isUsernameExists(String username) {
        return playerRepository.existsByUsername(username);
    }
}
