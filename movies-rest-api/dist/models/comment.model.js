"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const sequelize_1 = require("sequelize");
const config_1 = require("../config");
const utils_1 = require("../utils");
const CommentModel = config_1.connection.define(utils_1.TABLE_NAME.COMMENTS, {
    id: {
        type: sequelize_1.DataTypes.INTEGER.UNSIGNED,
        autoIncrement: true,
        primaryKey: true,
    },
    comment: {
        type: sequelize_1.DataTypes.STRING,
        allowNull: false,
    },
});
exports.default = CommentModel;
