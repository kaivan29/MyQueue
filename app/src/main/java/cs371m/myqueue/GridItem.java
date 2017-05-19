package cs371m.myqueue;

/**
 * Created by Kaivan on 4/29/2017.
 */

public class GridItem {

    String title,image;
    long tMDBid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void settMDBid(long tMDBid) {
        this.tMDBid = tMDBid;
    }

    public long gettMDBid() {
        return tMDBid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}