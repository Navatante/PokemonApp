package com.jcm.pokemonapp;

import java.util.List;

public class PokemonSimple {
    private String id;
    private String name;
    private String imageUrl;
    private int pokedexIndex;
    private List<String> types;
    private int weight;
    private int height;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPokedexIndex() {
        return pokedexIndex;
    }

    public void setPokedexIndex(int pokedexIndex) {
        this.pokedexIndex = pokedexIndex;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
