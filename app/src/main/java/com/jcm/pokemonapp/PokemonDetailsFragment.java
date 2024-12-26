package com.jcm.pokemonapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PokemonDetailsFragment extends Fragment {

    // Elementos de la vista
    private ImageView pokemonImageView;
    private TextView pokemonNameTextView;
    private TextView pokedexIndexTextView;
    private TextView pokemonTypesTextView;
    private TextView pokemonHeightTextView;
    private TextView pokemonWeightTextView;

    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_details, container, false);

        // Inicializar las vistas
        pokemonImageView = view.findViewById(R.id.pokemonImageView);
        pokemonNameTextView = view.findViewById(R.id.pokemonNameTextView);
        pokedexIndexTextView = view.findViewById(R.id.pokedexIndexTextView);
        pokemonTypesTextView = view.findViewById(R.id.pokemonTypesTextView);
        pokemonHeightTextView = view.findViewById(R.id.alturaPokemonTextView);
        pokemonWeightTextView = view.findViewById(R.id.pesoPokemonTextView);
        backButton = view.findViewById(R.id.backButton);  // Obtener el botón

        // Configurar el listener para el botón "Atrás"
        backButton.setOnClickListener(v -> {
            // Volver al fragmento anterior
            // Usar NavController para navegar
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.pokemonsCapturadosFragment);
        });

        // Recuperar los datos del Bundle
        Bundle args = getArguments();
        if (args != null) {
            String pokemonName = args.getString("pokemonName").toUpperCase();
            String pokedexIndex = args.getString("pokedexIndex");
            String pokemonHeight = args.getString("pokemonHeight");
            String pokemonWeight = args.getString("pokemonWeight");
            ArrayList<String> pokemonTypes = args.getStringArrayList("pokemonType");

            // Actualizar las vistas con los datos
            pokemonNameTextView.setText(pokemonName);
            pokedexIndexTextView.setText(getString(R.string.indicePokedex) + " " + pokedexIndex);
            pokemonHeightTextView.setText(getString(R.string.alturaPokemon) + " " + pokemonHeight);
            pokemonWeightTextView.setText(getString(R.string.pesoPokemon) + " " + pokemonWeight);


            // Mostrar los tipos del Pokémon
            if (pokemonTypes != null && !pokemonTypes.isEmpty()) {
                String types = getString(R.string.tiposPokemon) + String.join(", ", pokemonTypes);
                pokemonTypesTextView.setText(types);
            }

            // Cargar la imagen del Pokémon (suponiendo que tengas una URL para la imagen)
            String imageUrl = args.getString("imageUrl"); // Asegúrate de pasar esta URL al fragmento
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(pokemonImageView);
            }
        }

        return view;
    }
}
