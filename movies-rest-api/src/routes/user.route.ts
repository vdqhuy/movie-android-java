import { Router } from "express";
import { UserController } from "../controllers";
import { authenticateToken, validateRequestSchema } from "../middleware";
import { loginSchema, signupSchema, updateUserSchema } from "../schema";

const router = Router();

router.post(
  "/signup",
  validateRequestSchema(signupSchema),
  UserController.signUp
);

router.post("/login", validateRequestSchema(loginSchema), UserController.login);

router.put(
  "/profile",
  authenticateToken,
  validateRequestSchema(updateUserSchema),
  UserController.updateProfile
);

router.get("/profile", authenticateToken, UserController.getProfile);

export default router;
