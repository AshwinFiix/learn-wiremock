package com.learnwiremock.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnwiremock.dto.Movie;
import com.learnwiremock.exception.MovieErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class MoviesServiceTest {

    MoviesRestClient moviesRestClient = null;
    WebClient webClient;
    ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        int port = 8081;
        final String baseUrl = String.format("http://localhost:%s", port);
        webClient = WebClient.create();
        moviesRestClient = new MoviesRestClient(baseUrl, webClient);

    }

    @Test
    public void getAllMovies() {

        //given

        //when
        List<Movie> movieList = moviesRestClient.retrieveAllMovies();
        System.out.println("movieList : " + movieList);

        //then
        assertTrue(!movieList.isEmpty());
    }

    @Test
    public void retrieveMovieById() {
        //given
        Integer movieId = 1;

        //when
        Movie movie = moviesRestClient.retrieveMovieById(movieId);

        //then
        assertEquals("Batman Begins", movie.getName());
    }

    @Test(expected = MovieErrorResponse.class)
    public void retrieveMovieById_NotFound() {
        //given
        Integer movieId = 100;

        //when
        moviesRestClient.retrieveMovieById(movieId);

    }

    @Test
    public void retrieveMovieByName() {
        //given
        String movieName = "Avengers";

        //when
        List<Movie> movieList = moviesRestClient.retrieveMovieByName(movieName);

        //then
        String expectedCastName = "Robert Downey Jr, Chris Evans , Chris HemsWorth";
        assertEquals(4, movieList.size());
        assertEquals(expectedCastName, movieList.get(0).getCast());
    }

    @Test(expected = MovieErrorResponse.class)
    public void retrieveMovieByName_Not_Found() {
        //given
        String movieName = "ABC";

        //when
        moviesRestClient.retrieveMovieByName(movieName);
    }


    @Test
    public void retrieveMovieByYear() {
        //given
        Integer year = 2012;

        //when
        List<Movie> movieList = moviesRestClient.retreieveMovieByYear(year);

        //then
        assertEquals(2, movieList.size());

    }

    @Test(expected = MovieErrorResponse.class)
    public void retrieveMovieByYear_Not_Found() {
        //given
        Integer year = 1950;

        //when
        moviesRestClient.retreieveMovieByYear(year);

    }

    @Test
    public void addNewMovie() {
        //given
        String batmanBeginsCrew = "Tom Hanks, Tim Allen";
        Movie toyStory = new Movie(null, "Toy Story 4", 2019, batmanBeginsCrew, LocalDate.of(2019, 06, 20));

        //when
        Movie movie = moviesRestClient.addNewMovie(toyStory);

        //then
        assertTrue(movie.getMovie_id() != null);

    }

    @Test(expected = MovieErrorResponse.class)
    public void addNewMovie_InvalidInput() {
        //given
        String batmanBeginsCrew = "Tom Hanks, Tim Allen";
        Movie toyStory = new Movie(null, null, null, batmanBeginsCrew, LocalDate.of(2019, 06, 20));

        //when
        moviesRestClient.addNewMovie(toyStory);

    }

    @Test
    public void updateMovie() {
        //given
        String darkNightRisesCrew = "Tom Hardy";
        Movie darkNightRises = new Movie(null, null, null, darkNightRisesCrew, null);
        Integer movieId = 3;

        //when
        Movie updatedMovie = moviesRestClient.updateMovie(movieId, darkNightRises);

        //then
        String updatedCastName = "Christian Bale, Heath Ledger , Michael Caine, Tom Hardy";
        assertEquals(updatedCastName, updatedMovie.getCast());


    }

    @Test(expected = MovieErrorResponse.class)
    public void updateMovie_Not_Found() {
        //given
        String darkNightRisesCrew = "Tom Hardy";
        Movie darkNightRises = new Movie(null, null, null, darkNightRisesCrew, null);
        Integer movieId = 100;

        //when
         moviesRestClient.updateMovie(movieId, darkNightRises);
    }

    @Test
    public void deleteMovie() {

        //given
        String batmanBeginsCrew = "Tom Hanks, Tim Allen";
        Movie toyStory = new Movie(null, "Toy Story 4", 2019, batmanBeginsCrew, LocalDate.of(2019, 06, 20));
        Movie movie = moviesRestClient.addNewMovie(toyStory);
        Integer movieId=movie.getMovie_id().intValue();

        //when
        String response = moviesRestClient.deleteMovieById(movieId);

        //then
        String expectedResponse = "Movie Deleted Successfully";
        assertEquals(expectedResponse, response);

    }

    @Test(expected = MovieErrorResponse.class)
    public void deleteMovie_notFound() {

        //given
        Integer movieId=100;

        //when
        moviesRestClient.deleteMovieById(movieId) ;

    }


}
