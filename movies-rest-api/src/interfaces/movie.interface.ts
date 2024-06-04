import {
  CreationOptional,
  InferAttributes,
  InferCreationAttributes,
  Model,
} from "sequelize";

export interface Movie
  extends Model<InferAttributes<Movie>, InferCreationAttributes<Movie>> {
  id: CreationOptional<number>;
  title: string;
  description: string;
  genre: string;
  director: string;
  releaseYear: string;
  duration: number;
  posterHorizontal: string;
  posterVertical: string;
  country: string;
  actors: string;
  videoURL: Array<string | undefined>;
  trailerURL: string;
  createdAt?: Date;
  updatedAt?: Date;
  rating?: number;
}
