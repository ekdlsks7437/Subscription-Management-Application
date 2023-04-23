require("dotenv").config();
const express = require("express");
const port = process.env.APP_PORT;
const userRouter = require("./router/userRouter");
const subscriptionRouter = require("./router/subscriptionRouter");
const settingRouter = require("./router/settingRouter");
require("./config/db");
const bodyParser = require("body-parser");

const app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use("/user", userRouter);
app.use("/subscription", subscriptionRouter);
app.use("/setting", settingRouter);

app.listen(port, () => {
  console.log(`Server running on port: ${port}`);
});
