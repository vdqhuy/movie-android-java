"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const models_1 = require("../models");
const utils_1 = require("../utils");
const getCommentById = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { commentId } = req.params;
        const comment = yield models_1.CommentModel.findByPk(commentId, {
            include: [models_1.UserModel, models_1.MovieModel],
        });
        if (!comment) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["comment-not-found"]);
        }
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: comment,
        });
    }
    catch (error) {
        next(error);
    }
});
const getCommentsByMovie = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const comments = yield models_1.CommentModel.findAll({
            where: {
                movieId,
            },
            include: [models_1.UserModel],
        });
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: comments,
        });
    }
    catch (error) {
        next(error);
    }
});
const getCommentsByUserId = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const comments = yield models_1.CommentModel.findAll({
            where: {
                userId: req.user.id,
            },
            include: [models_1.MovieModel],
        });
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: comments,
        });
    }
    catch (error) {
        next(error);
    }
});
const createComment = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const { comment } = req.body;
        const userId = req.user.id;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const newComment = yield models_1.CommentModel.sync({ alter: true }).then(() => {
            return models_1.CommentModel.create({
                comment,
                movieId,
                userId,
            });
        });
        (0, utils_1.sendResponse)(res, {
            code: 201,
            status: "Success",
            data: newComment,
        });
    }
    catch (error) {
        next(error);
    }
});
const updateComment = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { commentId } = req.params;
        const { comment } = req.body;
        const data = yield models_1.CommentModel.findByPk(commentId);
        if (!data) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["comment-not-found"]);
        }
        data.update({
            comment: comment,
        });
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            message: "Cập nhật bình luận thành công.",
        });
    }
    catch (error) {
        next(error);
    }
});
const deleteComment = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { commentId } = req.params;
        const data = yield models_1.CommentModel.findByPk(commentId);
        if (!data) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["comment-not-found"]);
        }
        data.destroy();
        return (0, utils_1.sendResponse)(res, {
            code: 204,
            status: "Success",
            message: "Xoá bình luận thành công.",
        });
    }
    catch (error) {
        next(error);
    }
});
const CommentController = {
    getCommentById,
    getCommentsByMovie,
    getCommentsByUserId,
    createComment,
    updateComment,
    deleteComment,
};
exports.default = CommentController;
