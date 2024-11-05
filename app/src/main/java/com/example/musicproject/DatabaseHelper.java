package com.example.musicproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Music.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng song
    private static final String TABLE_SONG = "song";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMG_ID = "img_id";
    private static final String COLUMN_SONG_NAME = "song_name";
    private static final String COLUMN_SINGER_NAME = "singer_name";

    // Bảng fauvorite song
    private static final String TABLE_FAUVORITE_SONG = "fauvorite_song";
    private static final String COLUMN_FAU_ID = "fau_id";
    private static final String COLUMN_FAU_IMG_ID = "img_id";
    private static final String COLUMN_FAU_SONG_NAME = "fau_song_name";
    private static final String COLUMN_FAU_SINGER_NAME = "fau_singer_name";

    // Bảng album
    private static final String TABLE_ALBUM = "album";
    private static final String COLUMN_ALBUM_ID = "album_id";
    private static final String COLUMN_ALBUM_NAME = "album_name";
    private static final String COLUMN_ALBUM_SINGER_NAME = "album_singer_name";

    // Bảng detail album
    private static final String TABLE_ALBUM_DETAIL = "album_detail";
    private static final String COLUMN_DETAIL_SONG_ID = "detail_song_id";
    private static final String COLUMN_DETAIL_ALBUM_ID = "detail_album_id";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createSongsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SONG + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY , " +
                COLUMN_IMG_ID + " INTEGER, " +
                COLUMN_SONG_NAME + " TEXT," +
                COLUMN_SINGER_NAME + " TEXT)";
        db.execSQL(createSongsTable);

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_FAUVORITE_SONG + " (" +
                COLUMN_FAU_ID + " INTEGER PRIMARY KEY , " +
                COLUMN_FAU_IMG_ID + " INTEGER, " +
                COLUMN_FAU_SONG_NAME + " TEXT," +
                COLUMN_FAU_SINGER_NAME + " TEXT)";
        db.execSQL(createOrdersTable);

        String createAlbumsTable = "CREATE TABLE IF NOT EXISTS "+TABLE_ALBUM +" (" +
                COLUMN_ALBUM_ID +" INTEGER PRIMARY KEY , " +
                COLUMN_ALBUM_NAME + " TEXT ," +
                COLUMN_ALBUM_SINGER_NAME + " TEXT)";
        db.execSQL(createAlbumsTable);

        String createDeTailAlbum = "CREATE TABLE IF NOT EXISTS "+TABLE_ALBUM_DETAIL +" ("+
                COLUMN_DETAIL_SONG_ID +" INTEGER ," +
                COLUMN_DETAIL_ALBUM_ID +" INTEGER , FOREIGN KEY ("+COLUMN_DETAIL_SONG_ID+") REFERENCES " +
                "song("+COLUMN_ID+") , FOREIGN KEY ("+COLUMN_DETAIL_ALBUM_ID+") REFERENCES " +
                "album("+COLUMN_ALBUM_ID+"))";
        db.execSQL(createDeTailAlbum);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ALBUM_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAUVORITE_SONG);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ALBUM);
        onCreate(db);
    }

    public ArrayList<Song> getAllSongsByAlbum(int id){
        ArrayList<Song> songs = new ArrayList<>();
        String query = "SELECT song.id, song.img_id, song.song_name, song.singer_name " +
                "FROM "+TABLE_SONG+" " +
                "INNER JOIN "+TABLE_ALBUM_DETAIL+" ON "+TABLE_ALBUM_DETAIL+".detail_song_id = song.id " +
                "WHERE "+TABLE_ALBUM_DETAIL+".detail_album_id = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id1 = cursor.getInt(0);
                int img_id = cursor.getInt(1);
                String songName = cursor.getString(2);
                String singerName = cursor.getString(3);
                Song song = new Song(img_id,songName,singerName,id1);
                songs.add(song);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songs;
    }

    public void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, song.getFileId());
        values.put(COLUMN_IMG_ID, song.getImageId());
        values.put(COLUMN_SONG_NAME, song.getSongName());
        values.put(COLUMN_SINGER_NAME, song.getSingerName());
        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    public void addFauSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAU_ID, song.getFileId());
        values.put(COLUMN_FAU_IMG_ID, song.getImageId());
        values.put(COLUMN_FAU_SONG_NAME, song.getSongName());
        values.put(COLUMN_FAU_SINGER_NAME, song.getSingerName());

        db.insert(TABLE_FAUVORITE_SONG, null, values);
        db.close();
    }
    public void addAlbum(Album album) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALBUM_ID, album.getId());
        values.put(COLUMN_ALBUM_NAME, album.getName());
        values.put(COLUMN_ALBUM_SINGER_NAME, album.getSingerName());

        db.insert(TABLE_ALBUM, null, values);
        Log.d("TAGGG", "addAlbum: "+album.getSingerName());
        db.close();
    }
    public void addSongToAlbum(Song song ,Album album){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETAIL_SONG_ID,song.getFileId());
        values.put(COLUMN_DETAIL_ALBUM_ID,album.getId());
        db.insert(TABLE_ALBUM_DETAIL,null,values);
        db.close();
    }
    public ArrayList<Song> getAllFauSongs() {
        ArrayList<Song> fauSongs = new ArrayList<>();
        SQLiteDatabase fauDb = this.getReadableDatabase();
        String fauSelectQuery = "SELECT * FROM " + TABLE_FAUVORITE_SONG;

        Cursor fauCursor = fauDb.rawQuery(fauSelectQuery, null);
        if (fauCursor.moveToFirst()) {
            do {
                int id = fauCursor.getInt(0);
                int imgId = fauCursor.getInt(1);
                String songName = fauCursor.getString(2);
                String singerName = fauCursor.getString(3);
                Song song = new Song(imgId, songName, singerName, id);
                fauSongs.add(song);
            } while (fauCursor.moveToNext());

            fauCursor.close();
            fauDb.close();
            return fauSongs;
        }
        return null;
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SONG;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int imgId = cursor.getInt(1);
                String songName = cursor.getString(2);
                String singerName = cursor.getString(3);
                Song song = new Song(imgId, songName, singerName, id);
                songs.add(song);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return songs;
        }
        return null;
    }
    public ArrayList<Album> getAllAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ALBUM;
        Cursor alCursor = db.rawQuery(selectQuery, null);
        if (alCursor != null && alCursor.moveToFirst()) {
            do {
                int id = alCursor.getInt(0);
                String name = alCursor.getString(1);
                String singerName = alCursor.getString(2);
                Album album = new Album(id, name, singerName);
                albums.add(album);
            } while (alCursor.moveToNext());
            alCursor.close();
            db.close();
            return albums;
        }
        return null;
    }
    public void deleteFauSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAUVORITE_SONG, COLUMN_FAU_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteAlLSongsInAlbum(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_ALBUM_DETAIL,null,null);
        Log.d("TAG", "deleteAlLSongsInAlbum: finish");
    }
    public void deleteAllFauSongs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAUVORITE_SONG, null, null);
        db.close();
    }
}
