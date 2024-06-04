import { Response } from "express";

const sendResponse = <T>(res: Response<T>, result: T) => {
  const response: any = {
    ...result,
  };

  const code = response?.code;

  res.status(code).json(response);
};

export default sendResponse;
