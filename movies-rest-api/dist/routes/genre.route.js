"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const genre_controller_1 = __importDefault(require("../controllers/genre.controller"));
const middleware_1 = require("../middleware");
const router = (0, express_1.Router)();
router.get("/genres", middleware_1.authenticateToken, genre_controller_1.default.getGenres);
exports.default = router;
