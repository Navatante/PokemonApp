package com.jcm.pokemonapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PokemonsCapturadosFragment extends Fragment {
    private RecyclerView recyclerView;
    private PokemonsCapturadosAdapter adapter;
    private final List<PokemonSimple> pokemonsCapturadosList = new ArrayList<>();
    private boolean sePuedeEliminarPokemon;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemons_capturados, container, false);

        // Leer configuración de SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Ajustes", getContext().MODE_PRIVATE);
        sePuedeEliminarPokemon = sharedPreferences.getBoolean("sePuedeEliminarPokemon", true);

        // Cargar los datos de Pokémon capturados
        loadPokemonsCapturados();

        // Configurar el RecyclerView
        setupRecyclerView(view);

        return view;
    }

    // Configura el RecyclerView y su adaptador
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewPokemonsCapturados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crea el adaptador con la lista de Pokémon capturados
        adapter = new PokemonsCapturadosAdapter(pokemonsCapturadosList, getContext());
        recyclerView.setAdapter(adapter);

        // Añadir Swipe to Delete si está habilitado
        if (sePuedeEliminarPokemon) {
            addSwipeToDelete();
        }

        // Configurar clic para navegar a los detalles
        addClickToNavigateToPokemonDetails();
    }

    private void addSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No soportamos movimiento de arrastrar
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                PokemonSimple pokemon = pokemonsCapturadosList.get(position);

                // Eliminar de Firestore
                String userId = currentUser.getUid();
                db.collection("users")
                        .document(userId)
                        .collection("pokemons_capturados")
                        .document(pokemon.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // Eliminar de la lista y notificar al adaptador
                            pokemonsCapturadosList.remove(position);
                            adapter.notifyItemRemoved(position);
                        })
                        .addOnFailureListener(e -> Log.e("FirestoreError", "Error al eliminar Pokémon", e));
            }
        };

        // Asocia el ItemTouchHelper al RecyclerView
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void addClickToNavigateToPokemonDetails() {
        // Configurar clic en los elementos para abrir detalles
        adapter.setOnItemClickListener(pokemon -> {
            Fragment fragment = new PokemonDetailsFragment();

            // Pasar los datos del Pokémon al fragmento de detalles
            Bundle args = new Bundle();
            args.putString("pokemonId", pokemon.getId());
            args.putString("imageUrl", pokemon.getImageUrl());
            args.putString("pokemonName", pokemon.getName());
            args.putString("pokedexIndex", String.valueOf(pokemon.getPokedexIndex()));
            args.putString("pokemonHeight", String.valueOf(pokemon.getHeight()));
            args.putString("pokemonWeight", String.valueOf(pokemon.getWeight()));
            List<String> types = pokemon.getTypes(); // Assuming getTypes() returns a List<String>
            ArrayList<String> typesArrayList = new ArrayList<>(types);
            args.putStringArrayList("pokemonType", typesArrayList);
            fragment.setArguments(args);

            // Usar NavController para navegar
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_pokemonsCapturadosFragment_to_pokemonDetailsFragment, args);
        });
    }

    // Carga la lista de Pokémons capturados desde Firestore
    private void loadPokemonsCapturados() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .collection("pokemons_capturados").orderBy("createdAt")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        pokemonsCapturadosList.clear(); // Limpiar la lista antes de agregar nuevos elementos
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            PokemonSimple pokemon = document.toObject(PokemonSimple.class); // Mapea los datos de Firestore
                            pokemon.setId(document.getId()); // Guarda el ID del documento
                            pokemonsCapturadosList.add(pokemon);
                        }
                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Error al cargar Pokémon", e));
        }
    }
}
