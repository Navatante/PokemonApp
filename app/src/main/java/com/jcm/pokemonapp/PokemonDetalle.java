package com.jcm.pokemonapp;

import java.util.List;
// TODO ver si puedo eliminarla y apanarme con PokemonSimple
public class PokemonDetalle {
    private int id;
    private String name;
    private int weight; // Peso del Pokémon
    private int height; // Altura del Pokémon
    private Sprite sprites;
    private List<TypeSlot> types; // Tipos del Pokémon

    public static class Sprite {
        private String front_default; // URL de la imagen del Pokémon

        public String getFront_default() {
            return front_default;
        }

        public void setFront_default(String front_default) {
            this.front_default = front_default;
        }
    }

    public static class TypeSlot {
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public static class Type {
            private String name; // Nombre del tipo (ej. "fire", "water")

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Sprite getSprites() {
        return sprites;
    }

    public void setSprites(Sprite sprites) {
        this.sprites = sprites;
    }

    public List<TypeSlot> getTypes() {
        return types;
    }

    public void setTypes(List<TypeSlot> types) {
        this.types = types;
    }
}
