import { Sequelize } from "sequelize";

const connection = new Sequelize({
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

export default connection;
