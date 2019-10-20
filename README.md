# Private Push Notification (Android side)
 ![alt text](https://user-images.githubusercontent.com/36668681/67162771-5c314700-f374-11e9-9f15-dcf200ba69bc.gif)

## What is PPN?
Private Push Notification (PPN) is a project that you can connect your client app to your server app and send messages as push notification to your client app from server app.
For using of this project you have to run Server App on your server and import Client Library in your client app.
**At this moment just Android Library (Java) is now written for the client section**.
 you can start server app on any OS which has python3.
## Why i started this project?
This is a fun project for me, i started this project to learning socket programming in java and python and now i want to share it with you.
# Installing
At first you have to run server app, [check this link](https://github.com/behradrvb/ppn-server-python)
### Importing the module in your android app
You have to get this module and import in your project.

1. Open cmd/terminal and type `git clone https://github.com/behradrvb/ppn-client-android.git`
2. In Android Studio go to File -> New -> Import Module
3. Enter the address of directory of step 1
4. Add `implementation project(path: ':ppn-client-android')` in dependencies of build.gradle (app) 
    
### Connecting to server
After installing, you can connect to your server app.
This module saves SERVER data in shared preferences. So you have to run this command at least 1 time.

`PPN.init(getApplicationContext(),new Server(<server ip>,<server app port>,<session id>));`

After running server app, it returns ip and port. You can use them in above command.

Like this

>Server was launched successfully.(Sun Oct 20 18:30:37 2019)
>address:192.168.1.103:21000
>Waiting...

`session_id` is a id of your user, for example user_id, user_name and etc.
For sending push notifications you send this to server app to send a message to special client.

`PPN.init()` just initializes the data. doesn't establish the connection.

For establishing the connection, add this command in an activity, a service, a fragment or wherever you want.
Note that if you run this command in activity, after closing the activity, connection will be closed.

``` 
PPN.execute(getApplicationContext(), new PPNConnectionInterface() {
            @Override
            public void OnTry() {

            }

            @Override
            public void OnNewConnectionEstablished() {

            }

            @Override
            public void OnConnectionClosed() {

            }
        }, new PPNMessagesInterface() {
            @Override
            public void OnNewMessageReceived(Message msg) {

            }
        });
```
OnTry function is called when system tries to connect.

OnNewConnectionEstablished function is called when connection established.

OnConnectionClosed function is called called when Connection closed.

OnNewMessageReceived function is called when a message is given by server.

`msg` object contain parsed json of server message. you can use it.
like `msg.getTitle()` `msg.getImageURL()` `msg.getContent()` .
You can add other variables in server and client. Project has JavaDoc and it can helps you. 

Remember that you have call PPN.disconnect() function in OnDestroy() method of activity or service or etc.
```
@Override
    protected void onDestroy() {
        super.onDestroy();
        PPN.disconnect();
    }
```


If you want to use UI commands you have to use it in runOnUiThread like this
```
runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, msg.getContent(), Toast.LENGTH_SHORT).show();
                    }
                });
```

Even you can send a Notification in `OnNewMessageReceived()` function. Your title is `msg.getTitle()` and your content is `msg.getContent()` and your image_url is `msg.getImageURL()`.
Read more about Notifications in [this link](https://developer.android.com/training/notify-user/build-notification) .
