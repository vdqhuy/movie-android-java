import { Router } from "express";
import { FavoriteController } from "../controllers";
import { authenticateToken, validateRequestSchema } from "../middleware";
import { createFavoriteSchema, deleteFavoriteSchema } from "../schema";

const router = Router();

// Get favorites by user ID
// router.get(
//   "/users/favorites",
//   authenticateToken,
//   FavoriteController.getFavoritesByUserId
// );

// Create a favorite
router.post(
  "/movies/:movieId/favorites",
  authenticateToken,
  validateRequestSchema(createFavoriteSchema),
  FavoriteController.createFavorite
);

// Delete a favorite
router.delete(
  "/movies/:movieId/favorites",
  authenticateToken,
  validateRequestSchema(deleteFavoriteSchema),
  FavoriteController.deleteFavorite
);

export default router;
