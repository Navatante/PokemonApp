package com.jcm.pokemonapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private final List<PokemonSimple> pokemonList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        setupRecyclerView(view);

        // Cargar los datos de Pokémon
        loadPokemonData();

        return view;
    }

    // Configura el RecyclerView y su adaptador
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewPokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PokedexAdapter(pokemonList, getContext());
        recyclerView.setAdapter(adapter);
    }

    // Carga la lista de Pokémon desde la API
    private void loadPokemonData() {
        String BASE_URL = "https://pokeapi.co/api/v2/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService pokeApiService = retrofit.create(PokeApiService.class);

        pokeApiService.getPokemonList(150, 0).enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PokemonListResponse.PokemonSummary summary : response.body().getResults()) {
                        int id = extractIdFromUrl(summary.getUrl());
                        fetchPokemonDetails(id, pokeApiService);
                    }
                } else {
                    Log.e("API Error", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch Pokémon list: " + t.getMessage());
            }
        });
    }

    // Obtiene detalles de un Pokémon específico por su ID
    private void fetchPokemonDetails(int id, PokeApiService pokeApiService) {
        pokeApiService.getPokemonDetails(id).enqueue(new Callback<PokemonDetalle>() {
            @Override
            public void onResponse(Call<PokemonDetalle> call, Response<PokemonDetalle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonSimple pokemonSimple = mapPokemonDetailsToSimple(response.body());
                    pokemonList.add(pokemonSimple);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PokemonDetalle> call, Throwable t) {
                Log.e("API Error", "Failed to fetch Pokémon details: " + t.getMessage());
            }
        });
    }

    // Mapea los detalles de Pokémon a un objeto simple
    private PokemonSimple mapPokemonDetailsToSimple(PokemonDetalle details) {
        PokemonSimple pokemon = new PokemonSimple();
        pokemon.setName(details.getName());
        pokemon.setImageUrl(details.getSprites().getFront_default());
        pokemon.setPokedexIndex(details.getId());
        pokemon.setWeight(details.getWeight());
        pokemon.setHeight(details.getHeight());

        List<String> types = new ArrayList<>();
        for (PokemonDetalle.TypeSlot typeSlot : details.getTypes()) {
            types.add(typeSlot.getType().getName());
        }
        pokemon.setTypes(types);

        return pokemon;
    }

    // Extrae el ID del Pokémon a partir de su URL
    private int extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[6]);
    }
}
