# Coffees REST API

This is a simple Express API that serves different coffees.

## Configuration
In order to run this API you need to have installed Node JS. You can download it from [here](https://nodejs.org/en/download/).

Install the project dependencies:

```bash
# ./rest
npm install
```

Create an API in the Auth0 dashboard selecting _RS256_ as the signing algorithm. You can add the API permissions that you require. Take the API identifier and your account's domain and put them in the `.env` file. 

```go
AUDIENCE={API_IDENTIFIER}
ISSUER_BASE_URL={AUTH0_DOMAIN}
```
**Hint:** The issuer base URL starts with `https://`.

You can read more about APIs in the following articles:
- https://auth0.com/docs/get-started/set-up-apis.
- https://auth0.com/docs/configure/apis/add-api-permissions 


## Usage

Run the API:

```bash
# ./rest
node index.js

# API running on port 3000
# - Audience: my-api
# - Domain: https://johndoe.auth0.com
```

The service will be available in `localhost:3000`.

### Authorization

To prevent unauthorized access to the API, some of their endpoints require authentication. Use the [bearer scheme](https://swagger.io/docs/specification/authentication/bearer-authentication/) to attach a token to the request.

```js
Authorization: Bearer {TOKEN_GOES_HERE}
```

### Errors
- Missing authentication

```json
{ 
  "code": "invalid_request",
  "description": "Bearer token is missing"
}
```

- Missing authorization

```json
{ 
  "code": "insufficient_scope",
  "description": "Insufficient Scope"
}
```

- Invalid token

```json
{
  "code": "invalid_token",
  "description": "signature verification failed"
}
```

## Endpoints

### 🔓 GET /
Returns a text message.

```json
"Brewing coffees..."
```

### 🔒 GET /who
Returns the identifier of the current user. 

Requires authentication.

```json
"You are 'QsFfeOaZphFGGrLnyBFBhsD74IYycA8b@clients'."
```

### 🔐 GET /coffees
Returns the list of coffees. 

Requires authentication and a scope of `read:coffees`.

```json
[
  {
    "title": "Sweet",
    "type": "hot",
    "description": "What makes this coffee special",
    "ingredients": [
      "Coffee",
      "Sugar"
    ],
    "id": 1
  }
  // ...
]
```

## LICENSE
MIT - Auth0 2021