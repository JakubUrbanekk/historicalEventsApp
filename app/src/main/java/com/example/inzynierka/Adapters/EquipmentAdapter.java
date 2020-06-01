package com.example.inzynierka.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.inzynierka.Database.equipment.IEquipment;
import com.example.inzynierka.Database.equipment.IEquipmentRepository;
import com.example.inzynierka.R;
import com.example.inzynierka.equipments.EditEquipmentActivity;
import com.example.inzynierka.equipments.MainEquipmentsActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@AllArgsConstructor
@Log
public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
    Activity activity;
    List <IEquipment> equipmentList;
    IEquipmentRepository repository;
    public static final String BUNDLE_EQUIPMENT_ID = "idequipment";

    @NonNull
    @Override
    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item, parent, false);
        return new EquipmentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentAdapter.ViewHolder holder, int position) {
        IEquipment equipment = equipmentList.get(position);
        holder.category.setText("Rodzaj: " + equipment.getCategory());
        holder.name.setText("Tytuł: " + equipment.getName());
        holder.description.setText("Opis: " + equipment.getDescription());
        log.info("ADAPTER PHOTO URI " + equipment.getPhotoUri());
        if(equipment.getPhotoUri()!=""){
            holder.photo.setVisibility(View.VISIBLE);
        }

        Glide
                .with(activity.getApplicationContext())
                .load(equipment.getPhotoUri())
                .placeholder(R.drawable.noimge)
                .into(holder.photo);

        holder.popupMenu.setOnClickListener(view -> showPopupMenu(view, position));
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    private void showPopupMenu(View view, int position){
        PopupMenu popup = new PopupMenu(activity,view);
        popup.getMenuInflater().inflate(R.menu.equipment_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, view));
        popup.show();
    }

    public void setEquipmentList(List<IEquipment> iEquipments){
        equipmentList = iEquipments;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView category;
        TextView description;
        ImageView photo;
        ImageButton popupMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.equipmentItemNameTextView);
            category = (TextView) itemView.findViewById(R.id.equipmentItemCategoryTextView);
            photo = (ImageView) itemView.findViewById(R.id.equipmentItemPhotoImageView);
            popupMenu = (ImageButton) itemView.findViewById(R.id.equipmentItemPopupMenu);
            description = (TextView) itemView.findViewById(R.id.equipmentItemDescriptionTextView);
        }
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int position;
        private View view;

        public MyMenuItemClickListener(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.editEquipment:
                    Intent intent = new Intent(activity, EditEquipmentActivity.class);
                    long id = equipmentList.get(position).getId();
                    intent.putExtra(BUNDLE_EQUIPMENT_ID, id);
                    putEquipmentExtra(intent);
                    activity.startActivity(intent);
                    return true;

                case R.id.deleteEquipment:
                    new DeleteEquipmentAsyncTASK(equipmentList.get(position), view).execute();
                    return true;
                default:
                    break;
            }
            return false;
        }
    }

    @AllArgsConstructor
    public class DeleteEquipmentAsyncTASK extends AsyncTask<Void, Void, List<IEquipment>> {
        IEquipment equipment;
        View view;

        @Override
        protected List<IEquipment> doInBackground(Void... voids) {
            log.info(("DELETING" + equipment));
            repository.delete(equipment);
            return repository.getAll();
        }

        @Override
        protected void onPostExecute(List<IEquipment> s) {
            setEquipmentList(s);
            Snackbar.make(view, "Usunięto", Snackbar.LENGTH_SHORT).show();
        }
    }
    private int getExtraBundle(){
        String key = MainEquipmentsActivity.BUNDLE_EQUIPMENT_TYPE;
        Intent intent = activity.getIntent();
        int value = intent.getIntExtra(key, -1);
        return value;
    }

    public void putEquipmentExtra(Intent intent){
        intent.putExtra(MainEquipmentsActivity.BUNDLE_EQUIPMENT_TYPE, getExtraBundle());
    }
}
