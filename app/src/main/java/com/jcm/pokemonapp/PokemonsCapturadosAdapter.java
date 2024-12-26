package com.jcm.pokemonapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PokemonsCapturadosAdapter extends RecyclerView.Adapter<PokemonsCapturadosAdapter.PokemonCapturadoViewHolder> {
    private List<PokemonSimple> pokemonList;
    private Context context;
    private OnItemClickListener onItemClickListener; // Listener para los clics en los elementos

    public PokemonsCapturadosAdapter(List<PokemonSimple> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
    }

    // Interfaz para manejar clics
    public interface OnItemClickListener {
        void onItemClick(PokemonSimple pokemon);
    }

    // Método para establecer el listener desde el fragmento
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public PokemonCapturadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonCapturadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonCapturadoViewHolder holder, int position) {
        PokemonSimple pokemon = pokemonList.get(position);

        holder.nameTextView.setText(pokemon.getName());
        Glide.with(context).load(pokemon.getImageUrl()).into(holder.pokemonImageView);

        // Configurar el click listener si está definido
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // Inner class del ViewHolder
    public static class PokemonCapturadoViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView pokemonImageView;

        public PokemonCapturadoViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonNameTextView);
            pokemonImageView = itemView.findViewById(R.id.pokemonImageView);
        }
    }
}
