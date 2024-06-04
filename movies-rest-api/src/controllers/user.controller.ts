import bcrypt from "bcryptjs";
import { RequestHandler } from "express";
import _ from "lodash";
import { ResponseResult, User } from "../interfaces";
import { generateToken } from "../middleware";
import { UserModel } from "../models";
import { LoginBody, SignupBody, UpdateUserBody } from "../schema";
import { sendResponse } from "../utils";

const signUp: RequestHandler<
  unknown,
  ResponseResult<User | undefined>,
  SignupBody,
  unknown
> = async (req, res, next) => {
  try {
    const { name, email, birthday, password } = req.body;

    const existingUser = await UserModel.findOne({
      where: { email: email },
    });

    if (existingUser) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Email đã được đăng ký",
      });
    }

    const hashedPassword = await bcrypt.hash(password as string, 10);

    UserModel.sync({ alter: true }).then(() => {
      return UserModel.create({
        name: name,
        email: email,
        birthday: birthday,
        password: hashedPassword,
      });
    });

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Tạo tài khoản thành công.",
    });
  } catch (error) {
    next(error);
  }
};

const login: RequestHandler<
  unknown,
  ResponseResult<User | undefined>,
  LoginBody,
  unknown
> = async (req, res, next) => {
  try {
    const { email, password } = req.body;
    const user = await UserModel.findOne({ where: { email: email } });

    if (!user) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Email hoặc mật khẩu không đúng, vui lòng thử lại.",
      });
    }

    const isPasswordMatch = await bcrypt.compare(
      password as string,
      user.password as string
    );

    if (!isPasswordMatch) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Email hoặc mật khẩu không đúng, vui lòng thử lại.",
      });
    }

    const userObj = _.omit(user.toJSON() as User, ["password"]);

    const accessToken = generateToken(user);

    console.log(accessToken);

    return sendResponse(res, {
      code: 200,
      status: "Success",
      message: "Đăng nhập thành công.",
      data: userObj as User,
      accessToken: accessToken,
    });
  } catch (error) {}
};

const updateProfile: RequestHandler<
  unknown,
  ResponseResult<User | undefined>,
  UpdateUserBody,
  unknown
> = async (req, res) => {
  try {
    const { name, birthday, photoURL } = req.body;
    const userId = req.user?.id;

    if (!userId) {
      return res.status(200).json({
        code: 400,
        status: "Error",
        message: "Vui lòng kiểm tra lại.",
      });
    }

    const user = await UserModel.findByPk(userId);

    if (!user) {
      return res.status(200).json({
        code: 400,
        status: "Error",
        message: "Người dùng không tồn tại.",
      });
    }

    user.update({
      name,
      birthday,
      photoURL,
    });

    return res.status(200).json({
      code: 200,
      status: "Success",
      message: "Cập nhật thông tin thành công.",
    });
  } catch (error) {}
};

const getProfile: RequestHandler<
  unknown,
  ResponseResult<User | undefined>,
  unknown,
  unknown
> = async (req, res) => {
  try {
    const userId = req.user?.id;
    const user = await UserModel.findByPk(userId, {
      attributes: { exclude: ["password"] },
    });
    if (!user) {
      return sendResponse(res, {
        code: 400,
        status: "Error",
        message: "Người dùng không tồn tại.",
      });
    }
    return sendResponse(res, {
      code: 200,
      status: "Success",
      data: user,
    });
  } catch (error) {}
};

const UserController = {
  signUp,
  login,
  updateProfile,
  getProfile,
};

export default UserController;
