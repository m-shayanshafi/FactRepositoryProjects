package com.game.entity;

public enum Player {
    A('A'), B('B');

    private final Character value;

    Player(Character value) {
        this.value = value;
    }
    
    public Character getValue(){
    	return this.value;
    }
}
