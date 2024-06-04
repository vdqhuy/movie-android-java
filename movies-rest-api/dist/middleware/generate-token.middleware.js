"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const utils_1 = require("../utils");
const generateToken = (user) => {
    return jsonwebtoken_1.default.sign({ id: user.id }, utils_1.JWT_SECRET_KEY, {
        expiresIn: 100 * 60000,
    });
};
exports.default = generateToken;
