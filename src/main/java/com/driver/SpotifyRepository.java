package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User curuser : users){
            if (curuser.getMobile().equals(mobile))return  curuser;
        }
        User u1 = new User(name,mobile);
        users.add(u1);
        return u1;
    }


    public Artist createArtist(String name) {
        for (Artist a1 : artists){
            if (a1.getName().equals(name))return a1;
        }
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = createArtist(artistName);
    for (Album album :albums) {
        if (album.getTitle().equals(title)) return album;
    }
    Album al = new Album(title);
    albums.add(al);
  //  return al;
        List<Album>alb = new ArrayList<>();
        if (artistAlbumMap.containsKey(artist)){
            alb = artistAlbumMap.get(artist);
        }
        alb.add(al);
        artistAlbumMap.put(artist,alb);
        return al;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean isAlbumPresent = false;
        Album album = new Album();
        for (Album currentAlbum : albums) {
            if (currentAlbum.getTitle().equals(albumName)) {
                album = currentAlbum;
                isAlbumPresent = true;
                break;
            }
        }
            if (isAlbumPresent == false){
                throw new Exception("Album does not exist");
            }
            Song song = new Song(title,length);
            songs.add(song);

          //adding album & its songs into albumMap
            List<Song>songlist = new ArrayList<>();
          if (albumSongMap.containsKey(album)){
              songlist = albumSongMap.get(album);
          }
          songlist.add(song);
          albumSongMap.put(album,songlist);

     return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
  for (Playlist playlist :playlists) {
      if (playlist.getTitle().equals(title)) return playlist;
  }
      Playlist playlist = new Playlist(title);
      //add playlist1 to list of playlist list
      playlists.add(playlist);

      List<Song>newSonglist = new ArrayList<>();
      for (Song song : songs){
          if (song.getLength()==length)newSonglist.add(song);
      }
      playlistSongMap.put(playlist,newSonglist);

      User currentuser= new User();
      boolean flag = false;
      for (User user : users){
          if (user.getMobile().equals(mobile)){
              currentuser = user;
              flag = true;
              break;
          }
      }
      if (flag==false){
          throw new Exception("User does not exist");
      }
      List<User>userlist = new ArrayList<>();
      if (playlistListenerMap.containsKey(playlist)){
          userlist = playlistListenerMap.get(playlist);
      }
      userlist.add(currentuser);
      playlistListenerMap.put(playlist,userlist);
      creatorPlaylistMap.put(currentuser,playlist);

      List<Playlist>userplaylists = new ArrayList<>();
      if (userPlaylistMap.containsKey(currentuser)){
          userplaylists = userPlaylistMap.get(currentuser);
      }
      userplaylists.add(playlist);
      userPlaylistMap.put(currentuser,userplaylists);
      return playlist;
  }


    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
for (Playlist playlist : playlists){
    if (playlist.getTitle().equals(title))return playlist;
}
Playlist playlist = new Playlist(title);
playlists.add(playlist); //add playlist to playlistlist
 List<Song> temp = new ArrayList<>();
 for (Song song : songs){
     if (songTitles.contains(song.getTitle())){
         temp.add(song);
     }
 }
 playlistSongMap.put(playlist,temp);

 User currentUser = new User();
 boolean flag = false;
 for (User user : users){
     if (user.getMobile().equals(mobile)){
         currentUser = user;
         flag = true;
         break;
     }
 }
 if (flag == false){
     throw new Exception("User does not exist");
 }
 List<User> userlist = new ArrayList<>();
 if (playlistListenerMap.containsKey(playlist)){
     userlist = playlistListenerMap.get(playlist);
 }
 userlist.add(currentUser);
 playlistListenerMap.put(playlist,userlist);
 creatorPlaylistMap.put(currentUser,playlist);

 List<Playlist>userplaylists = new ArrayList<>();
 if (userPlaylistMap.containsKey(currentUser)){
     userplaylists = userPlaylistMap.get(currentUser);
 }
 userplaylists.add(playlist);
 userPlaylistMap.put(currentUser,userplaylists);
 return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
boolean flag = false;
Playlist playlist = new Playlist();
for (Playlist currplaylist : playlists){
    if (currplaylist.getTitle().equals(playlistTitle)){
        playlist = currplaylist;
        flag = true;break;
    }
}
if (flag==false){
    throw new Exception("Playlist does not exist");
}
User curUser = new User();
boolean flag2 = false;
for (User user : users){
    if (user.getMobile().equals(mobile)){
        curUser = user;
        flag2 = true; break;
    }
}
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        List<User>userlist = new ArrayList<>();
        if (playlistListenerMap.containsKey(playlist)){
            userlist = playlistListenerMap.get(playlist);
        }
        if (!userlist.contains(curUser)){
            userlist.add(curUser);
        }
        playlistListenerMap.put(playlist,userlist);
        if (creatorPlaylistMap.get(curUser)!= playlist)creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if (userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
User curUser = new User();
boolean flag = false;
for (User user : users){
    if (user.getMobile().equals(mobile)){
        curUser = user;
        flag =true;break;
    }
}
if (flag==false){
    throw new Exception("User does not exist");
}
Song song = new Song();
boolean flag1 = false;
for (Song cursong : songs){
    if (cursong.getTitle().equals(songTitle)){
        song = cursong;
        flag1 = true;break;
    }
}
        if (flag==false){
            throw new Exception("Song does not exist");
        }
        List<User> users = new ArrayList<>();
        if (songLikeMap.containsKey(song)){
            users = songLikeMap.get(song);
        }
        if (!users.contains(curUser)){
            users.add(curUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);

            Album album = new Album();
            for (Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if (temp.contains(song)){
                    album = curAlbum;
                    break;
                }
            }
            Artist artist = new Artist();
            for (Artist curArtist : artistAlbumMap.keySet()){
                List<Album>temp = artistAlbumMap.get(curArtist);
                if (temp.contains(album)){
                    artist = curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes()+1);
        }
    return song;
    }

    public String mostPopularArtist() {
    String name ="";
    int maxLikes = Integer.MIN_VALUE;
    for (Artist artist1 : artists){
        maxLikes = Math.max(maxLikes,artist1.getLikes());
    }
    for (Artist art : artists){
        if (maxLikes == art.getLikes()){
            name = art.getName();
        }
    }
    return  name;
    }

    public String mostPopularSong() {
    String name ="";
        int maxLikes = Integer.MIN_VALUE;
        for (Song song : songs){
            if (maxLikes== Math.max(maxLikes,song.getLikes()));
        }
        for (Song song : songs){
            if (maxLikes== song.getLikes())name = song.getTitle();
        }
    return name;}
}
