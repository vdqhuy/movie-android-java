import { DataTypes } from "sequelize";
import { connection } from "../config";
import { Comment } from "../interfaces";
import { TABLE_NAME } from "../utils";

const CommentModel = connection.define<Comment>(TABLE_NAME.COMMENTS, {
  id: {
    type: DataTypes.INTEGER.UNSIGNED,
    autoIncrement: true,
    primaryKey: true,
  },
  comment: {
    type: DataTypes.STRING,
    allowNull: false,
  },
});

export default CommentModel;
