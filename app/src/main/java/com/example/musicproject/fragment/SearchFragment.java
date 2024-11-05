package com.example.musicproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicproject.Animation;
import com.example.musicproject.DatabaseHelper;
import com.example.musicproject.MusicActivity;
import com.example.musicproject.R;
import com.example.musicproject.Song;
import com.example.musicproject.SongAdaper;
import com.example.musicproject.SubActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchFragment extends Fragment {
    DatabaseHelper helper;
    EditText edtSearch;
    ListView lv;
    ArrayList<Song> songs;
    SongAdaper adaper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        lv = view.findViewById(R.id.lv);

        edtSearch = view.findViewById(R.id.edtSearch);

            songs = helper.getAllSongs();
            adaper = new SongAdaper(getActivity(),R.layout.song_item,songs,true);
            lv.setAdapter(adaper);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Song> newSong = new ArrayList<>();
                String text = edtSearch.getText().toString();
                String regex = ".*"+text+".*";
                Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
                Matcher matcher;
                for(int j = 0 ; j < songs.size() ; j++){
                    matcher = pattern.matcher(songs.get(j).getSongName()+songs.get(j).getSingerName());
                    if(matcher.matches()){
                        newSong.add(songs.get(j));
                    }
                }
                if(newSong.size() > 0) {
                    SongAdaper newAdaper = new SongAdaper(getActivity(), R.layout.song_item, newSong,true);
                    lv.setAdapter(newAdaper);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Animation.setAnimation(view);
                        Song song = songs.get(i);
                        Intent intent = new Intent(getActivity(), SubActivity.class);
                        intent.putExtra("song",song);
                        intent.putExtra("songs",songs);
                        startActivity(intent);

            }
        });
        return view;
    }

}