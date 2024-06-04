"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const controllers_1 = require("../controllers");
const middleware_1 = require("../middleware");
const schema_1 = require("../schema");
const multer = require("multer");
const router = (0, express_1.Router)();
/**
 * Search for movies based on a query string.
 */
router.get("/movies/search/:query", controllers_1.MovieController.searchMovie);
/**
 * Create a movie.
 */
router.post("/movies", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.createMovieSchema), controllers_1.MovieController.createMovie);
/**
 *
 */
router.post("/movies/:id/view-count", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.createMovieSchema), controllers_1.MovieController.createMovie);
/**
 *
 */
router.get("/movies/favorites", middleware_1.authenticateToken, controllers_1.MovieController.getFavoriteMovies);
/**
 *
 */
router.get("/movies/comments", middleware_1.authenticateToken, controllers_1.MovieController.getCommentedMovies);
/**
 *
 */
router.get("/movies/ratings", middleware_1.authenticateToken, controllers_1.MovieController.getRatedMovies);
/**
 * Update a movie.
 */
router.put("/movies/:id", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.updateMovieSchema), controllers_1.MovieController.updateMovie);
/**
 * Delete a movie.
 */
router.delete("/movies/:id", middleware_1.authenticateToken, controllers_1.MovieController.deleteMovie);
/**
 * Get of latest movies.
 */
router.get("/movies/latest", controllers_1.MovieController.getLatestMovies);
/**
 * Get a movie by its ID.
 */
router.get("/movies/:id", middleware_1.authenticateToken, controllers_1.MovieController.getMovieById);
/**
 * Get movie recommendations based on the current movie ID.
 */
router.get("/movies/:movieId/recommendations", controllers_1.MovieController.recommendationsByMovieId);
/**
 * Get movies by genre.
 */
router.get("/movies/genre/:genreId", controllers_1.MovieController.getMoviesByGenre);
/**
 * Get movies by country.
 */
router.get("/movies/country/:countryId", controllers_1.MovieController.getMoviesByCountry);
exports.default = router;
