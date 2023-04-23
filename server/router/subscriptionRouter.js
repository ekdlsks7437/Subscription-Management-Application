const express = require("express");
const subscriptionRouter = express.Router();
const { Subscription } = require("../models/Subscription");
const crypto = require("crypto");
const { verifyAuthToken } = require("./AuthTokenHelper");
const date = require("date-and-time");

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

subscriptionRouter.get("/subscriptions", async (req, res) => {
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

    const subscriptions = await Subscription.findAll({
      where: { userId: userId },
    });

    //return the whole response object of success, message
    const succeededResponse = {
      success: true,
      message:
        "Successfully got all " + subscriptions.length + " subscriptions",
      subscriptions,
    };

    if (!subscriptions || subscriptions.length == 0) {
      // if the person doesn't have any subscriptions like when they register we will just send back
      // a 200 and a message to catch on the front end of "no subscriptions"
      succeededResponse.message = "no subscriptions";
      succeededResponse.success = false;
      return res.status(200).send(succeededResponse);
    }


    return res.send(succeededResponse);
  } catch (err) {
    console.error(err);
    return res.status(500).send("Error retrieving subscription(s)");
  }
});

subscriptionRouter.post("/create", async (req, res) => {
  const obj = req.body;
  const authToken = req.body.authToken;
  let {
    userId,
    name,
    cost,
    total_spent,
    starting_date,
    renewal_period,
    renewal_type,
    renewal_date,
    spentYTD,
  } = {
    userId: obj.subscription.userId,
    name: obj.subscription.name,
    cost: obj.subscription.cost,
    total_spent: obj.subscription.totalSpent,
    starting_date: obj.subscription.startingDate,
    renewal_period: obj.subscription.renewalPeriod,
    renewal_type: obj.subscription.renewalType.period,
    renewal_date: obj.subscription.renewalDate,
    spentYTD: obj.subscription.spentYTD,
  };

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

    // create the subscription in the database.
    const notificationID = crypto.randomUUID();
    const returnSubscription = await Subscription.create({
      userId,
      name,
      cost,
      total_spent,
      starting_date,
      renewal_period,
      renewal_type,
      renewal_date,
      notificationID,
      spentYTD,
    });

    const succeededResponse = {
      success: true,
      message: "Successfully entered new subscription",
      // I don't think the response needs a authToken from this since it is just checking it.
      // authToken,
      returnSubscription,
    };

    res.status(200).write(JSON.stringify(succeededResponse)); //write a response to the client
    res.end(); //end the response
  } catch (error) {
    errorHandler(error, res);
  }
});

subscriptionRouter.post("/delete", async (req, res) => {
  const notificationID = req.body.id;
  const authToken = req.body.authToken;

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

    // delete subscription
    const deletedSubscription = await Subscription.destroy({
      where: { notificationID: notificationID },
    });

    const succeededResponse = {
      success: true,
      message: "Successfully deleted subscription",
      // authToken,
      deletedSubscription,
    };

    res.status(200).write(JSON.stringify(succeededResponse));
    res.end();
  } catch (error) {
    errorHandler(error, res);
  }
});

/**
Endpoint for updating a subscription in the database
@param {Object} req - The HTTP request object
@param {Object} res - The HTTP response object
@returns {Object} - Returns a JSON object with the updated subscription and success status
**/
subscriptionRouter.post("/update", async (req, res) => {
  const obj = req.body;
  const userId = obj.user.userId;
  const authToken = obj.authToken;
  const notificationID = obj.subscription.notificationID;

  // Extract subscription properties from request body
  let {
    updatedName,
    updatedCost,
    updatedStarting_date,
    updatedRenewal_period,
    updatedRenewal_type,
    updatedRenewal_date,
  } = {
    updatedName: obj.subscription.name,
    updatedCost: obj.subscription.cost,
    updatedStarting_date: obj.subscription.startingDate,
    updatedRenewal_period: obj.subscription.renewalPeriod,
    updatedRenewal_type: obj.subscription.renewalType.period,
    updatedRenewal_date: obj.subscription.renewalDate,
  };

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

    // edit subscription
    // Check if notificationId is provided since the front end doesn't have the subscriptionId.
    if (!notificationID) {
      // no id sent
      const failureResponse = {
        success: false,
        message: "notificationId not provided",
      };
      res.status(400).write(JSON.stringify(failureResponse));
      res.end();
      return;
    }

    // Get target subscription from database
    const targetSubscription = await Subscription.findOne({
      where: { userId: userId, notificationID: notificationID },
    });

    // If subscription not found
    if (!targetSubscription) {
      const failureResponse = {
        success: false,
        message: "SubscriptionId not found",
      };
      res.status(404).write(JSON.stringify(failureResponse));
      res.end();
      return;
    }

    // Create subscription JSON object with updated properties
    let subscriptionJSON = {
      name: updatedName,
      cost: updatedCost,
      starting_date: updatedStarting_date,
      renewal_period: updatedRenewal_period,
      renewal_type: updatedRenewal_type,
      renewal_date: updatedRenewal_date,
    };

    // Update subscription
    const updatedSubscription = await targetSubscription.update(
      subscriptionJSON
    );

    // Send success response with updated subscription
    const succeededResponse = {
      success: true,
      message: "Successfully updated subscription",
      // authToken,
      updatedSubscription,
    };

    res.status(200).write(JSON.stringify(succeededResponse));
    res.end();
  } catch (error) {
    errorHandler(error, res);
  }
});

/**
 * Takes in a notification id and updates the notification date, Spent YTD, Total spent then sends it back to the client
 * and saves the updated notification to the database. This will be called when the notification pops up on the users device,
 * and will call this function to get the updated subscription.
 */
subscriptionRouter.post("/updateNotification", async (req, res) => {
  // get userId and notificationID from request body
  const userId = req.body.userId;
  const notificationID = req.body.notificationID;

  try {
    // get the corresponding subscription
    const subscription = await Subscription.findOne({
      where: { notificationID: notificationID },
    });

    // verify user
    if (userId != subscription.userId) {
      const failureResponse = {
        success: false,
        message: "Invalid user",
      };
      res.status(401).write(JSON.stringify(failureResponse));
      res.end();
      return;
    }

    // update the total spent
    const currentSpent = subscription.total_spent;
    const updatedSpent = currentSpent + subscription.cost;

    // update spent YTD
    const currentSpentYTD = subscription.spentYTD;
    const updatedSpentYTD = currentSpentYTD + subscription.cost;

    // update renewal date
    const renewalPeriod = subscription.renewal_period;
    const currentRenewalDate = new Date(subscription.renewal_date);

    // get renewal times
    const indexD = renewalPeriod.indexOf("D");
    const indexM = renewalPeriod.indexOf("M");
    const indexY = renewalPeriod.indexOf("Y");

    let day = renewalPeriod.substring(0, indexD);
    let month = renewalPeriod.substring(indexD + 1, indexM);
    let year = renewalPeriod.substring(indexM + 1, indexY);

    let updatedRenewalDate = date.addDays(currentRenewalDate, day++);
    updatedRenewalDate = date.addMonths(updatedRenewalDate, month++);
    updatedRenewalDate = date.addYears(updatedRenewalDate, year++);
    updatedRenewalDate = date.format(updatedRenewalDate, "YYYY-MM-DDTHH:mm");

    // return updated subscription
    let subscriptionJSON = {
      total_spent: updatedSpent,
      renewal_date: updatedRenewalDate,
      spentYTD: updatedSpentYTD,
    };

    const updatedSubscription = await Subscription.update(subscriptionJSON, {
      where: { notificationID: notificationID },
    });

    // id not in table
    if (updatedSubscription[0] == 0) {
      const failureResponse = {
        success: false,
        message: "NotificationID not found",
      };
      res.status(404).write(JSON.stringify(failureResponse));
      res.end();
      return;
    }

    const succeededResponse = {
      success: true,
      message: "Successfully updated subscription",
      //authToken,
      updatedSubscription,
    };
    res.status(200).write(JSON.stringify(succeededResponse));
    res.end();
  } catch (error) {
    console.log(error);
    errorHandler(error, res);
  }
});

module.exports = subscriptionRouter;
