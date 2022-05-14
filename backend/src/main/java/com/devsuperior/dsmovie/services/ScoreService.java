package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.Movie;
import com.devsuperior.dsmovie.entities.Score;
import com.devsuperior.dsmovie.entities.User;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScoreService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @Transactional(readOnly = true)
    public MovieDTO saveScore(ScoreDTO scoreDTO){
        User user = userRepository.findByEmail(scoreDTO.getEmail()); //retrive user
        if(user == null){
            user = new User();
            user.setEmail(scoreDTO.getEmail());
            userRepository.saveAndFlush(user);
        }
        Movie movie = movieRepository.findById(scoreDTO.getMovieId()).get(); //retrieve movie

        Score score = new Score();
        score.setUser(user);
        score.setMovie(movie);
        score.setValue(scoreDTO.getValue());
        scoreRepository.saveAndFlush(score);

        double sum = 0.0;
        for (Score s : movie.getScores()) {
            sum += s.getValue();
        }
        double avg = sum/movie.getScores().size(); //calc avg

        movie.setScore(avg);
        movie.setCount(movie.getScores().size());

        movie = movieRepository.saveAndFlush(movie); //update score movie
        return new MovieDTO(movie);
    }
}
