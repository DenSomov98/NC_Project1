package Model;

import java.util.LinkedList;

public class TrackModel {
    private LinkedList<Track> tracks;
    private GenreModel genreModel;
    public void addTrack(String name, String artist, String namegenre)throws Exception{
        Genre tmp;
        try{
            tmp=genreModel.getGenre(namegenre);
        }
        catch (Exception e){
            tmp =new Genre(namegenre);
            genreModel.addGenre(tmp);
        }
        tracks.addLast(new Track(name,artist,tmp));
    }
    public void removeTrack(int index){
        tracks.remove(index);
    }
    public void editName(int index, String name){
        Track track=tracks.get(index);
        track.setName(name);
    }
    public void editArtist(int index, String artist){
        Track track=tracks.get(index);
        track.setArtist(artist);
    }
    public void editGenre(int index, String genre){
        Track track=tracks.get(index);
        Genre tmp;
        try{
            tmp=genreModel.getGenre(genre);
        }
        catch (Exception e){
            tmp =new Genre(genre);
            genreModel.addGenre(tmp);
        }
        track.setGenre(tmp);
    }
    public String getViewList(){
        StringBuilder s=new StringBuilder();
        int index=1;
        for (Track track:tracks) {
            s.append(track.getName()).append(":")
                    .append(" ")
                    .append(track.getArtist())
                    .append(",")
                    .append(" ")
                    .append(track.getGenre().toString())
                    .append(" ")
                    .append(index);
            index++;
        }
        return s.toString();
    }
}
