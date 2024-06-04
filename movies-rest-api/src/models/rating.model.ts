import { DataTypes } from "sequelize";
import { connection } from "../config";
import { Rating } from "../interfaces";
import { TABLE_NAME } from "../utils";

const RatingModel = connection.define<Rating>(TABLE_NAME.RATINGS, {
  id: {
    type: DataTypes.INTEGER.UNSIGNED,
    autoIncrement: true,
    primaryKey: true,
  },
  rating: {
    type: DataTypes.INTEGER,
    allowNull: false,
  },
});

export default RatingModel;
