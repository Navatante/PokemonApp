package com.jcm.pokemonapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar el idioma configurado
        cargarIdioma();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el NavHostFragment y el NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Configurar el BottomNavigationView con el NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Establecer el ítem seleccionado inicialmente en el BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.menu_pokedex);

        // Manejar la navegación entre los fragmentos desde el BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_pokedex) {
                navController.navigate(R.id.pokedexFragment);
                return true;
            } else if (item.getItemId() == R.id.menu_misPokemons) {
                navController.navigate(R.id.pokemonsCapturadosFragment);
                return true;
            } else if (item.getItemId() == R.id.menu_ajustes) {
                navController.navigate(R.id.ajustesFragment);
                return true;
            }
            return false;
        });
    }

    private void cargarIdioma() {
        SharedPreferences sharedPreferences = getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = sharedPreferences.getString("idioma", "es");
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

}
