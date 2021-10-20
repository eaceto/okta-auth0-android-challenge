# Android Technical Exercise

## Objective
Write an Android app that features user authentication to consume our Coffee API. You will be provided with a starting Android app project and a [Node API](rest/README.md) to run locally.

- Without using an Auth related SDK, authenticate the user through our Universal Login page using the Authorization Code + PKCE flow. Display information about the logged in user in the `MainActivity`.
- Retrieve and show the coffees known by the provided Coffee API. Clicking an item from the list should prompt the user to share the selected coffee information with an external app (i.e. mail, notes, etc).
- Add tests and documentation.

### Bonus
- Avoid showing `LoginActivity` if the user is already logged in and let them log out from the provided button.
- Design two different item views depending on whether the coffee type is “hot” or “iced”.
- Use Kotlin Coroutines to run background work.

## Hints
- The Coffee API only serves requests to those with the right level of access. Make sure to check the included README in [the API project](https://github.com/auth0-samples/android-exercise/tree/main/rest).
- The `Utils.kt` file contains functions that can help obtain values for the PKCE flow.
- The logged-in user details can be obtained by decoding the ID token or invoking the `/userinfo` endpoint from the Authentication API using the Access Token.

## Resources
- Authorization Code + PKCE flow: https://auth0.com/docs/login/authentication/add-login-using-the-authorization-code-flow-with-pkce.
- Authentication API: https://auth0.com/docs/api/authentication 
- ID token claims: https://auth0.com/docs/security/tokens/id-tokens/id-token-structure

# LICENSE 
MIT - Auth0 2021
