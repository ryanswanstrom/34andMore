*Note: Google has shut down the Shopping API, so this no longer works.*

# About the App
This is a app that performs a search against the Google product API.
It sends a query and then returns the results in a different view
than Google Shopping.

# Deploy the app
1. heroku create 34andmore
1. git push heroku master
1. heroku config:add MONGO_URL=
1. heroku config:add MONGO_PORT=
1. heroku config:add MONGO_NAME=
1. heroku config:add MONGO_USER=
1. heroku config:add MONGO_PW=
1. heroku config:add GOOGLE_API_KEY=
