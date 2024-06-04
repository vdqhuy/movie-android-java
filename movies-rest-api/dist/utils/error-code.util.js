"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ErrorCode = {
    "user-not-found": {
        code: 400,
        status: "Error",
        message: "Người dùng không tồn tại.",
    },
    "movie-not-found": {
        code: 400,
        status: "Error",
        message: "Phim không tồn tại.",
    },
    "comment-not-found": {
        code: 400,
        status: "Error",
        message: "Bình luận không tồn tại.",
    },
    "favorite-not-found": {
        code: 400,
        status: "Error",
        message: "Yêu thích không tồn tại.",
    },
    "rate-not-found": {
        code: 400,
        status: "Error",
        message: "Yêu thích không tồn tại.",
    },
};
exports.default = ErrorCode;
