const { Sequelize } = require('sequelize');
const { Model, DataTypes } = require('sequelize');

//TODO: can't open the database.sqlite in linux 
// have to move it to window and open it there
const sequelize = new Sequelize({
    dialect: 'sqlite',
    storage: './config/database.sqlite'
});



(async () => {
    try {
        await sequelize.authenticate();
        console.log('Connection has been established successfully.');

        await sequelize.sync({ force: true });
        console.log('All models were synchronized successfully.');
    } catch (error) {
        console.error('Unable to connect to the database:', error);
    }
})();


module.exports = { sequelize };