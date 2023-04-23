const { sequelize } = require("../config/db");
const { Model, DataTypes } = require('sequelize');

const AuthToken = sequelize.define('AuthToken', {
    token: {
        type: DataTypes.STRING,
        primaryKey: true,
        allowNull: false,
        unique: true
    },
    userId: {
        type: DataTypes.STRING,
        allowNull: false
    },
    expireDate: {
        type: DataTypes.STRING,
        allowNull: false
    }
});

module.exports = { AuthToken };