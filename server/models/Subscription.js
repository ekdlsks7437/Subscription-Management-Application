const { sequelize } = require("../config/db");
const { Model, DataTypes } = require("sequelize");

const Subscription = sequelize.define("Subscription", {
  id: {
    type: DataTypes.INTEGER,
    autoIncrement: true,
    primaryKey: true,
  },
  userId: {
    type: DataTypes.STRING,
    foreignKey: true,
  },
  name: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: true,
  },
  cost: {
    type: DataTypes.DOUBLE,
    allowNull: false,
  },
  total_spent: {
    type: DataTypes.DOUBLE,
    allowNull: false,
  },
  starting_date: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  renewal_period: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  renewal_type: {
    type: DataTypes.ENUM("MONTHLY", "YEARLY", "CUSTOM"),
    defaultValue: "MONTHLY",
  },
  renewal_date: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  notificationID: {
    type: DataTypes.STRING,
    allowNull: false,
    unique: true,
  },
  spentYTD: {
    type: DataTypes.DOUBLE,
    allowNull: false,
  },
});

module.exports = { Subscription };
