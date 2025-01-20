// SPDX-License-Identifier: MIT
pragma solidity ^0.8.28;

contract MovieReview {
    struct Review {
        uint256 id;
        uint256 movieId;
        string reviewText;
        uint8 rating;
        address reviewer;
        uint256 reviewerId;
    }

    Review[] public reviews;
    uint256 public reviewCount;
    string public textRaw;

    event ReviewAdded(
        uint256 id,
        uint256 movieId,
        string reviewText,
        uint8 rating,
        address reviewer,
        uint256 reviewerId
    );

    function setText(string calldata _text) external {
        textRaw = _text;
    }

    function addReview(
        uint256 _movieId,
        string memory _reviewText,
        uint256 _reviewerId,
        uint8 _rating
    ) public {
        require(_rating >= 1 && _rating <= 5, "Rating must be between 1 and 5");
        require(bytes(_reviewText).length > 0, "Review text cannot be empty");

        reviews.push(
            Review(reviewCount, _movieId, _reviewText, _rating, msg.sender, _reviewerId)
        );

        emit ReviewAdded(reviewCount, _movieId, _reviewText, _rating, msg.sender, _reviewerId);
        reviewCount++;
    }

    function getReviewsByMovie(uint256 _movieId) public view returns (Review[] memory) {
        uint256 count = 0;
        for (uint256 i = 0; i < reviews.length; i++) {
            if (reviews[i].movieId == _movieId) {
                count++;
            }
        }

        Review[] memory movieReviews = new Review[](count);
        uint256 index = 0;

        for (uint256 i = 0; i < reviews.length; i++) {
            if (reviews[i].movieId == _movieId) {
                movieReviews[index] = reviews[i];
                index++;
            }
        }

        return movieReviews;
    }
}
