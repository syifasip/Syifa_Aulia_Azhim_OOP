package com.syifa.frontend.commands;

import com.syifa.frontend.Player;
import com.syifa.frontend.GameManager;

public class RestartCommand implements Command {
    private Player player;
    private GameManager gameManager;
    public RestartCommand(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    @Override
    public void execute() {
        if (player != null) player.reset();
        if (gameManager != null) gameManager.setScore(0);
    }
}
