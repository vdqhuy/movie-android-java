import { Router } from "express";
import { CountryController } from "../controllers";
import { authenticateToken } from "../middleware";

const router = Router();

router.get("/countries", authenticateToken, CountryController.getCountries);

export default router;
