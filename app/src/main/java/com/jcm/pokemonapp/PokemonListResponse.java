package com.jcm.pokemonapp;

import java.util.List;

public class PokemonListResponse {
    private List<PokemonSummary> results;

    // Getter
    public List<PokemonSummary> getResults() {
        return results;
    }

    public static class PokemonSummary {
        private String name;
        private String url;

        // Getters
        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
