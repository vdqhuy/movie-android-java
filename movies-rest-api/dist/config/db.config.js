"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const sequelize_1 = require("sequelize");
const connection = new sequelize_1.Sequelize({
    host: "localhost",
    dialect: "mysql",
    username: "root",
    password: "",
    database: "real_film",
    logging: false,
});
connection
    .authenticate()
    .then(() => {
    console.log("Connection has been established successfully.");
})
    .catch((error) => {
    console.error("Unable to connect to the database: ", error);
});
exports.default = connection;
