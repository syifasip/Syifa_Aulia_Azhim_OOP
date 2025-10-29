package com.syifa.backend.service;

import com.syifa.backend.model.Score;
import com.syifa.backend.repository.ScoreRepository;
import com.syifa.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Transactional
    public Score createScore(Score score) {
        if (!playerRepository.existsById(score.getPlayerId())) {
            throw new RuntimeException("Player not found with ID: " + score.getPlayerId());
        }
        Score savedScore = scoreRepository.save(score);
        playerService.updatePlayerStats(savedScore.getPlayerId(),
                savedScore.getValue(),
                savedScore.getCoinsCollected(),
                savedScore.getDistanceTravelled());
        return savedScore;
    }

    public Optional<Score>getScoreById(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getScoresByPlayerId(UUID playerId) {
        return scoreRepository.findByPlayerId(playerId);
    }

    public List<Score> getScoresByPlayerIdOrderByValue(UUID playerId) {
        return scoreRepository.findByPlayerIdOrderByValueDesc(playerId);
    }

    public List<Score> getLeaderboard(int limit) {
        return scoreRepository.findTopScores(limit);
    }

    public Optional<Score> getHighestScoreByPlayerId(UUID playerId) {
        List<Score> scores = scoreRepository.findHighestScoreByPlayerId(playerId);
        if (scores.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(scores.get(0));
        }
    }

    public List<Score> getScoresAboveValue(Integer minValue) {
        return scoreRepository.findByValueGreaterThan(minValue);
    }

    public List<Score>  getRecentScores() {
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public Integer getTotalCoinsByPlayerId(UUID playerId) {
        Integer total = scoreRepository.getTotalCoinsByPlayerId(playerId);
        return total != null ? total : 0;
    }

    public Integer  getTotalDistanceByPlayerId(UUID playerId) {
        Integer total = scoreRepository.getTotalDistanceByPlayerId(playerId);
        return total != null ? total : 0;
    }

    public Score  updateScore(UUID scoreId, Score updatedScore) {
        Score existingScore = scoreRepository.findById(scoreId)
                .orElseThrow(); new RuntimeException("Score not found with ID: " + scoreId);

        if (updatedScore.getValue() != null) {
            existingScore.setValue(updatedScore.getValue());
        }
        if (updatedScore.getCoinsCollected() != null) {
            existingScore.setCoinsCollected(updatedScore.getCoinsCollected());
        }
        if (updatedScore.getDistanceTravelled() != null) {
            existingScore.setDistanceTravelled(updatedScore.getDistanceTravelled());
        }
        return scoreRepository.save(existingScore);
    }

    public void deleteScore(UUID scoreId) {
        if (!scoreRepository.existsById(scoreId)) {
            throw new RuntimeException("Score not found with ID: " + scoreId);
        }
        scoreRepository.deleteById(scoreId);
    }

    public void deleteScoresByPlayerId(UUID playerId) {
        List<Score> scores = scoreRepository.findByPlayerId(playerId);
        scoreRepository.deleteAll(scores);
    }
}