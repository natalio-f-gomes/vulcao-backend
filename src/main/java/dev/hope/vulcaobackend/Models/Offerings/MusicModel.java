package dev.hope.vulcaobackend.Models.Offerings;


import dev.hope.vulcaobackend.Models.Common.BaseOfferingModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class MusicModel extends BaseOfferingModel {
    private String albumName;
    private Set<String> artistName;

    private List<String> featuredArtists;
    private String recordLabel;
    private List<String> writers;

    @DynamoDbSecondaryPartitionKey(indexNames = "album-name-index")
    public String getAlbumName(){return this.albumName;}

   //private ArrayList<ReviewModel> reviews = new ArrayList<>();
}
