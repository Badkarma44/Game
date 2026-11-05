package com.example.slaylike;

public class Card {
    public String name;
    public int cost;
    public int damage;
    public String description;

    public Card(String name, int cost, int damage, String description) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.description = description;
    }
}
