"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.genreData = exports.countryData = void 0;
var country_data_1 = require("./country.data");
Object.defineProperty(exports, "countryData", { enumerable: true, get: function () { return __importDefault(country_data_1).default; } });
var genre_data_1 = require("./genre-data");
Object.defineProperty(exports, "genreData", { enumerable: true, get: function () { return __importDefault(genre_data_1).default; } });
