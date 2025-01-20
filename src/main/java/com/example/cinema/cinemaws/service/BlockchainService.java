package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.contract.MovieReview;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

@Service
public class BlockchainService {

    private final Web3j web3j;

    @Value("${contract.address}")
    private String contractAddress;

    @Value("${private.key:}")
    private String privateKey;

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
    }

    @PostConstruct
    public void validatePrivateKey() {
        if (privateKey == null || privateKey.isEmpty()) {
            throw new IllegalArgumentException("Private key must be configured!");
        }
    }

    public MovieReview loadContract(Credentials credentials) throws Exception {
        BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L);
        BigInteger gasLimit = BigInteger.valueOf(6_721_975L);

        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        return MovieReview.load(contractAddress, web3j, credentials, gasProvider);
    }

    public String addReview(BigInteger movieTitle, String reviewText, BigInteger reviewerId, BigInteger rating) {
        try {
            Credentials credentials = Credentials.create(privateKey);
            MovieReview contract = loadContract(credentials);
            TransactionReceipt receipt = contract.addReview(movieTitle, reviewText, reviewerId, rating).send();
            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Transaction failed with status: " + receipt.getStatus());
            }
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add review: " + e.getMessage(), e);
        }
    }

    public List getAllReviews(BigInteger movieId) {
        try {
            Credentials credentials = Credentials.create(privateKey);
            MovieReview contract = loadContract(credentials);
            RemoteFunctionCall<List> allReviews = contract.getReviewsByMovie(movieId);
            List reviews = allReviews.send();

            if (reviews == null || reviews.isEmpty()) {
                return Collections.emptyList();
            }

            return reviews;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch reviews: " + e.getMessage(), e);
        }
    }

}
