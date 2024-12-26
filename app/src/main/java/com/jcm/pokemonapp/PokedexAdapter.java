package com.jcm.pokemonapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokemonViewHolder> {

    private final List<PokemonSimple> pokemonList;
    private final Context context;

    // Constructor del adaptador
    public PokedexAdapter(List<PokemonSimple> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonSimple pokemonItem = pokemonList.get(position);
        holder.nameTextView.setText(pokemonItem.getName());

        // Usar Glide para cargar la imagen desde la URL
        Glide.with(context)
                .load(pokemonItem.getImageUrl())
                .into(holder.pokemonImageView);

        // Asegúrate de que el usuario esté autenticado antes de guardar
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Obtén el ID del usuario autenticado

            holder.itemView.setOnClickListener(v -> {
                // Crear un mapa con los datos del Pokémon
                Map<String, Object> pokemonData = new HashMap<>();
                pokemonData.put("createdAt", Timestamp.now());
                pokemonData.put("name", pokemonItem.getName());
                pokemonData.put("pokedexIndex", pokemonItem.getPokedexIndex());
                pokemonData.put("imageUrl", pokemonItem.getImageUrl());
                pokemonData.put("types", pokemonItem.getTypes());
                pokemonData.put("weight", pokemonItem.getWeight());
                pokemonData.put("height", pokemonItem.getHeight());

                // Guardar en la subcolección "pokemons" del usuario
                db.collection("users")
                        .document(userId) // Documento único para el usuario
                        .collection("pokemons_capturados") // Subcolección "pokemons_capturados"
                        .add(pokemonData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(context, R.string.exitoAlGuardar, Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, R.string.errirAlGuardar + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error al guardar el Pokémon", e);
                        });
            });
        } else {
            Toast.makeText(context, R.string.usuarioNoAuth, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // Inner class del ViewHolder como asi recomienda las buenas practicas.
    // El ViewHolder maneja cada vista individual del RecyclerView (la vista de cada pokemon en este caso)
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView pokemonImageView;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonNameTextView);
            pokemonImageView = itemView.findViewById(R.id.pokemonImageView);
        }
    }

}
