package com.aluracursos.literalura.dataMappings;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AuthorsData> authors,
        @JsonAlias("subjects") List<String> subjects,
        @JsonAlias("download_count") Long downloadCount,
        @JsonAlias("languages") List<String> languages ){
}
