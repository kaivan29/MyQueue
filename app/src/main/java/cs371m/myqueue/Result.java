package cs371m.myqueue;

/**
 * Created by erinjensby on 4/27/17.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "release_year",
        "themoviedb",
        "original_title",
        "alternate_titles",
        "imdb",
        "pre_order",
        "in_theaters",
        "release_date",
        "rating",
        "rottentomatoes",
        "freebase",
        "wikipedia_id",
        "metacritic",
        "common_sense_media",
        "poster_120x171",
        "poster_240x342",
        "poster_400x570"
})
public class Result {

    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("release_year")
    private long releaseYear;
    @JsonProperty("themoviedb")
    private long themoviedb;
    @JsonProperty("original_title")
    private String originalTitle;
    @JsonProperty("alternate_titles")
    private List<String> alternateTitles = null;
    @JsonProperty("imdb")
    private String imdb;
    @JsonProperty("pre_order")
    private boolean preOrder;
    @JsonProperty("in_theaters")
    private boolean inTheaters;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("rating")
    private String rating;
    @JsonProperty("rottentomatoes")
    private long rottentomatoes;
    @JsonProperty("freebase")
    private String freebase;
    @JsonProperty("wikipedia_id")
    private long wikipediaId;
    @JsonProperty("metacritic")
    private String metacritic;
    @JsonProperty("common_sense_media")
    private Object commonSenseMedia;
    @JsonProperty("poster_120x171")
    private String poster120x171;
    @JsonProperty("poster_240x342")
    private String poster240x342;
    @JsonProperty("poster_400x570")
    private String poster400x570;
    @JsonProperty("artwork_304x171")
    private String artwork_304x171;
    @JsonProperty("first_aired")
    private String first_aired;

    public String type;
    public String source;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("release_year")
    public long getReleaseYear() {
        return releaseYear;
    }

    @JsonProperty("release_year")
    public void setReleaseYear(long releaseYear) {
        this.releaseYear = releaseYear;
    }

    @JsonProperty("themoviedb")
    public long getThemoviedb() {
        return themoviedb;
    }

    @JsonProperty("themoviedb")
    public void setThemoviedb(long themoviedb) {
        this.themoviedb = themoviedb;
    }

    @JsonProperty("original_title")
    public String getOriginalTitle() {
        return originalTitle;
    }

    @JsonProperty("original_title")
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @JsonProperty("alternate_titles")
    public List<String> getAlternateTitles() {
        return alternateTitles;
    }

    @JsonProperty("alternate_titles")
    public void setAlternateTitles(List<String> alternateTitles) {
        this.alternateTitles = alternateTitles;
    }

    @JsonProperty("imdb")
    public String getImdb() {
        return imdb;
    }

    @JsonProperty("imdb")
    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @JsonProperty("pre_order")
    public boolean isPreOrder() {
        return preOrder;
    }

    @JsonProperty("pre_order")
    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }

    @JsonProperty("in_theaters")
    public boolean isInTheaters() {
        return inTheaters;
    }

    @JsonProperty("in_theaters")
    public void setInTheaters(boolean inTheaters) {
        this.inTheaters = inTheaters;
    }

    @JsonProperty("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    @JsonProperty("release_date")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonProperty("rating")
    public String getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(String rating) {
        this.rating = rating;
    }

    @JsonProperty("rottentomatoes")
    public long getRottentomatoes() {
        return rottentomatoes;
    }

    @JsonProperty("rottentomatoes")
    public void setRottentomatoes(long rottentomatoes) {
        this.rottentomatoes = rottentomatoes;
    }

    @JsonProperty("freebase")
    public String getFreebase() {
        return freebase;
    }

    @JsonProperty("freebase")
    public void setFreebase(String freebase) {
        this.freebase = freebase;
    }

    @JsonProperty("wikipedia_id")
    public long getWikipediaId() {
        return wikipediaId;
    }

    @JsonProperty("wikipedia_id")
    public void setWikipediaId(long wikipediaId) {
        this.wikipediaId = wikipediaId;
    }

    @JsonProperty("metacritic")
    public String getMetacritic() {
        return metacritic;
    }

    @JsonProperty("metacritic")
    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    @JsonProperty("common_sense_media")
    public Object getCommonSenseMedia() {
        return commonSenseMedia;
    }

    @JsonProperty("common_sense_media")
    public void setCommonSenseMedia(Object commonSenseMedia) {
        this.commonSenseMedia = commonSenseMedia;
    }

    @JsonProperty("poster_120x171")
    public String getPoster120x171() {
        return poster120x171;
    }

    @JsonProperty("poster_120x171")
    public void setPoster120x171(String poster120x171) {
        this.poster120x171 = poster120x171;
    }

    @JsonProperty("artwork_304x171")
    public String getArtwork_304x171(){ return artwork_304x171; }

    @JsonProperty("first_aired")
    public String getFirst_aired() { return first_aired;}

    @JsonProperty("artwork_304x171")
    public void setArtwork_304x171(String artwork_304x171) {
        this.artwork_304x171 = artwork_304x171;
    }

    @JsonProperty("poster_240x342")
    public String getPoster240x342() {
        return poster240x342;
    }

    @JsonProperty("poster_240x342")
    public void setPoster240x342(String poster240x342) {
        this.poster240x342 = poster240x342;
    }

    @JsonProperty("poster_400x570")
    public String getPoster400x570() {
        return poster400x570;
    }

    @JsonProperty("poster_400x570")
    public void setPoster400x570(String poster400x570) {
        this.poster400x570 = poster400x570;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}