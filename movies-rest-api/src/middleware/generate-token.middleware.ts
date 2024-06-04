import jwt from "jsonwebtoken";
import { User } from "../interfaces";
import { JWT_SECRET_KEY } from "../utils";

import _ from "lodash";

const generateToken = (user: User) => {
  return jwt.sign({ id: user.id }, JWT_SECRET_KEY, {
    expiresIn: 100 * 60000,
  });
};

export default generateToken;
