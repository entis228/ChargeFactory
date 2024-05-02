## Before running server please make sure that you have installed:
 
 - java version 17
 - maven
 - docker
 - docker-compose

If you are on Linux, run `start-server.sh` script
If you are on Windows, type commands from file `start-server.bat` in command-line inside current folder

Open browser and type into address bar [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

In the main page you need to pick `POST /api/users` and then ***Try it out*** button.
Enter the email, password and your name. Now you have registered your user account.
To sign in you should choose `POST /api/token` request then enter your e-mail and password.
If email and password is correct you'll get access token and refresh token.
Access token need for sign in and refresh token need for update your access token.
Then press authorise button on the top of the page and enter your access token.
Keep in mind that access token expires in about 10 minutes and refresh token about 3 days.
If you have access token expired, choose `POST /api/token/refresh` and enter your refresh token.
When you use your refresh token it becomes expired, so you need to save your new refresh token which will be in response of `POST /api/token/refresh`.
If you have both tokens expired, just make another sign in request in `POST /api/token`.
When you sign in, you can view you profile by `GET /api/users/current request`.
You can change your profile by `PATCH /api/users/current request`.
Be aware when you edit your email address, because it can be another in sign in request if you change it.

There are three types of users: Regular, Admin and Owner.

### List of requests available for regular users:

> 1. POST /api/users/current/add (top up balance of account)
> 2. GET  /api/users/current 
> 3. GET /api/users/current/charges (you can view all your charge events near all charging stations)
> 4. PATCH /api/users/current 
> 5. PATCH /api/users/current/password (change password)

### List of requests available for admins:

> 1. All regular user requests 
> 2. GET /api/users (view all registered users)
> 3. PATCH /api/users/id={id}/status (block or unblock regular users)
> 4. PATCH /api/users/id={id}/password (set new password to user if it lost)
> 5. GET /api/users/id={id} (view info about user by id)
> 6. GET /api/users/email={email} (view info about user by email)
> 7. GET /api/users/id={id}/charges (view all charge events of user)
> 8. GET /api/stations (view all available charge stations)
> 9. GET /api/stations/{id} (view charge station's info)
> 10. POST /api/stations (register new charge station)
> 11. PATCH /api/stations/{id} (edit name and state of station)
> 12. DELETE /api/stations/{id} (delete station)

### List of requests available for owners:

> 1. All admin requests 
> 2. POST /api/users/admins (create new admin account)
> 3. POST /api/users/owners (create new owner account)
> 4. DELETE /api/users/id={id} (delete account by id)

### List of requests available for all and unauthorised users:

> 1. POST /api/users 
> 2. POST /api/token 
> 3. POST /api/token/refresh