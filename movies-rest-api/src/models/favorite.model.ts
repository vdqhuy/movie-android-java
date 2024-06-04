import { DataTypes } from "sequelize";
import { connection } from "../config";
import { Favorite } from "../interfaces";
import { TABLE_NAME } from "../utils";

const FavoriteModel = connection.define<Favorite>(TABLE_NAME.FAVORITES, {
  id: {
    type: DataTypes.INTEGER.UNSIGNED,
    autoIncrement: true,
    primaryKey: true,
  },
});

export default FavoriteModel;
