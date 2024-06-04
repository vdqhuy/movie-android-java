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
const getFavoritesByUserId = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const favorites = yield models_1.FavoriteModel.findAll({
            where: {
                userId: req.user.id,
            },
            include: [models_1.MovieModel],
        });
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: favorites,
        });
    }
    catch (error) {
        next(error);
    }
});
const createFavorite = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const userId = req.user.id;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const checkfavorite = yield models_1.FavoriteModel.findOne({
            where: {
                userId: req.user.id,
                movieId,
            },
        });
        if (checkfavorite) {
            console.log("dit me");
            (0, utils_1.sendResponse)(res, {
                code: 400,
                status: "Error",
                message: "Vui lòng thử lại",
            });
            return;
        }
        const favorite = yield models_1.FavoriteModel.sync({ alter: true }).then(() => {
            return models_1.FavoriteModel.create({
                movieId,
                userId,
            });
        });
        (0, utils_1.sendResponse)(res, {
            code: 201,
            status: "Success",
            data: favorite,
        });
    }
    catch (error) {
        next(error);
    }
});
const deleteFavorite = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const favorite = yield models_1.FavoriteModel.findOne({
            where: {
                userId: req.user.id,
                movieId,
            },
        });
        if (!favorite) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["favorite-not-found"]);
        }
        favorite.destroy();
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            message: "Xoá yêu thích thành công.",
        });
    }
    catch (error) {
        next(error);
    }
});
const getFavoritesByMovie = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const favorites = yield models_1.FavoriteModel.findAll({
            where: {
                movieId,
            },
            include: [models_1.UserModel],
        });
        if (favorites == null) return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["favorite-not-found"]);
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: favorites,
        });
    }
    catch (error) {
        next(error);
    }
});
const FavoriteController = {
    getFavoritesByUserId,
    createFavorite,
    deleteFavorite,
    getFavoritesByMovie
};
exports.default = FavoriteController;
