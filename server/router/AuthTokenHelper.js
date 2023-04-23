
const express = require("express");
const authTokenRouter = express.Router();
const crypto = require("crypto");
const { AuthToken } = require("../models/Authtoken");

// how long the authToken is valid for
const oneMonthEpocTime = 2629800000;

/**
 * Creates a authToken for a given userId and adds it to the DB
 *
 * @param userId the unique user id to be associated with that user.
 * @returns the created AuthToken. Null on error.
 */
async function createAuthToken(userId) {
  const token = crypto.randomUUID();
  const expireDate = Date.now() + oneMonthEpocTime;
  try {
    const authToken = await AuthToken.create({ userId, token, expireDate });
    return authToken;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/***
 * Gets the authToken from the database. On error returns null.
 *
 * @param authTokenId the authToken.token to be checked
 * @returns the authToken from the db.
 */
async function get(token) {
  try {
    const authToken = await AuthToken.findByPk(token);
    if (authToken === null) {
      return null;
    }
    return (authToken);
  } catch (err) {
    console.error(err);
    // I guess since this should only be used through the verifyAuthToken function, we can just
    // return null on error, or no AuthToken existing?
    // TODO: maybe make this throw an error like "throw 500" and then catch it in the verify
    // method to then throw it from there up to the calling function.
    return null;
  }
}

/**
 * Removes the given authtoken from the DB
 *
 * @param authTokenId unique id for authtoken to be removed
 * @returns true if it succeeded, false if it failed
 */
async function removeAuthToken(authTokenId) {
  try {
    //TODO: I think this will work, but I'm not sure.
    const authToken = await AuthToken.destroy({
      where: {
        token: authTokenId,
      },
    });
    if (!authToken) {
      return false;
    }
    return true;
  } catch (err) {
    console.error(err);
    // if it  is just deleting the authToken, the error is it probably already is deleted. or internal server error.
    return false;
  }
}

/**
 * Removes all of the authTokens for a given user id.
 * @param userId unique id for a given user.
 * @returns true if it succeeded, false if it failed.
 */
async function RemoveAllAuthTokenFromGivenUSer(userId) {
  try {
    //TODO: I think this will work, but I'm not sure.
    const authToken = await AuthToken.destroy({
      where: {
        userId: userId,
      },
    });
    if (!authToken) {
      return false;
    }
    return true;
  } catch (err) {
    console.error(err);
    // if it  is just deleting the authToken, the error is it probably already is deleted. or internal server error.
    return false;
  }
}

/**
 * Verifies if a AuthToken is within the given timeframe
 *
 * @param authToken authToken to be verified as still usable.
 * @returns boolean true if AuthToken is valid, or false if it is not valid.
 */
async function verifyAuthToken(authToken) {
  var tempToken = authToken.token;
  var tempAuthToken = await get(tempToken);
  if (tempAuthToken === null) {
    return false;
  }
  if (Date.now() < parseInt(tempAuthToken.expireDate)) {
    return true;
  }
  return false;
}

module.exports = {
  createAuthToken,
  removeAuthToken,
  get,
  verifyAuthToken,
  RemoveAllAuthTokenFromGivenUSer,
}
