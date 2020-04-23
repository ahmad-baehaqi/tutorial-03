package com.apap.tu03.controller;

import java.util.List;
import java.util.Optional;

import com.apap.tu03.model.MovieModel;
import com.apap.tu03.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @RequestMapping("/movie/add")
    public String add(@RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "genre", required = true) String genre,
            @RequestParam(value = "budget", required = true) Long budget,
            @RequestParam(value = "duration", required = true) Integer duration) {
        MovieModel movie = new MovieModel(id, title, genre, budget, duration);
        movieService.addMovie(movie);
        return "add";
    }

    @RequestMapping("/movie/view")
    public String view(@RequestParam("id") String id, Model model) {
        MovieModel archive = movieService.getMovieDetail(id);
        model.addAttribute("movie", archive);
        return "view-movie";
    }

    @RequestMapping("/movie/viewall")
    public String viewAll(Model model) {
        List<MovieModel> archive = movieService.getMovieList();
        model.addAttribute("movies", archive);
        return "viewall-movie";
    }

    @RequestMapping(value = { "/movie/view/", "/movie/view/{id}" })
    public String viewMovie(Model model, @PathVariable Optional<String> id) {
        if (id != null) {
            MovieModel movie = movieService.getMovieDetail(id.get());
            if (movie != null) {
                model.addAttribute("movie", movie);
                return "view-movie";
            }
            model.addAttribute("id", id.get());
        }
        return "error-view";
    }

    @RequestMapping("/movie/update/{id}/duration/{duration}")
    public String updateDuration(Model model, @PathVariable Optional<String> id, @PathVariable Integer duration) {
        if (id != null && duration != null) {
            MovieModel movie = movieService.getMovieDetail(id.get());
            if (movie != null) {
                movie.setDuration(duration);
                model.addAttribute("movie", movie);
                return "update-movie";
            }
        }
        return "fail-update";
    }

    @RequestMapping("movie/delete/{id}")
    public String deleteMovie(Model model, @PathVariable Optional<String> id) {
        if (id != null) {
            MovieModel movie = movieService.deleteMovie(id.get());
            if (movie != null) {
                model.addAttribute("id", id.get());
                return "delete-movie";
            }
        }
        model.addAttribute("id", id.get());
        return "fail-delete";
    }

}