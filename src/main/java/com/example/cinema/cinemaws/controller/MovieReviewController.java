package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/movie/review")
@RequiredArgsConstructor
public class MovieReviewController {

    private final BlockchainService blockchainService;
    private final ApiResponseFactory apiResponseFactory;


    @PostMapping("/add")
    public ResponseEntity<ApiResponseTO<Object>> addReview(
            @RequestParam BigInteger movieTitle,
            @RequestParam String reviewText,
            @RequestParam BigInteger reviewerId,
            @RequestParam BigInteger rating
    ) throws Exception {
        String transactionHash = blockchainService.addReview(movieTitle, reviewText, reviewerId, rating);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, transactionHash);

    }

    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponseTO<List>> getAllReviews(@PathVariable BigInteger movieId) throws Exception {
        List reviews = blockchainService.getAllReviews(movieId);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, reviews);
    }
}
