
# GameCenter
Android application with three games where users can sign in and compare their scores via the leaderboard:    

## Sequencer
- Goal is to memorize the sequence on each round and tap corresponding tiles, sequence gets larger as rounds increase.
Further, autosave is implemented for sequencer.

## TicTacToe
- Three types of playing options easy and hard where you play against an AI. Or you can play against another user on the same phone.

## SlidingTiles
- Slide the tiles until they are in sequential order.

# How to use
Currently need Android Studio.

Once the application is opened (the path should be "GameCentre/") on Android Studio, run the app and select Android device (Pixel 2) that you would like to run the app on.

Once opened, the user will have the option to log in or sign up as a new user.

Log in or sign up as needed. (Password created must be longer than 6 characters)

Note:
- If necessary import project from gradle.
If "sync failed" error occurs in the build tab, click on "Re-download dependencies and sync project".

## Functionalities:

- Real Time Firebase online database used to store the users and their settings Users can sign into their account across all devices with the application installed.

- Global scoreboard shows the top scores for all users across all devices. Updates Real Time.

- The undo button, where in settings, the user have either the choice for unlimited undos, or the default amount of undos, set at 3.

- Unlimited undos

- Background of Game Launch Centre; tapping on the background of the Game Launch Centre allows the user to change the color of the background of the Game Launch Centre.

- Image tiles, instead of having numbered tiles, the game can be be played instead with an image which will be "split up" by the application and then used as the tile background, instead of numbers (not hard-coded). If the user chooses the option 'Image tile' , he can then choose 1 out of the 3 given images for the background of the board.

- Your saved settings when you start a new game become your default settings whenever you start a new game. So every user's specific customized settings are checked by default when he wants to start a new game across multiple devices. The radio buttons for the settings in the settings activity are synced from the database, every time the user logs in.

- The leaderboard is very detailed. It contains the currents user's best scores per board size and also the global best scores of all the users per each board size.

- The welcome screen after the user logs in is customized with the currently logged in user's name extracted Real Time from the Firebase Database.

- We also have a customized icon for our application. You can see it when you look for the application on the phone in the main menu.

- If the the device is not connected to the internet when the application is launched, the a pop will appear prompting the using to take him to the wifi connection settings or to quit the application. This is to ensure that the user is connected to the internet so that the data is in sync with the Firebase Database.


## Tests
They can be found under "app/src/test" folder.

## Authors
- Simran Singh, Marco Angeli, Ryan Foster, Abdullah Sumsum, Jeffrey Chan
