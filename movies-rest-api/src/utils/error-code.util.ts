import { ResponseResult } from "../interfaces";

const ErrorCode: Record<
  | "user-not-found"
  | "movie-not-found"
  | "comment-not-found"
  | "favorite-not-found",
  ResponseResult<undefined>
> = {
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
};

export default ErrorCode;
