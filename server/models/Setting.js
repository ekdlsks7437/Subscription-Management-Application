const { sequelize } = require("../config/db");
const { Model, DataTypes } = require('sequelize');

const Setting = sequelize.define('Setting', {
    userId: {
        type: DataTypes.STRING,
        primaryKey: true,
        allowNull: false,
        unique: true
    },
    currency: {
        type: DataTypes.STRING,
        allowNull: false,
        defaultValue: 'USD' // set default value for currency column
    },
    notification_time: {
        type: DataTypes.STRING,
        allowNull: false,
        defaultValue: '10:00 AM' // set default value for notification_time column
    }
});


module.exports = { Setting };