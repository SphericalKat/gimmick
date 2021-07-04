# Gimmick
A while back, I noticed that the official GitHub app doesn't have a feed similar to the one can find on the GitHub homepage. Naturally I had to make it myself.

This app is built using Jetpack Compose, Hilt, Kotlin Flow, Room and Retrofit.

# Building
You will need to create or obtain a `secrets.properties` file, located at the root of the project. This file will contain your GitHub OAuth app credentials. The format for this file is as given below:

```properties
CLIENT_ID="<client id>" // these quotes are important!
CLIENT_SECRET="<client secret>" // these quotes are important!
```

# Contribution
As always, I'm open to pull requests and issues. I will do my best to address these, but might not always be available thanks to work and other priorities.