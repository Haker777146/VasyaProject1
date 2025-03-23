package ru.cuty.vasyaproject;

public class Player {
    String name = "Noname";
    int score;
    int kills;

    public void clone(Player player){
        name = player.name;
        score = player.score;
        kills = player.kills;
    }

    public void clear(){
        name = "Noname";
        score = 0;
        kills = 0;
    }
}
