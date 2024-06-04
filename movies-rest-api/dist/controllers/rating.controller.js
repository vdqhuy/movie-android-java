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
const getRatingByMovie = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const rates = yield models_1.RatingModel.findAll({
            where: {
                movieId,
            },
            include: [models_1.UserModel],
        });
        if (rates == null) return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["rate-not-found"]);
        return (0, utils_1.sendResponse)(res, {
            code: 200,
            status: "Success",
            data: rates,
        });
    }
    catch (error) {
        next(error);
    }
});
// const getRatingByUserId: RequestHandler<
//   unknown,
//   ResponseResult<Array<Favorite> | undefined>,
//   unknown,
//   unknown
// > = async (req, res, next) => {
//   try {
//     const ratings = await RatingModel.findAll({
//       where: {
//         userId: req.user.id,
//       },
//     });
//     return sendResponse(res, {
//       code: 200,
//       status: "Success",
//       data: ratings,
//     });
//   } catch (error) {
//     next(error);
//   }
// };
const rateMovie = (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const { movieId } = req.params;
        const { rating } = req.body;
        const userId = req.user.id;
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            return (0, utils_1.sendResponse)(res, utils_1.ErrorCode["movie-not-found"]);
        }
        const ratingFound = yield models_1.RatingModel.findOne({
            where: {
                userId: userId,
                movieId: movieId,
            },
        });
        if (ratingFound) {
            ratingFound.update({
                rating,
            });
            return (0, utils_1.sendResponse)(res, {
                code: 200,
                status: "Success",
                message: "Cập nhật đánh giá thành công.",
            });
        }
        const ratingRecord = yield models_1.RatingModel.sync({ alter: true }).then(() => {
            return models_1.RatingModel.create({
                movieId,
                userId: userId,
                rating,
            });
        });
        (0, utils_1.sendResponse)(res, {
            code: 201,
            status: "Success",
            data: ratingRecord,
        });
    }
    catch (error) {
        next(error);
    }
});
// const updateRating: RequestHandler<
//   UpdateRatingParams,
//   ResponseResult<undefined>,
//   UpdateRatingBody,
//   unknown
// > = async (req, res, next) => {
//   try {
//     const { ratingId } = req.params;
//     const { rating } = req.body;
//     const ratingFound = await RatingModel.findByPk(ratingId);
//     if (!ratingFound) {
//       return sendResponse(res, ErrorCode["favorite-not-found"]);
//     }
//     ratingFound.update({
//       rating,
//     });
//     return sendResponse(res, {
//       code: 200,
//       status: "Success",
//       message: "Cập nhật đánh giá thành công.",
//     });
//   } catch (error) {
//     next(error);
//   }
// };
const RatingController = {
    getRatingByMovie,
    // getRatingByUserId,
    rateMovie,
    // updateRating,
};
exports.default = RatingController;
