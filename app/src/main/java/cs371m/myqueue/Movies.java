package cs371m.myqueue;

/**
 * Created by erinjensby on 4/27/17.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "total_results",
        "total_returned",
        "results"                   //10 results
})

public class Movies {

    @JsonProperty("total_results")
    private long totalResults;
    @JsonProperty("total_returned")
    private long totalReturned;
    @JsonProperty("results")
    private ArrayList<Result> results = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("total_results")
    public long getTotalResults() {
        return totalResults;
    }

    @JsonProperty("total_results")
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    @JsonProperty("total_returned")
    public long getTotalReturned() {
        return totalReturned;
    }

    @JsonProperty("total_returned")
    public void setTotalReturned(long totalReturned) {
        this.totalReturned = totalReturned;
    }

    @JsonProperty("results")
    public ArrayList<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(ArrayList<Result> results) {
        this.results = results;
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