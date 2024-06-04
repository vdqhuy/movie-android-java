import express, { Express, Response } from "express";
import {
  CommentRoutes,
  CountryRoutes,
  FavoriteRoutes,
  GenreRoutes,
  MovieRoutes,
  RatingRoutes,
  UserRoutes,
} from "./routes";

const bodyParser = require("body-parser");
const app: Express = express();
const port = 3000;

const apiVersion = "1";
const apiRoutes = `/api/v${apiVersion}`;

// parse application/x-www-form-urlencoded
app.use(
  bodyParser.urlencoded({
    limit: "50mb",
    extended: true,
    parameterLimit: 50000,
  })
);

// parse application/json
app.use(
  bodyParser.json({
    extended: true,
    limit: "50mb",
  })
);

app.get("/", (req: any, res: Response) => {
  res.send("Real Film!.");
});

app.use(apiRoutes, UserRoutes);
app.use(apiRoutes, CountryRoutes);
app.use(apiRoutes, GenreRoutes);
app.use(apiRoutes, MovieRoutes);
app.use(apiRoutes, CommentRoutes);
app.use(apiRoutes, FavoriteRoutes);
app.use(apiRoutes, RatingRoutes);

app.listen(port, () => {
  console.log(`Server đang khởi chạy tại cổng ${port}`);
});
