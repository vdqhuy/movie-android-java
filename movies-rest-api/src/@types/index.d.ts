import { User } from "../interfaces";
declare module "express-serve-static-core" {
  export interface Request {
    user: User;
  }
}
