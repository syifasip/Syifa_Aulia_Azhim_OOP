package com.syifa.frontend.commands;

import com.syifa.frontend.Player;

public class JetpackCommand implements Command{
    private Player player;
    public JetpackCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (player != null) {
            player.fly();
        }
    }

}
