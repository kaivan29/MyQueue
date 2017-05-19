package cs371m.myqueue;

/**
 * Created by Kaivan on 5/1/2017.
 */


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "overview",
        "vote_average"
})

public class tMDB {
    @JsonProperty("overview")
    private String overview;

    @JsonProperty("vote_average")
    private double rating;

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("vote_average")
    public Double getRating() {
        return rating;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("vote_average")
    public void setRating(double rating) {
        this.rating = rating;
    }

}
