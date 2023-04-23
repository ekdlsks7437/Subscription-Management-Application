const express = require('express');
const userRouter = express.Router();
const { User } = require('../models/User');
const crypto = require('crypto');
const { createAuthToken, RemoveAllAuthTokenFromGivenUSer } = require("./AuthTokenHelper");
const { hashPassword, verifyPassword, validateEmail, removeInputSpace } = require("../utils/utils");

const errorHandler = (error, res) => {
    let returnError = { "success": false, "message": "internal server error", "errorType": error.name };

    if (error.name === "SequelizeUniqueConstraintError") {
        returnError = { "success": false, "message": "email already in use.", "errorType": error.name };
    } else {
        returnError = { "success": false, "message": error.message, "errorType": error.name };
    }
    res.status(500).json(returnError);
    res.end;
}

userRouter.get('/:id', async (req, res) => {
    const id = req.params.id;
    try {
        const user = await User.findByPk(id);
        if (!user) {
            return res.status(404).send('User not found');
        }
        return res.send(user);
    } catch (err) {
        console.error(err);
        return res.status(500).send('Error retrieving user');
    }
});


userRouter.post('/create', async (req, res) => {

    const obj = req.body;
    // remove space
    let { email, password } = removeInputSpace(obj.email, obj.password);
    try {

        if (!validateEmail(email)) {
            const error = new Error("Invalid Email");
            error.name = "invalidEmail"
            throw error;
        }

        // hash password
        password = hashPassword(password);

        // create the user in the database.
        const newUser = await User.create({ email, password });
        const userId = newUser.id;
        const authToken = await createAuthToken(userId);
        const returnUser = { email, userId };
        const succeededResponse = { "success": true, "message": "Successully Register", authToken, returnUser };

        res.status(200).write(JSON.stringify(succeededResponse)); //write a response to the client
        res.end(); //end the response
    } catch (error) {
        errorHandler(error, res);
    }
});

userRouter.post('/signin', async (req, res) => {
    const obj = req.body;

    // remove space
    let { email, password } = removeInputSpace(obj.email, obj.password);

    try {

        if (!validateEmail(email)) {
            const error = new Error("Invalid Email");
            error.name = "invalidEmail"
            throw error;
        }

        // get user by email
        const user = await User.findOne({ where: { email: email } });

        // check email & passsword
        if (!user || !verifyPassword(password, user.get("password"))) {
            const error = new Error("Incorrect email or password");
            error.name = "InvalidSignInInfo";
            throw error;
        }

        const userId = user.id;
        const authToken = await createAuthToken(userId);
        const returnUser = { email, userId };
        const succeededResponse = { "success": true, "message": "Successully Sign In", authToken, returnUser };

        res.status(200).write(JSON.stringify(succeededResponse)); //write a response to the client
        res.end(); //end the response
    } catch (error) {
        errorHandler(error, res);
    }
});

userRouter.post('/signout', async (req, res) => {
    //TODO: double check this destructuring is okay
    // check what format frontend will send in body
    const { email, userId } = req.body;

    try {
        if (!RemoveAllAuthTokenFromGivenUSer(userId)) {
            const error = new Error("Error: Internal AuthToken Error");
            error.name = "AuthtokenError"
            throw error;
        }

        const succeededResponse = { "success": true, "message": "Successully Sign Out" };

        res.status(200).write(JSON.stringify(succeededResponse)); //write a response to the client
        res.end(); //end the response
    } catch (error) {
        errorHandler(error, res);
    }
});





module.exports = userRouter;
