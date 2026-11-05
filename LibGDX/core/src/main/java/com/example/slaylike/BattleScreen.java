package com.example.slaylike;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

public class BattleScreen implements Screen {

    private Texture background;
    private Texture cardFace;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    private final Deck deck;
    private final Array<Card> hand = new Array<>();
    private int playerHp = 80;
    private int enemyHp = 40;
    private int energy = 3;
    private String statusMessage = "Choose a card";

    public BattleScreen() {
        background = new Texture(Gdx.files.internal("battleBackground.png"));
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.deck = new Deck();
        cardFace = new Texture(Gdx.files.internal("card.jpg"));
        drawNewHand();
    }

    private void drawNewHand() {
        hand.clear();
        hand.addAll(deck.drawCards(4));
        if (hand.size == 0) {
            statusMessage = "No cards left!";
        } else {
            statusMessage = "Choose a card";
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) playCard(0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) playCard(1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) playCard(2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) playCard(3);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) endTurn();

        // Draw the background first.
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw shapes only while ShapeRenderer is active.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(0.8f, 0.8f, 1f, 1f));
        shapeRenderer.rect(120, 350, 180, 220);

        shapeRenderer.setColor(new Color(1f, 0.6f, 0.6f, 1f));
        shapeRenderer.rect(500, 350, 180, 220);

        shapeRenderer.end();

        // Draw text and cards after the shapes.
        batch.begin();

        font.draw(batch, "Player HP: " + playerHp, 120, 300);
        font.draw(batch, "Enemy HP: " + enemyHp, 500, 300);
        font.draw(batch, "Energy: " + energy, 120, 260);
        font.draw(batch, statusMessage, 120, 230);

        int x = 80;
        int y = 40;

        for (int i = 0; i < hand.size; i++) {
            Card card = hand.get(i);

            batch.draw(cardFace, x, y, 120, 150);
            font.draw(batch, card.name, x + 20, y + 120);
            font.draw(batch, "Cost: " + card.cost, x + 20, y + 70);
            font.draw(batch, "Key: " + (i + 1), x + 20, y + 45);

            x += 140;
        }

        batch.end();
    }

    private void playCard(int index) {
        if (index < 0 || index >= hand.size) return;

        Card card = hand.get(index);

        if (energy <= 0) {
            statusMessage = "No energy! Press SPACE to end turn.";
            return;
        }

        if (card.cost > energy) {
            statusMessage = "Not enough energy for " + card.name + ".";
            return;
        }

        energy -= card.cost;
        statusMessage = "Played " + card.name;

        if (card.damage > 0) {
            enemyHp -= card.damage;
        }

        hand.removeIndex(index);
        deck.addCard(card);

        if (enemyHp <= 0) {
            Card newCard = CardFactory.createRandomRewardCard();
            deck.addCard(newCard);

            enemyHp = 50;
            playerHp += 5;
            energy = 3;
            hand.clear();

            statusMessage = "Reward! Added " + newCard.name + " to your deck.";
            drawNewHand();
            return;
        }

        if (energy <= 0) {
            statusMessage = "Out of energy! Press SPACE to end turn.";
        }
    }

    private void endTurn() {
        if (enemyHp <= 0) {
            return;
        }

        deck.discardHand(hand);
        playerHp -= 5;
        energy = 3;
        statusMessage = "Turn ended. Energy restored.";

        if (playerHp <= 0) {
            statusMessage = "You died! Restart the game.";
            playerHp = 80;
            enemyHp = 45;
        }

        drawNewHand();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void show() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        cardFace.dispose();
    }
}
