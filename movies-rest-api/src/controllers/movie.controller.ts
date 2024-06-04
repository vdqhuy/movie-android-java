import { Request, RequestHandler, Response } from "express";
import { Op } from "sequelize";
import unidecode from "unidecode";
import { countryData, genreData } from "../data";
import { Movie, ResponseResult } from "../interfaces";
import {
  CommentModel,
  FavoriteModel,
  MovieModel,
  RatingModel,
} from "../models";
import { CreateMovieBody, UpdateMovieBody } from "../schema";
import { sendResponse } from "../utils";

const createMovie: RequestHandler<
  unknown,
  ResponseResult<Movie | undefined>,
  CreateMovieBody,
  unknown
> = (req, res, next) => {
  try {
    const {
      title,
      description,
      genre,
      director,
      releaseYear,
      duration,
      posterHorizontal,
      posterVertical,
      country,
      actors,
      videoURL,
      trailerURL,
    } = req.body;

    const newGenre = typeof genre == "object" ? genre : [genre];
    const newVideoUrl = typeof videoURL == "object" ? videoURL : [videoURL];

    MovieModel.sync({ alter: true }).then(() => {
      return MovieModel.create({
        title,
        description,
        genre: newGenre as any,
        director,
        releaseYear,
        duration,
        posterHorizontal,
        posterVertical,
        country,
        actors,
        videoURL: newVideoUrl as any,
        trailerURL,
      });
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Thêm phim thành công.",
    });
  } catch (error) {
    next(error);
  }
};

const updateMovie: RequestHandler<any, unknown, any, unknown> = async (
  req,
  res,
  next
) => {
  try {
    const {
      title,
      description,
      genre,
      director,
      releaseYear,
      duration,
      posterHorizontal,
      posterVertical,
      country,
      actors,
      videoURL,
      trailerURL,
    } = req.body;

    const newVideoUrl = typeof videoURL == "object" ? videoURL : [videoURL];
    const newGenre = typeof genre == "object" ? genre : [genre];
    const { id } = req.params;
    const movie = await MovieModel.findByPk(id);

    if (!movie) {
      return sendResponse(res, {
        code: 404,
        status: "Error",
        message: "Không tìm thấy phim.",
      });
    }

    const updateMovie = {
      title,
      description,
      director,
      releaseYear,
      duration,
      posterHorizontal,
      posterVertical,
      actors,
      trailerURL,
      genre: newGenre,
      videoURL: newVideoUrl as any,
    };

    movie.update({ ...updateMovie });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: movie,
    });
  } catch (error) {}
};

const deleteMovie = async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    const movie = await MovieModel.findByPk(id);

    if (!movie) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Không tìm thấy phim.",
      });
    }
    movie.destroy();

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Xoá phim thành công.",
    });
  } catch (error) {}
};

const getMovieById = async (
  req: Request,
  res: Response<ResponseResult<Movie | null>>,
  next: any
) => {
  try {
    const { id } = req.params;
    const userId = req.user.id;

    const movie = await MovieModel.findByPk(id);

    if (!movie) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Không tìm thấy phim.",
      });
    }

    const favorite = await FavoriteModel.findOne({
      where: {
        userId: userId,
        movieId: id,
      },
    });
    console.log("trigger2");

    const ratings = await RatingModel.findAll({
      where: {
        movieId: id,
      },
    });

    const sumRatings = ratings
      ? ratings.reduce((total, e) => total + e.rating, 0)
      : 0;

    const numberOfReviews = ratings ? ratings.length : 0;

    console.log(sumRatings);
    console.log(numberOfReviews);

    const numRating = sumRatings / numberOfReviews;

    const newGenre = movie.genre
      .split(", ")
      .map((id) => genreData.find((genre) => genre.code === id))
      .filter(Boolean)
      .map((genre) => genre?.name)
      .join(", ");

    const newCountry = countryData.find((e) => e.code === movie?.country)?.name;

    const newMovie: Movie = {
      ...(movie.toJSON() as any),
      genre: newGenre,
      country: newCountry,
      numberOfReviews: numberOfReviews,
      rating: numRating ? numRating : 0,
      hasFavorite: !!favorite,
    };
    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: newMovie,
    });
  } catch (error) {}
};

const searchMovie = async (req: Request, res: Response) => {
  try {
    const { query } = req.params;

    const normalizedSearchQuery = unidecode(query);

    const movies = await MovieModel.findAll({
      where: {
        title: {
          [Op.like]: `%${normalizedSearchQuery.trim()}%`,
        },
      },
      attributes: { exclude: ["genre"] },
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    console.log(error);
  }
};

const recommendationsByMovieId = async (req: Request, res: Response) => {
  try {
    const { movieId } = req.params;

    const movie = await MovieModel.findByPk(movieId);

    const genre = movie?.genre;

    // const genreQuery = genre?.map((g) => `genre LIKE '%"${g}"%'`).join(" OR ");

    const recommendationMovies = await MovieModel.findAll({
      where: {
        id: {
          [Op.not]: movieId,
        },
        // [Op.or]: Sequelize.literal(genreQuery as string),
      },
      attributes: { exclude: ["genre"] },
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: recommendationMovies,
    });
  } catch (error) {
    console.log(error);
  }
};

const getLatestMovies: RequestHandler<
  unknown,
  unknown,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const movies = await MovieModel.findAll({
      order: [["releaseYear", "DESC"]],
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const getMoviesByGenre: RequestHandler<any, unknown, unknown, unknown> = async (
  req,
  res,
  next
) => {
  try {
    const { genreId } = req.params;

    const movies = await MovieModel.findAll({
      where: {
        genre: {
          [Op.like]: `%${genreId}%`,
        },
      },
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const getMoviesByCountry: RequestHandler<
  any,
  unknown,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { countryId } = req.params;

    const movies = await MovieModel.findAll({
      where: {
        country: countryId,
      },
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const getFavoriteMovies: RequestHandler<any, any, any, any> = async (
  req,
  res,
  next
) => {
  try {
    const userId = req.user.id;

    const movies = await MovieModel.findAll({
      include: [
        {
          model: FavoriteModel,
          where: { userId },
        },
      ],
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const getCommentedMovies: RequestHandler<any, any, any, any> = async (
  req,
  res,
  next
) => {
  try {
    const userId = req.user.id;

    const movies = await MovieModel.findAll({
      include: [
        {
          model: CommentModel,
          where: { userId },
        },
      ],
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const getRatedMovies: RequestHandler<any, any, any, any> = async (
  req,
  res,
  next
) => {
  try {
    const userId = req.user.id;

    const movies = await MovieModel.findAll({
      include: [
        {
          model: RatingModel,
          where: { userId },
        },
      ],
    });

    sendResponse(res, {
      code: 200,
      status: "Success",
      data: movies,
    });
  } catch (error) {
    next(error);
  }
};

const MovieController = {
  createMovie,
  updateMovie,
  deleteMovie,
  getMovieById,
  searchMovie,
  recommendationsByMovieId,
  getLatestMovies,
  getMoviesByGenre,
  getMoviesByCountry,
  getFavoriteMovies,
  getCommentedMovies,
  getRatedMovies,
};

export default MovieController;
