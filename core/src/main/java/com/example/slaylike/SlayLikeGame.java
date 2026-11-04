package com.example.slaylike;

import com.badlogic.gdx.Game;

public class SlayLikeGame extends Game {
    @Override
    public void create() {
        setScreen(new BattleScreen());
    }
}
