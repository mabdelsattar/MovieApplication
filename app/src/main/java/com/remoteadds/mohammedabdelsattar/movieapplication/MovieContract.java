package com.remoteadds.mohammedabdelsattar.movieapplication;
        import com.activeandroid.Model;
        import com.activeandroid.annotation.Column;
        import com.activeandroid.annotation.Table;



@Table(name ="MovieTable")
public class MovieContract extends Model{



    @Column(name = "movieID")
    private int movieID;
    @Column(name = "title")
    private String title;
    @Column(name = "path")
    private String path;
    @Column(name = "overview")
    private String overview;
    @Column(name = "date")
    private String date;
    @Column(name = "rate")
    private int rate;



    public int getmovieID() {
        return movieID;
    }

    public void setmovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }



    public MovieContract(int movieID, String title, String path, String overview, String date, int rate) {
        super();
        this.movieID = movieID;
        this.title = title;
        this.path = path;
        this.overview = overview;
        this.date = date;
        this.rate = rate;
    }
    public MovieContract(){

        super();
    }
}