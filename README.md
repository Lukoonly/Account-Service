# About
REST API for accounting for company employees
## Technologies
- Java
- Spring Boot
- Spring Security
- Hibernate
- H2 Database
## Advantages
- Secure connection via HTTPS protocol
- Differentiation of user rights: administrator, accountant, security auditor and other employee
- Blocking a user after five failed login attempts
- Logging of the most important security-related events
- Possibility of accounting
### Endpoints and access levels
|endponit                       | Anonymous | User |Accountant|Administrator|Auditor|
|:--------:                     |:--------: |:----:|:--------:|:-----------:|:-----:|
|```POST api/auth/signup```     | +         | +    |+         | +           |+      |
|```POST api/auth/changepass``` |           | +    |+         | +           |  -    |       
|```GET api/empl/payment```     | -         | +    |+         | -           |  -    |   
|```POST api/acct/payments```   | -         |  -   |+         | -           |  -    |
|```PUT api/acct/payments```    | -         |  -   |+         | -           |  -    |
|```GET api/admin/user```       | -         |  -   |-         |        +    |  -    |
|```DELETE api/admin/user```    | -         |  -   |-         |        +    |  -    |
|```PUT api/admin/user/role```  | -         |  -   |-         |        +    |  -    |
|```PUT api/admin/user/access```| -         |  -   |-         |        +    |  -    |
|```GET api/security/events```  | -         |  -   |-         |-            |+      |
# Examples
## User registration
#### ```POST api/auth/signup``` request

Request body:
```json
{
   "name": "Admin",
   "lastname": "Admin",
   "email": "admin@acme.com",
   "password": "secret"
}
```
Response body:
```json
{
    "id": 1,
    "name": "Admin",
    "lastname": "Admin",
    "email": "admin@acme.com",
    "roles": [
        "ROLE_ADMINISTRATOR"
    ]
}
```

**The user who registers first is automatically assigned as an administrator**

## Role Assignment
#### ```PUT api/admin/user/role``` request with administrator authorization

Request body:
```json
{
   "user": "accountant@acme.com",
   "role": "ACCOUNTANT",
   "operation": "GRANT"
}
```
Response body:
```json
{
    "id": 3,
    "name": "accountant",
    "lastname": "accountant",
    "email": "accountant@acme.com",
    "roles": [
        "ROLE_ACCOUNTANT",
        "ROLE_USER"
    ]
}
```
**To remove a role, use the operation REMOVE**

## Loading payments
#### ```POST api/acct/payments``` request with an accountant's authorization

Request body:
```json
[
    {
        "employee": "user@acme.com",
        "period": "01-2021",
        "salary": 123457
    },
    {
        "employee": "user@acme.com",
        "period": "02-2021",
        "salary": 123456
    },
    {
        "employee": "user@acme.com",
        "period": "03-2021",
        "salary": 123456
    }
]
```
Response body:
```json
{
    "status": "Added successfully!"
}
```
**Period must be unique**

## View payment
#### ```GET api/empl/payment?period=01-2021``` request with user authorization

Response body:
```json
{
   "name": "User",
   "lastname": "User",
   "period": "January-2021",
   "salary": "123456 dollar(s) 56 cent(s)"
}
```


## View payments
#### ```GET api/empl/payment``` request with user authorization

Response body:
```json
[
    {
       "name": "User",
       "lastname": "User",
       "period": "March-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    },
    {
       "name": "User",
       "lastname": "User",
       "period": "February-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    },
    {
       "name": "User",
       "lastname": "User",
       "period": "January-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    }
]
```
## Lock/unlock user
#### ```PUT api/admin/user/access``` request with administrator authorization

Request body:
```json
{
   "user": "user@acme.com",
   "operation": "LOCK" //UNLOCK to unlock
}
```
Response body:
```json
{
    "status": "User user@acme.com locked!"
}
```
**Admin can't be blocked**

## View log
#### ```GET api/auth/signup``` request with auditor authorization

Response body:
```json
[
{
  "date" : "<date>",
  "action" : "CREATE_USER",
  "subject" : "Anonymous", \\ A User is not defined, fill with Anonymous
  "object" : "johndoe@acme.com",
  "path" : "/api/auth/signup"
}, {
  "date" : "<date>",
  "action" : "LOGIN_FAILED",
  "subject" : "maxmustermann@acme.com",
  "object" : "/api/empl/payment", \\ the endpoint where the event occurred
  "path" : "/api/empl/payment"
}, {
  "date" : "<date>",
  "action" : "GRANT_ROLE",
  "subject" : "johndoe@acme.com",
  "object" : "Grant role ACCOUNTANT to petrpetrov@acme.com",
  "path" : "/api/admin/user/role"
}, {
  "date" : "<date>",
  "action" : "REMOVE_ROLE",
  "subject" : "johndoe@acme.com",
  "object" : "Remove role ACCOUNTANT from petrpetrov@acme.com",
  "path" : "/api/admin/user/role"
}, {
  "date" : "<date>",
  "action" : "DELETE_USER",
  "subject" : "johndoe@acme.com",
  "object" : "petrpetrov@acme.com",
  "path" : "/api/admin/user"
}, {
  "date" : "<date>",
  "action" : "CHANGE_PASSWORD",
  "subject" : "johndoe@acme.com",
  "object" : "johndoe@acme.com",
  "path" : "/api/auth/changepass"
}, {
  "date" : "<date>",
  "action" : "ACCESS_DENIED",
  "subject" : "johndoe@acme.com",
  "object" : "/api/acct/payments", \\ the endpoint where the event occurred
  "path" : "/api/acct/payments"
}, {
  "date" : "<date>",
  "action" : "BRUTE_FORCE",
  "subject" : "maxmustermann@acme.com",
  "object" : "/api/empl/payment", \\ the endpoint where the event occurred
  "path" : "/api/empl/payment"
}, {
  "date" : "<date>",
  "action" : "LOCK_USER",
  "subject" : "maxmustermann@acme.com",
  "object" : "Lock user maxmustermann@acme.com",
  "path" : "/api/empl/payment" \\ the endpoint where the lock occurred
}, {
  "date" : "<date>",
  "action" : "UNLOCK_USER",
  "subject" : "johndoe@acme.com",
  "object" : "Unlock user maxmustermann@acme.com",
  "path" : "/api/admin/user/access"
}
]
```

### List of logged events
|                              Description                              |          Event          | 
|:---------------------------------------------------------------------:|:-----------------------:|
|                     User successfully registered                      |    ```CREATE_USER```    |
|                  User successfully changed password                   |  ```CHANGE_PASSWORD```  |      
|     The user is trying to access a resource without access rights     |   ```ACCESS_DENIED```   |  
|                         Failed Authentication                         |   ```LOGIN_FAILED```    |
|                    The role is granted to the user                    |    ```GRANT_ROLE```     | 
|                         Role has been revoked                         |    ```REMOVE_ROLE```    | 
|                          Admin blocked user                           |     ```LOCK_USER```     | 
|                          Admin unlocked user                          |    ```UNLOCK_USER```    | 
|                          Admin deleted user                           |    ```DELETE_USER```    | 
|     The user was blocked due to suspicion of a brute-force attack     |    ```BRUTE_FORCE```    | 