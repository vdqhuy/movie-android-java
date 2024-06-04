import * as yup from "yup";

// Get rating by movie ID
export const getRatingByMovieSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
});

// Create a rating
export const createRatingSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
  body: yup.object({
    rating: yup.number().required(),
  }),
});

export type CreateRatingParams = yup.InferType<
  typeof createRatingSchema
>["params"];

export type CreateRatingBody = yup.InferType<typeof createRatingSchema>["body"];

// Update a rating
export const updateRatingSchema = yup.object({
  params: yup.object({
    ratingId: yup.number().required(),
  }),
  body: yup.object({
    rating: yup.number().required(),
  }),
});

export type UpdateRatingParams = yup.InferType<
  typeof updateRatingSchema
>["params"];

export type UpdateRatingBody = yup.InferType<typeof updateRatingSchema>["body"];
