import { Router } from "express";
import { CommentController } from "../controllers";
import { authenticateToken, validateRequestSchema } from "../middleware";
import {
  createCommentSchema,
  deleteCommentSchema,
  getCommentByMovieSchema,
  updateCommentSchema,
} from "../schema";

const router = Router();

// Get comment by its ID
router.get(
  "/comments/:commentId",
  authenticateToken,
  CommentController.getCommentById
);

// Get comments by movie ID
router.get(
  "/movies/:movieId/comments",
  validateRequestSchema(getCommentByMovieSchema),
  CommentController.getCommentsByMovie
);

// // Get comments by user ID
// router.get(
//   "/users/comments",
//   authenticateToken,
//   CommentController.getCommentsByUserId
// );

// Create a comment
router.post(
  "/movies/:movieId/comments",
  authenticateToken,
  validateRequestSchema(createCommentSchema),
  CommentController.createComment
);

// Update a comment
router.put(
  "/comments/:commentId",
  authenticateToken,
  validateRequestSchema(updateCommentSchema),
  CommentController.updateComment
);

// Delete a comment
router.delete(
  "/comments/:commentId",
  authenticateToken,
  validateRequestSchema(deleteCommentSchema),
  CommentController.deleteComment
);

export default router;
