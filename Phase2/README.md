Group_0668 README.MD
#PHASE 2:
# Steps in cloning:
1. Make a new folder in your file manager and call it something relevant to this project
2. Open Android Studio and select File -> New -> Project from Version Control -> Git
3. Use this link https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0668 and select the new folder that you have just made to clone it into.
Make sure you/the user have GameCentre as the project you/the user open not Phase 1 when you want to run the app.

Once there, run the app and select Android device (Pixel 2) that you would like to run the app on.

Once opened, the user will have the option to log in or sign up as a new user.

Log in or sign up as needed. (Password created must be longer than 6 characters)

Once in, you have the option of playing three games, checking the leaderboard, and signing out

HOW TO PLAY
# Sequencer :
Goal is to memorize the sequence on each round and tap corresponding tiles, sequence gets larger as rounds increase.
Further, autosave is implemented for sequencer.

# TicTacToe
Three types of playing options easy and hard where you play against an AI. Or you can play against another user on the same phone.
#SlidingTiles:
As per Phase1

#ScoreBoardNote:
The scores in the database are lower than actually attainable,
for SlidingTiles we did this for testing purposes, further the leaderboard only display the highest score of a person.

# leaderboard Note
Each games scores is calculated such that lower scores mean that you did better in the game, the goal was to resemble ranking.   

#**IMPORTANT NOTE:

-In case you want to see our Firebase Database. You can log on to the Firebase Console using following login credentials:
Email Address: csc207fall2018@gmail.com
Password: CSC207FALL
The password is all uppercase.
Steps after login:
1. Press Game Centre.
Under 'Develop'
2, Press Database
3. Make sure that 'Real Time Database' is selected at the top, this is info and dropdown is located near the large Database title.
4. To see the registered users, press authentication.
5. In case database has not updated, press the refresh button near the right corner of the screen.


- In case you find errors after cloning, try cleaning and rebuilding the project and then running it.

-You may receive warning about the 'use of deprecated API' in
FirstActivity after building the app. Just ignore it and run the application. We adopted the API to check the internet connection from another resource (citation included).




#PHASE 1
# Steps in cloning:
1. Make a new folder in your file manager and call it something relevant to this project
2. Open Android Studio and select File -> New -> Project from Version Control -> Git
3. Use this link https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0514 and select the new folder that you have just made to clone it into.
Make sure you/the user have GameCentre as the project you/the user open not Phase 1 when you want to run the app.


Once there, run the app and select Android device (Pixel 2) that you would like to run the app on.

Once opened, the user will have the option to log in or sign up as a new user.

Log in or sign up as needed. (Password created must be longer than 6 characters)

To play, press the New Game button, once pressed, you should be at a game settings screen where the user can pick the game complexity, and the undo option; 3 undos or unlimited undos, the type of the board (number tiles or image tiles), and the image to be chosen if the image tiles option was chosen.

If the user presses back, they can have the option to save the game, and load the saved game if they want to continue the game that they saved. However, autosaving is also turned on by default and the game automatically saves after every successful tap move by the user. So every user will his own customized board and settings saved even if the application is killed and the user logs in again after logging out.

There is also a leaderboard button, where if pressed, a leaderboard of the highest scores of the game and the highest scores of the user will be displayed. The leader board is very detailed with a local leaderboard of the currently logged in user for each board size and a global leaderboard for all the users of the game for each size.

Basically the scoring works according to the number of successful taps. The lower the number of taps it takes for the user to win the game, the better the score.

There is also a log out button, in case a different user wants to log into their account and play.
Pressing the log out button will return to the log in/sign up screen.

In order to login a new user, you must log out. Other wise the current signed in user will always be signed in until you log out.

#New Game Setup:
When you click on 'new game' in the game launcher activity, you will directed to the game setup. Here you can choose the board size, the number of undos and the type of the board (numbers or pictures) and then choose the a picture background for the board accordingly. Then you can like Start to launch the game.


# Extra Functionalities (Bonuses)

 - **(Bonus)** Real Time Firebase online database used to store the users and their settings Users can sign into their account across all devices with the application installed.

- **(Bonus)** Global scoreboard shows the top scores for all users across all devices. Updates Real Time.

- The undo button, where in settings, the user have either the choice for unlimited undos, or the default amount of undos, set at 3.

- **(Bonus)** Unlimited undos

- **(Bonus)** Background of Game Launch Centre; tapping on the background of the Game Launch Centre allows the user to change the color of the background of the Game Launch Centre.

- **(Bonus)** Image tiles, instead of having numbered tiles, the game can be be played instead with an image which will be "split up" by the application and then used as the tile background, instead of numbers (not hard-coded). If the user chooses the option 'Image tile' , he can then choose 1 out of the 3 given images for the background of the board.

--**(Bonus)** Your saved settings when you start a new game become your default settings whenever you start a new game. So every user's specific customized settings are checked by default when he wants to start a new game across multiple devices. The radio buttons for the settings in the settings activity are synced from the database, everytime the user logs in.

--**(Bonus)** The leaderboard is very detailed. It contains the currents user's best scores per board size and also the global best scores of all the users per each board size.

--**(Bonus)** The welcome screen after the user logs in is customized with the currently logged in user's name extracted Real Time from the Firebase Database.

--**(Bonus)** We also have a customized icon for our application. You can see it when you look for the application on the phone in the main menu.

--**(Bonus)** If the the device is not connected to the internet when the application is launched, the a pop will appear prompting the using to take him to the wifi connection settings or to quit the application. This is to ensure that the user is connected to the internet so that the data is in sync with the Firebase Database.

#**IMPORTANT NOTE:

-In case you want to see our Firebase Database. You can log on to the Firebase Console using following login credentials:
Email Address: csc207fall2018@gmail.com
Password: CSC207FALL
The password is all uppercase.
Steps after login:
1. Press sliding tiles.
Under 'Develop'
2, Press Database
3. Make sure that 'Real Time Database' is selected at the top, this is info and dropdown is located near the large Database title.
4. To see the registered users, press authentication.
5. In case database has not updated, press the refresh button near the right corner of the screen.


- In case you find errors after cloning, try cleaning and rebuilding the project and then running it.

-You may receive warning about the 'use of deprecated API' in
FirstActivity after building the app. Just ignore it and run the application. We adopted the API to check the internet connection from another resource (citation included).
