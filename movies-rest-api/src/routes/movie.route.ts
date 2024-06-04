import { Router } from "express";
import { MovieController } from "../controllers";
import { authenticateToken, validateRequestSchema } from "../middleware";
import { createMovieSchema, updateMovieSchema } from "../schema";
const multer = require("multer");

const router = Router();

/**
 * Search for movies based on a query string.
 */
router.get("/movies/search/:query", MovieController.searchMovie);

/**
 * Create a movie.
 */
router.post(
  "/movies",
  authenticateToken,
  validateRequestSchema(createMovieSchema),
  MovieController.createMovie
);

/**
 *
 */
router.get(
  "/movies/favorites",
  authenticateToken,
  MovieController.getFavoriteMovies
);

/**
 *
 */
router.get(
  "/movies/comments",
  authenticateToken,
  MovieController.getCommentedMovies
);

/**
 *
 */
router.get(
  "/movies/ratings",
  authenticateToken,
  MovieController.getRatedMovies
);

/**
 * Update a movie.
 */
router.put(
  "/movies/:id",
  authenticateToken,
  validateRequestSchema(updateMovieSchema),
  MovieController.updateMovie
);

/**
 * Delete a movie.
 */
router.delete("/movies/:id", authenticateToken, MovieController.deleteMovie);

/**
 * Get of latest movies.
 */
router.get("/movies/latest", MovieController.getLatestMovies);

/**
 * Get a movie by its ID.
 */
router.get("/movies/:id", authenticateToken, MovieController.getMovieById);

/**
 * Get movie recommendations based on the current movie ID.
 */
router.get(
  "/movies/:movieId/recommendations",
  MovieController.recommendationsByMovieId
);

/**
 * Get movies by genre.
 */
router.get("/movies/genre/:genreId", MovieController.getMoviesByGenre);

/**
 * Get movies by country.
 */
router.get("/movies/country/:countryId", MovieController.getMoviesByCountry);

export default router;
