import { RequestHandler } from "express";
import { Comment, ResponseResult } from "../interfaces";
import { CommentModel, MovieModel, UserModel } from "../models";
import {
  CreateCommentBody,
  CreateCommentParams,
  DeleteCommentParams,
  GetCommentByIdParams,
  GetCommentsByMovieParams,
  UpdateCommentBody,
  UpdateCommentParams,
} from "../schema";
import { ErrorCode, sendResponse } from "../utils";

const getCommentById: RequestHandler<
  GetCommentByIdParams,
  ResponseResult<Comment | undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { commentId } = req.params;

    const comment = await CommentModel.findByPk(commentId, {
      include: [UserModel, MovieModel],
    });
    if (!comment) {
      return sendResponse(res, ErrorCode["comment-not-found"]);
    }

    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: comment,
    });
  } catch (error) {
    next(error);
  }
};

const getCommentsByMovie: RequestHandler<
  GetCommentsByMovieParams,
  ResponseResult<Array<Comment> | undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { movieId } = req.params;

    const movie = await MovieModel.findByPk(movieId);
    if (!movie) {
      return sendResponse(res, ErrorCode["movie-not-found"]);
    }

    const comments = await CommentModel.findAll({
      where: {
        movieId,
      },
      include: [UserModel],
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: comments,
    });
  } catch (error) {
    next(error);
  }
};

const getCommentsByUserId: RequestHandler<
  unknown,
  ResponseResult<Array<Comment> | undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const comments = await CommentModel.findAll({
      where: {
        userId: req.user.id,
      },
      include: [MovieModel],
    });
    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: comments,
    });
  } catch (error) {
    next(error);
  }
};

const createComment: RequestHandler<
  CreateCommentParams,
  ResponseResult<Comment | undefined>,
  CreateCommentBody,
  unknown
> = async (req, res, next) => {
  try {
    const { movieId } = req.params;
    const { comment } = req.body;
    const userId = req.user.id;

    const movie = await MovieModel.findByPk(movieId);
    if (!movie) {
      return sendResponse(res, ErrorCode["movie-not-found"]);
    }

    const newComment = await CommentModel.sync({ alter: true }).then(() => {
      return CommentModel.create({
        comment,
        movieId,
        userId,
      });
    });

    sendResponse(res, {
      code: 201,
      status: "Success",
      data: newComment,
    });
  } catch (error) {
    next(error);
  }
};

const updateComment: RequestHandler<
  UpdateCommentParams,
  ResponseResult<undefined>,
  UpdateCommentBody,
  unknown
> = async (req, res, next) => {
  try {
    const { commentId } = req.params;
    const { comment } = req.body;

    const data = await CommentModel.findByPk(commentId);
    if (!data) {
      return sendResponse(res, ErrorCode["comment-not-found"]);
    }
    data.update({
      comment: comment,
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Cập nhật bình luận thành công.",
    });
  } catch (error) {
    next(error);
  }
};

const deleteComment: RequestHandler<
  DeleteCommentParams,
  ResponseResult<undefined>,
  unknown,
  unknown
> = async (req, res, next) => {
  try {
    const { commentId } = req.params;

    const data = await CommentModel.findByPk(commentId);
    if (!data) {
      return sendResponse(res, ErrorCode["comment-not-found"]);
    }
    data.destroy();

    return sendResponse(res, {
      code: 204,
      status: "Success",
      message: "Xoá bình luận thành công.",
    });
  } catch (error) {
    next(error);
  }
};

const CommentController = {
  getCommentById,
  getCommentsByMovie,
  getCommentsByUserId,
  createComment,
  updateComment,
  deleteComment,
};

export default CommentController;
