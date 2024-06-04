"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const controllers_1 = require("../controllers");
const middleware_1 = require("../middleware");
const schema_1 = require("../schema");
const router = (0, express_1.Router)();
// Get favorites by user ID
// router.get(
//   "/users/favorites",
//   authenticateToken,
//   FavoriteController.getFavoritesByUserId
// );
// Create a favorite
router.post("/movies/:movieId/favorites", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.createFavoriteSchema), controllers_1.FavoriteController.createFavorite);
// Delete a favorite
router.delete("/movies/:movieId/favorites", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.deleteFavoriteSchema), controllers_1.FavoriteController.deleteFavorite);
// Get favorites by movie
router.get("/movies/:movieId/favorites", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.getFavoritesByMovieSchema), controllers_1.FavoriteController.getFavoritesByMovie);
exports.default = router;
