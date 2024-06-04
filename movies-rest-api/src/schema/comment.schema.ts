import * as yup from "yup";

// Get comment by its ID
export const getCommentByIdSchema = yup.object({
  params: yup.object({
    commentId: yup.number().required(),
  }),
});

export type GetCommentByIdParams = yup.InferType<
  typeof getCommentByIdSchema
>["params"];

// Get comment by movie ID
export const getCommentByMovieSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
});

export type GetCommentsByMovieParams = yup.InferType<
  typeof getCommentByMovieSchema
>["params"];

// Create a new comment
export const createCommentSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
  body: yup.object({
    comment: yup.string().required(),
  }),
});

export type CreateCommentParams = yup.InferType<
  typeof createCommentSchema
>["params"];

export type CreateCommentBody = yup.InferType<
  typeof createCommentSchema
>["body"];

// Update a comment
export const updateCommentSchema = yup.object({
  params: yup.object({
    commentId: yup.number().required(),
  }),
  body: yup.object({
    comment: yup.string().required(),
  }),
});

export type UpdateCommentParams = yup.InferType<
  typeof updateCommentSchema
>["params"];

export type UpdateCommentBody = yup.InferType<
  typeof updateCommentSchema
>["body"];

// Delete a comment
export const deleteCommentSchema = yup.object({
  params: yup.object({
    commentId: yup.number().required(),
  }),
});

export type DeleteCommentParams = yup.InferType<
  typeof deleteCommentSchema
>["params"];
