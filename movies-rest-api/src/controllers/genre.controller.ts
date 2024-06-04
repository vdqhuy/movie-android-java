import { Request, Response } from "express";
import { genreData } from "../data";
import { Genre, ResponseResult } from "../interfaces";
import { sendResponse } from "../utils";

const getGenres = (
  req: Request,
  res: Response<ResponseResult<Array<Genre>>>
) => {
  try {
    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "",
      data: genreData,
    });
  } catch (error) {}
};

const GenreController = {
  getGenres,
};

export default GenreController;
