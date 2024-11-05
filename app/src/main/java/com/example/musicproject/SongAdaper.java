package com.example.musicproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SongAdaper extends ArrayAdapter<Song> {
    private Activity context;
    private int layoutId;
    private ArrayList<Song> songs;
    private boolean isForHome;

    public SongAdaper( Activity context, int layoutId, ArrayList<Song> songs,boolean isForHome) {
        super(context, layoutId,songs);
        this.context = context;
        this.layoutId = layoutId;
        this.songs = songs;
        this.isForHome = isForHome;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
            Song song = songs.get(position);
            ImageView img = convertView.findViewById(R.id.ivSong);
            TextView songName = convertView.findViewById(R.id.lbSongName);
            TextView singerName = convertView.findViewById(R.id.lbSingerName);
            TextView lbId = convertView.findViewById(R.id.lbId);
            img.setImageResource(song.getImageId());
            songName.setText(song.getSongName());
            singerName.setText(song.getSingerName());
            lbId.setText(song.getFileId() + "");
        if(!isForHome) {
            Button btnRemove = convertView.findViewById(R.id.btnRemove);
            btnRemove.setBackgroundResource(R.drawable.baseline_delete_24);
            btnRemove.setVisibility(View.VISIBLE);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    songs.remove(song);
                    Toast.makeText(getContext(), "đã xóa", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    DatabaseHelper helper = new DatabaseHelper(getContext());
                    helper.deleteFauSong(song.getFileId());
                }
            });
        }
        return convertView;
    }
}
