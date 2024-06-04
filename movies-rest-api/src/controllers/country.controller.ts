import { Request, Response } from "express";
import { countryData } from "../data";
import { Country, ResponseResult } from "../interfaces";
import { sendResponse } from "../utils";

const getCountries = (
  req: Request,
  res: Response<ResponseResult<Array<Country>>>
) => {
  try {
    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "",
      data: countryData,
    });
  } catch (error) {}
};

const CountryController = {
  getCountries,
};

export default CountryController;
