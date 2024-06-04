import {
  CreationOptional,
  InferAttributes,
  InferCreationAttributes,
  Model,
} from "sequelize";

export interface User
  extends Model<InferAttributes<User>, InferCreationAttributes<User>> {
  id: CreationOptional<number>;
  name: string;
  email: string;
  birthday: string;
  createdAt?: Date;
  updatedAt?: Date;
  password?: string;
  photoURL?: string;
  isAdmin?: boolean;
}
