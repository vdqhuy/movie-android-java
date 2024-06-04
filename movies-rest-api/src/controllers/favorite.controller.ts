import { RequestHandler } from "express";
import { Favorite, ResponseResult } from "../interfaces";
import { FavoriteModel, MovieModel } from "../models";
import { CreateFavoriteParams, DeleteFavoriteParams } from "../schema";
import { ErrorCode, sendResponse } from "../utils";

const getFavoritesByUserId: RequestHandler<
  unknown,
  ResponseResult<Array<Favorite> | undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const favorites = await FavoriteModel.findAll({
      where: {
        userId: req.user.id,
      },
      include: [MovieModel],
    });
    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: favorites,
    });
  } catch (error) {
    next(error);
  }
};

const createFavorite: RequestHandler<
  CreateFavoriteParams,
  ResponseResult<Favorite | undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { movieId } = req.params;
    const userId = req.user.id;

    const movie = await MovieModel.findByPk(movieId);
    if (!movie) {
      return sendResponse(res, ErrorCode["movie-not-found"]);
    }

    const checkfavorite = await FavoriteModel.findOne({
      where: {
        userId: req.user.id,
        movieId,
      },
    });
    if (checkfavorite) {
      console.log("dit me");
      sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Vui lòng thử lại",
      });
      return;
    }

    const favorite = await FavoriteModel.sync({ alter: true }).then(() => {
      return FavoriteModel.create({
        movieId,
        userId,
      });
    });

    sendResponse(res, {
      code: 201,
      status: "Success",
      data: favorite,
    });
  } catch (error) {
    next(error);
  }
};

const deleteFavorite: RequestHandler<
  DeleteFavoriteParams,
  ResponseResult<undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { movieId } = req.params;

    const favorite = await FavoriteModel.findOne({
      where: {
        userId: req.user.id,
        movieId,
      },
    });
    if (!favorite) {
      return sendResponse(res, ErrorCode["favorite-not-found"]);
    }
    favorite.destroy();

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Xoá yêu thích thành công.",
    });
  } catch (error) {
    next(error);
  }
};

const FavoriteController = {
  getFavoritesByUserId,
  createFavorite,
  deleteFavorite,
};

export default FavoriteController;
