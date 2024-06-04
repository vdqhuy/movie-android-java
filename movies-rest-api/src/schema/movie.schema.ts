import * as yup from "yup";

// Create a movie
export const createMovieSchema = yup.object({
  body: yup.object({
    title: yup.string().required(),
    description: yup.string().required(),
    genre: yup
      .mixed()
      .test((value) => {
        if (Array.isArray(value)) {
          return yup.array().of(yup.string()).isValidSync(value);
        } else if (typeof value === "number") {
          return yup.number().isValidSync(value);
        } else if (typeof value === "string") {
          return yup.string().isValidSync(value);
        }
        return false;
      })
      .required(),
    director: yup.string().required(),
    releaseYear: yup.string().required("Release year is required"),
    duration: yup.number().integer().required(),
    posterHorizontal: yup.string().required(),
    posterVertical: yup.string().required(),
    country: yup.string().required(),
    actors: yup.string().required(),
    videoURL: yup
      .mixed()
      .test((value) => {
        if (Array.isArray(value)) {
          return yup.array().of(yup.string()).isValidSync(value);
        } else if (typeof value === "number") {
          return yup.number().isValidSync(value);
        } else if (typeof value === "string") {
          return yup.string().isValidSync(value);
        }
        return false;
      })
      .required(),
    trailerURL: yup
      .string()
      .url("Link trailer không hợp lệ")
      .required("Trailer URL is required"),
  }),
});

export type CreateMovieBody = yup.InferType<
  Required<typeof createMovieSchema>
>["body"];

// Update a movie

export const updateMovieSchema = yup.object().shape({
  body: yup.object().shape({
    title: yup.string().notRequired(),
    description: yup.string().notRequired(),
    genre: yup
      .mixed()
      .test("isValidGenre", "Genre is invalid", (value) => {
        if (value !== undefined) {
          if (Array.isArray(value)) {
            return yup.array().of(yup.string()).isValidSync(value);
          } else if (typeof value === "number") {
            return yup.number().isValidSync(value);
          } else if (typeof value === "string") {
            return yup.string().isValidSync(value);
          }
          return false;
        }
        return true;
      })
      .nullable()
      .transform((value, originalValue) =>
        originalValue === "" ? null : value
      ),
    director: yup.string().notRequired(),
    releaseYear: yup.string().notRequired(),
    duration: yup.number().integer().notRequired(),
    posterHorizontal: yup.string().notRequired(),
    posterVertical: yup.string().notRequired(),
    country: yup.string().notRequired(),
    actors: yup.string().notRequired(),
    videoURL: yup
      .mixed()
      .test("isValidVideoURL", "Video URL is invalid", (value) => {
        if (value !== undefined) {
          if (Array.isArray(value)) {
            return yup.array().of(yup.string()).isValidSync(value);
          } else if (typeof value === "number") {
            return yup.number().isValidSync(value);
          } else if (typeof value === "string") {
            return yup.string().isValidSync(value);
          }
          return false;
        }
        return true;
      })
      .nullable()
      .transform((value, originalValue) =>
        originalValue === "" ? null : value
      ),
    trailerURL: yup.string().url("Invalid trailer URL").notRequired(),
  }),
});

export type UpdateMovieBody = yup.InferType<
  Required<typeof updateMovieSchema>
>["body"];
