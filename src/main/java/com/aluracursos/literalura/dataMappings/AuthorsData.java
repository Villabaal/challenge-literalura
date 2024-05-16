package com.aluracursos.literalura.dataMappings;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;

public record AuthorsData(
        @JsonAlias("birth_year") Long birthYear,
        @JsonAlias("death_year") Long deathYear,
        @JsonAlias("name") String name ) {
}
