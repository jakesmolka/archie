package com.nedap.archie.odin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nedap.archie.rm.datastructures.Item;

import java.util.List;

public interface OdinParsingClusterMixin {

    @JsonDeserialize(converter = ItemMapToListConverter.class)
    void setItems(List<Item> child);


}
