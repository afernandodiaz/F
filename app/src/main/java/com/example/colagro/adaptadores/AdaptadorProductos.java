package com.example.colagro.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.colagro.Clases.Producto;
import com.example.colagro.R;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductosViewHolder> {

    ArrayList<Producto> listaProductos;

    public AdaptadorProductos(ArrayList<Producto> listaProductos){
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public AdaptadorProductos.ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_producto,null,false);
        return null;
    }

    @Override
    public void onBindViewHolder(ProductosViewHolder holder, int position) {
        holder.id.setText(listaProductos.get(position).getId());
        holder.nom.setText(listaProductos.get(position).getNombre());
        holder.can.setText(listaProductos.get(position).getCantidad());
        holder.pres.setText(listaProductos.get(position).getPresentacion());
        holder.prec.setText((int) listaProductos.get(position).getPrecio());
        holder.cat.setText(listaProductos.get(position).getCategoria());
        holder.usu.setText(listaProductos.get(position).getUsuario());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public int getItemcount(){
        return listaProductos.size();
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView id, nom, can, img, pres, prec, cat, usu;

        public ProductosViewHolder(View itemView){
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.prod_id);
            nom = (TextView) itemView.findViewById(R.id.prod_nombre);
            can = (TextView) itemView.findViewById(R.id.prod_can);
            img = (TextView) itemView.findViewById(R.id.prod_img);
            pres = (TextView) itemView.findViewById(R.id.prod_prese);
            prec = (TextView) itemView.findViewById(R.id.prod_prec);
            cat = (TextView) itemView.findViewById(R.id.prod_cat);
            usu = (TextView) itemView.findViewById(R.id.prod_usu);
        }
    }
}
