import {
  InferAttributes,
  InferCreationAttributes,
  Model,
  CreationOptional,
} from "sequelize";

export interface Comment
  extends Model<InferAttributes<Comment>, InferCreationAttributes<Comment>> {
  id: CreationOptional<number>;
  comment: string;
  userId?: number;
  movieId?: number;
  createdAt?: Date;
  updatedAt?: Date;
}
