import { DataTypes } from "sequelize";
import { connection } from "../config";
import { Movie } from "../interfaces";
import { TABLE_NAME } from "../utils";

const MovieModel = connection.define<Movie>(TABLE_NAME.MOVIES, {
  id: {
    type: DataTypes.INTEGER.UNSIGNED,
    autoIncrement: true,
    primaryKey: true,
  },
  title: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  description: {
    type: DataTypes.TEXT,
    allowNull: false,
  },
  genre: {
    type: DataTypes.TEXT,
    allowNull: false,
    // get() {
    //   const value: any = this.getDataValue("genre");
    //   return value ? value : [];
    // },
    set(value: string[]) {
      this.setDataValue("genre", value.join(", ") as any);
    },
  },
  director: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  releaseYear: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  duration: {
    type: DataTypes.INTEGER,
    allowNull: false,
  },
  posterHorizontal: {
    type: DataTypes.TEXT("long"),
    allowNull: false,
  },
  posterVertical: {
    type: DataTypes.TEXT("long"),
    allowNull: false,
  },
  country: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  actors: {
    type: DataTypes.TEXT,
    allowNull: false,
  },
  videoURL: {
    type: DataTypes.TEXT,
    allowNull: false,
    get() {
      const value: any = this.getDataValue("videoURL");
      return value ? JSON.parse(value) : [];
    },
    set(value: string[]) {
      this.setDataValue("videoURL", JSON.stringify(value) as any);
    },
  },
  trailerURL: {
    type: DataTypes.TEXT,
    allowNull: false,
  },
});

export default MovieModel;
