"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.UserModel = exports.RatingModel = exports.MovieModel = exports.FavoriteModel = exports.CommentModel = void 0;
const comment_model_1 = __importDefault(require("./comment.model"));
const favorite_model_1 = __importDefault(require("./favorite.model"));
const movie_model_1 = __importDefault(require("./movie.model"));
const rating_model_1 = __importDefault(require("./rating.model"));
const user_model_1 = __importDefault(require("./user.model"));
var comment_model_2 = require("./comment.model");
Object.defineProperty(exports, "CommentModel", { enumerable: true, get: function () { return __importDefault(comment_model_2).default; } });
var favorite_model_2 = require("./favorite.model");
Object.defineProperty(exports, "FavoriteModel", { enumerable: true, get: function () { return __importDefault(favorite_model_2).default; } });
var movie_model_2 = require("./movie.model");
Object.defineProperty(exports, "MovieModel", { enumerable: true, get: function () { return __importDefault(movie_model_2).default; } });
var rating_model_2 = require("./rating.model");
Object.defineProperty(exports, "RatingModel", { enumerable: true, get: function () { return __importDefault(rating_model_2).default; } });
var user_model_2 = require("./user.model");
Object.defineProperty(exports, "UserModel", { enumerable: true, get: function () { return __importDefault(user_model_2).default; } });
// Establish relationships between User, Comment, and Movie.
user_model_1.default.hasMany(comment_model_1.default);
comment_model_1.default.belongsTo(user_model_1.default, {
    foreignKey: "userId",
});
movie_model_1.default.hasMany(comment_model_1.default);
comment_model_1.default.belongsTo(movie_model_1.default, {
    foreignKey: "movieId",
});
// Establish relationships between User, Comment, and Favorite.
user_model_1.default.hasMany(favorite_model_1.default);
favorite_model_1.default.belongsTo(user_model_1.default, {
    foreignKey: "userId",
});
movie_model_1.default.hasMany(favorite_model_1.default);
favorite_model_1.default.belongsTo(movie_model_1.default, {
    foreignKey: "movieId",
});
// Establish relationships between User, Comment, and Rating.
user_model_1.default.hasMany(rating_model_1.default);
rating_model_1.default.belongsTo(user_model_1.default, {
    foreignKey: "userId",
});
movie_model_1.default.hasMany(rating_model_1.default);
rating_model_1.default.belongsTo(movie_model_1.default, {
    foreignKey: "movieId",
});
