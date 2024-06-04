"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const controllers_1 = require("../controllers");
const middleware_1 = require("../middleware");
const schema_1 = require("../schema");
const router = (0, express_1.Router)();
// Get comment by its ID
router.get("/comments/:commentId", middleware_1.authenticateToken, controllers_1.CommentController.getCommentById);
// Get comments by movie ID
router.get("/movies/:movieId/comments", (0, middleware_1.validateRequestSchema)(schema_1.getCommentByMovieSchema), controllers_1.CommentController.getCommentsByMovie);
// // Get comments by user ID
// router.get(
//   "/users/comments",
//   authenticateToken,
//   CommentController.getCommentsByUserId
// );
// Create a comment
router.post("/movies/:movieId/comments", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.createCommentSchema), controllers_1.CommentController.createComment);
// Update a comment
router.put("/comments/:commentId", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.updateCommentSchema), controllers_1.CommentController.updateComment);
// Delete a comment
router.delete("/comments/:commentId", middleware_1.authenticateToken, (0, middleware_1.validateRequestSchema)(schema_1.deleteCommentSchema), controllers_1.CommentController.deleteComment);
exports.default = router;
