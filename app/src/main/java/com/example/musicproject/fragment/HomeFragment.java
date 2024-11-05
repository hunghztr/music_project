package com.example.musicproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicproject.Album;
import com.example.musicproject.Animation;
import com.example.musicproject.DatabaseHelper;
import com.example.musicproject.MusicActivity;
import com.example.musicproject.R;
import com.example.musicproject.Song;
import com.example.musicproject.SongAdaper;
import com.example.musicproject.SubActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;


public class HomeFragment extends Fragment {
    DatabaseHelper helper;
    ArrayList<Song> songs;
    ListView lvSongs;
    SongAdaper adaper;
    Button btnHot1, btnHot2 , btnHot3;
    ArrayList<Album> albums;
    TextView lbAlbumName1 , lbAlbumName2 , lbAlbumName3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        helper = new DatabaseHelper(getContext());
        albums = helper.getAllAlbums();
        lvSongs = view.findViewById(R.id.lvSongs);
        songs = new ArrayList<>();
            songs = helper.getAllSongs();
            adaper = new SongAdaper(getActivity(),R.layout.song_item,songs,true);
            lvSongs.setAdapter(adaper);

        btnHot1 = view.findViewById(R.id.btnHot1);
        btnHot2 = view.findViewById(R.id.btnHot2);
        btnHot3 = view.findViewById(R.id.btnHot3);
        lbAlbumName1 = view.findViewById(R.id.lbAlbumName1);
        lbAlbumName2 = view.findViewById(R.id.lbAlbumName2);
        lbAlbumName3 = view.findViewById(R.id.lbAlbumName3);
        lbAlbumName1.setText(albums.get(0).getName());
        lbAlbumName2.setText(albums.get(1).getName());
        lbAlbumName3.setText(albums.get(2).getName());
        btnHot1.setBackgroundResource(albums.get(0).getId());
        btnHot1.setTag(albums.get(0).getId());
        btnHot2.setBackgroundResource(albums.get(1).getId());
        btnHot2.setTag(albums.get(1).getId());
        btnHot3.setBackgroundResource(albums.get(2).getId());
        btnHot3.setTag(albums.get(2).getId());
//        ArrayList<Song> list = helper.getAllSongsByAlbum(R.drawable.charlie_puth);
//        for(Song i : list){
//            Log.d("TAG", "onCreateView: "+i.getSongName());
//        }

        btnHot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                ArrayList<Song> songs1 = helper.getAllSongsByAlbum((int)view.getTag());
                Song song = songs1.get(0);
                Intent intent = new Intent(getActivity(), SubActivity.class);
                intent.putExtra("song",song);
                intent.putExtra("songs",songs1);
                startActivity(intent);
            }
        });
        btnHot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                ArrayList<Song> songs1 = helper.getAllSongsByAlbum((int)view.getTag());
                Song song = songs1.get(0);
                Intent intent = new Intent(getActivity(),SubActivity.class);
                intent.putExtra("song",song);
                intent.putExtra("songs",songs1);
                startActivity(intent);
            }
        });
        btnHot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                ArrayList<Song> songs1 = helper.getAllSongsByAlbum((int)view.getTag());
                Song song = songs1.get(0);
                Intent intent = new Intent(getActivity(),SubActivity.class);
                intent.putExtra("song",song);
                intent.putExtra("songs",songs1);
                startActivity(intent);
            }
        });

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Animation.setAnimation(view);
                Song song = songs.get(i);
                Intent intent = new Intent(getActivity(),SubActivity.class);
                intent.putExtra("song",song);
                intent.putExtra("songs",songs);
                startActivity(intent);
            }
        });
        return view;
    }

}