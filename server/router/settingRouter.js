const express = require('express');
const settingRouter = express.Router();
const subscriptionRouter = require("./subscriptionRouter");
const { Setting } = require('../models/Setting');
const { verifyAuthToken } = require("./AuthTokenHelper");

const errorHandler = (error, res) => {
    let returnError = {
        success: false,
        message: "internal server error",
        errorType: error.name,
    };

    if (error.name === "SequelizeUniqueConstraintError") {
        returnError = {
            success: false,
            message: "email already in use.",
            errorType: error.name,
        };
    } else {
        returnError = {
            success: false,
            message: error.message,
            errorType: error.name,
        };
    }
    res.status(500).json(returnError);
    res.end;
};

settingRouter.post("/create", async (req, res) => {
    const obj = req.body;
    let {
        userId,
        authToken,
        currency,
        notification_time,
    } = {
        userId: obj.user.userId,
        authToken: obj.authToken.token,
        currency: obj.userSettings.currency,
        notification_time: obj.userSettings.notificationTime,
    }

    try {
        // verify authtoken
        if (!verifyAuthToken(authToken)) {
            const failureResponse = {
                success: false,
                message: "Invalid authtoken",
            };
            res.status(401).write(JSON.stringify(failureResponse));
            res.end();
            return;
        }


        const targetSetting = await Setting.create({
            userId,
            currency,
            notification_time,
        });

        if (!targetSetting) {
            return res.status(404).send("User's Settings not found");
        }
        
        const succeededResponse = {
            success: true,
            message: "Successfully added the user's settings",
            targetSetting,
        };

        return res.send(succeededResponse);
    } catch (err) {
        errorHandler(err, res);
    }
});


settingRouter.get("/settings", async (req, res) => {
    const userId = req.header("userId");
    const authToken = req.header("authToken");

    try {
        // verify authtoken
        if (!verifyAuthToken(authToken)) {
            const failureResponse = {
                success: false,
                message: "Invalid authtoken",
            };
            res.status(401).write(JSON.stringify(failureResponse));
            res.end();
            return;
        }


        const targetSetting = await Setting.findOne({
            where: { userId: userId },
        });

        if (!targetSetting) {
            return res.status(404).send("User's Setting not found");
        }

        const succeededResponse = {
            success: true,
            message: "Successfully got the user's setting",
            targetSetting,
        };

        return res.send(succeededResponse);
    } catch (err) {
        errorHandler(err, res);
    }
});

settingRouter.post("/update", async (req, res) => {
    const obj = req.body;
    let {
        userId,
        authToken,
    } = {
        userId: obj.user.userId,
        authToken: obj.authToken.token,
    }

    try {
        // verify authtoken
        if (!verifyAuthToken(authToken)) {
            const failureResponse = {
                success: false,
                message: "Invalid authtoken",
            };
            res.status(401).write(JSON.stringify(failureResponse));
            res.end();
            return;
        }

        // destructure the request body
        const currency = req.body.userSettings.currency;
        const notification_time = req.body.userSettings.notificationTime;

        // get the target setting
        const targetSetting = await Setting.findOne({
            where: { userId: userId },
        });

        // check if the setting exists
        if (!targetSetting) {
            return res.status(404).send("User's Setting not found");
        }

        // create the updated setting json
        let updatedSettingData = {
            currency: currency,
            notification_time: notification_time
        };

        // update the setting
        const updatedSetting = await targetSetting.update(updatedSettingData);

        // return the updated setting
        const succeededResponse = {
            success: true,
            message:
                "Successfully updated the user's setting",
            authToken,
            updatedSetting,
            targetSetting : updatedSetting,
        };

        return res.send(succeededResponse);
    } catch (err) {
        errorHandler(err, res);
    }
});

module.exports = settingRouter;
