import * as yup from "yup";

// Create a favorite
export const createFavoriteSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
});

export type CreateFavoriteParams = yup.InferType<
  typeof createFavoriteSchema
>["params"];

// Delete a favorite
export const deleteFavoriteSchema = yup.object({
  params: yup.object({
    movieId: yup.number().required(),
  }),
});

export type DeleteFavoriteParams = yup.InferType<
  typeof deleteFavoriteSchema
>["params"];
