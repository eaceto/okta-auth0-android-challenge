require('dotenv').config()
const coffeesData = require('./coffees.json')
const express = require("express");
const { auth, requiredScopes } = require('express-oauth2-jwt-bearer');
const morganBody = require("morgan-body");
const morgan = require("morgan");

const app = express();

app.get("/", (req, res, next) => {
  res.json("Brewing coffees...")
})

morgan.token('body', req => {
  return JSON.stringify(req.body)
})
morgan.token('req-headers', function(req,res){
  return JSON.stringify(req.headers)
 })
 
app.use(morgan(':method :url :status :body :req-headers'));
morganBody(app);


app.use(auth());

app.get("/who", (req, res, next) => {
  res.json(`You are '${req.user.sub}'.`)
})
app.get("/coffees", requiredScopes("read:coffees"), (req, res, next) => {
  res.json(coffeesData);
 });
app.use(function(err, req, res, next) {
  if (err){
    res.status(err.status || err.statusCode).send({code: err.code || err.error, description: err.message});
  }
  next();
});
app.listen(3000, () => {
  console.log("API running on port 3000");
  console.log(`- Audience: ${process.env.AUDIENCE}`)
  console.log(`- Domain: ${process.env.ISSUER_BASE_URL}`)
 });
 