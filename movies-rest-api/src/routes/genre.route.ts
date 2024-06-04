import { Router } from "express";
import GenreController from "../controllers/genre.controller";
import { authenticateToken } from "../middleware";

const router = Router();

router.get("/genres", authenticateToken, GenreController.getGenres);

export default router;
