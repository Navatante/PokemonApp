package com.jcm.pokemonapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class AjustesFragment extends Fragment {

    private Switch switchIdioma;
    private Switch switchEliminarPokemon;
    private Button btnAcercaDe;
    private Button btnCerrarSesion;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        // Inicializar vistas
        switchIdioma = view.findViewById(R.id.switchIdioma);
        switchEliminarPokemon = view.findViewById(R.id.switchEliminarPokemon);
        btnAcercaDe = view.findViewById(R.id.btnAcercaDe);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("Ajustes", getContext().MODE_PRIVATE);

        // Configurar switch de idioma
        boolean esIngles = sharedPreferences.getString("idioma", "es").equals("en");
        switchIdioma.setChecked(esIngles);
        switchIdioma.setOnCheckedChangeListener((buttonView, isChecked) -> cambiarIdioma(isChecked ? "en" : "es"));

        // Configurar switch de eliminación de Pokémon
        boolean sePuedeEliminarPokemon = sharedPreferences.getBoolean("sePuedeEliminarPokemon", true);
        switchEliminarPokemon.setChecked(sePuedeEliminarPokemon);
        switchEliminarPokemon.setOnCheckedChangeListener((buttonView, isChecked) -> guardarPreferenciaEliminacion(isChecked));

        // Configurar botón "Acerca de"
        btnAcercaDe.setOnClickListener(v -> mostrarDialogoAcercaDe());

        // Configurar botón "Cerrar sesión"
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        return view;
    }

    private void cargarIdioma() {
        String idioma = sharedPreferences.getString("idioma", "es");
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
    }

    private void cambiarIdioma(String idioma) {
        // Guardar idioma en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idioma", idioma);
        editor.apply();

        // Cambiar configuración de idioma
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());

        // Recargar toda la actividad principal
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void guardarPreferenciaEliminacion(boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sePuedeEliminarPokemon", isChecked);
        editor.apply();
    }

    private void mostrarDialogoAcercaDe() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.acercaDeBtn)
                .setMessage(R.string.developer)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        // Navegar a la pantalla de inicio de sesión
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
