import {
  InferAttributes,
  InferCreationAttributes,
  Model,
  CreationOptional,
} from "sequelize";

export interface Favorite
  extends Model<InferAttributes<Favorite>, InferCreationAttributes<Favorite>> {
  id: CreationOptional<number>;
  userId?: number;
  movieId?: number;
  createdAt?: Date;
  updatedAt?: Date;
}
