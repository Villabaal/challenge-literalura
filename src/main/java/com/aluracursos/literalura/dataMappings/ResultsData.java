package com.aluracursos.literalura.dataMappings;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultsData(
        @JsonAlias("results") List<BookData> results ) {
}
