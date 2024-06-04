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
const checkMovieExistence = (movieId) => __awaiter(void 0, void 0, void 0, function* () {
    try {
        const movie = yield models_1.MovieModel.findByPk(movieId);
        if (!movie) {
            throw new Error("Movie not found");
        }
        return movie;
    }
    catch (error) {
        throw error;
    }
});
module.exports = checkMovieExistence;
