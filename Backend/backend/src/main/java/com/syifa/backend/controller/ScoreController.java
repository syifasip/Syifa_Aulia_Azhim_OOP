package com.syifa.backend.controller;

import com.syifa.backend.model.Score;
import com.syifa.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")

public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Score newScore = scoreService.createScore(score);
            return ResponseEntity.status(HttpStatus.CREATED).body(newScore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById( @PathVariable UUID scoreId) {
        Optional<Score> score = scoreService.getScoreById(scoreId);
        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score not found with id: " + scoreId);
        }
    }

    @PutMapping("/{scoreId}")
    public ResponseEntity<?> updateScore(@PathVariable UUID scoreId, @RequestBody Score score) {
        try {
            Score updatedScore = scoreService.updateScore(scoreId, score);
            return ResponseEntity.ok(updatedScore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok("Score deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Score>> getScoresByPlayerId(@PathVariable UUID playerId) {
        List<Score> scores = scoreService.getScoresByPlayerId(playerId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/player/{playerId}/ordered")
    public ResponseEntity<List<Score>> getScoresByPlayerIdOrdered(@PathVariable UUID playerId) {
        List<Score> scores = scoreService.getScoresByPlayerIdOrderByValue(playerId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        List<Score> leaderboard = scoreService.getLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/player/{playerId}/highest")
    public ResponseEntity<?> getHighestScoreByPlayerId(@PathVariable UUID playerId) {
        Optional<Score> highestscore = scoreService.getHighestScoreByPlayerId(playerId);
        if (highestscore.isPresent()) {
            return ResponseEntity.ok(highestscore.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No scores found for this player.");
        }
    }

    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable Integer minValue) {
        List<Score> scores = scoreService.getScoresAboveValue(minValue);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores() {
        List<Score> scores = scoreService.getRecentScores();
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/player/{playerId}/total-coins")
    public ResponseEntity<?> getTotalCoinsByPlayerId(@PathVariable UUID playerId) {
        Integer totalCoins = scoreService.getTotalCoinsByPlayerId(playerId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalCoins", totalCoins);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/player/{playerId}/total-distance")
    public ResponseEntity<?> getTotalDistanceByPlayerId(@PathVariable UUID playerId) {
        Integer totalDistance = scoreService.getTotalDistanceByPlayerId(playerId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalDistance", totalDistance);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity<?> deleteScoresByPlayerId(@PathVariable UUID playerId) {
        scoreService.deleteScoresByPlayerId(playerId);
        return ResponseEntity.ok("All scores for player " + playerId + " have been deleted.");
    }

}
