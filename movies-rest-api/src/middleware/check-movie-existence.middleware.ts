import { MovieModel } from "../models";

const checkMovieExistence = async (movieId: number) => {
  try {
    const movie = await MovieModel.findByPk(movieId);
    if (!movie) {
      throw new Error("Movie not found");
    }
    return movie;
  } catch (error) {
    throw error;
  }
};

module.exports = checkMovieExistence;
