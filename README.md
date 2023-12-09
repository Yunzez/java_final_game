# java_final_game

## important note to run the game:

**Basic Information:**
This project is a 2D RPG game program designed based on libGDX API. Players could choose up to 8 different characters to make an exciting adventure. This program is supported to execute in Visual Studio Code under Windows and MacOSX. 


**To run the game**

To execute the program on Windows:
-  make sure you remove or comment the line vmArgs": "-XstartOnFirstThread in launch.json under directory .vscode. 


To execute the program on MacOSX, 
- just make sure this line exists or uncommented. To access more information about how to play the game, there is a built-in manual that will be available when you start the game. 

 
----------

## Game Design Overview: 

### Frontend Design: Libgdx: 

**We employ libgdx for 2D and 3D rendering.** 

There are a few **screens** that we made, which are the key components to this game:

1. **BaseScreen**
- the base screen is the only screen that **renders 3D models** in the game. We download third party, free 3D models and animation for this part. We also created some of the models ourselves using the tools in the **Resource Section** below 
- user can enter inventory, adventure, saving screen, and scoreboard here

2. **BattleScreen**
- Battle screen is the screen where we illustrate battles, use static image and buttons to represent battle. It may not be the most engaging way but it's the best we can do due to limited time.

3. **ChooseCharacterScreen**
- User can choose character from this screen, we currently have 8 characters, user can add their own chracters by modifying the characters.json. 
- the screen will present user the character and a story with their basic information 

4. **GameScreen**
- game screen is the map screen where map and monsters are generated. We count each tile (a recurring texture) as a unit and randomly creating the map and monsters
- we designed this to only be 2 levels to make it easy to pass. As level goes up, the monster will randomly increase health and be harder to be defeated. 

5. **LoadFromPreviousSavingScreen**
- this is where user can **store and load** games in to their local drive. The saving will be in the savedCharacters.json

6. **ScoreBoardScreen**
- this is the screen where we prsent user the scoreboard. Scoreboard shows the top 100 players in the server. Our server is hosted on Google clound. 
- User can register an account with an username and a password where the username will be user's name in the rank. 
- we do not use super secure encryption so make sure we are not using some actual passwords you would use in your daily life. 

7. **SettingScreen**
- setting screen is a simple setting the user can set in the beginning of the game. It currently can let user start the background music or choose wheather to skip the tutorial. Currently the settings are not saved in the local drive so user will have to redo the settings once they login again. 

8. **TutorialScreen**
- tutorial screen shows user the basic operation of the game. This can be skipped and can be skipped in the settings

9. **WelcomeScreen**
- this is the landing screen once user enters the game. 



There are a few **models** that we made to handle data:
- **Attack**
  - An enumeration defining basic attacks, with AttackType and AttackUtils for implementation.
- **Buff**
    - A class representing buffs, detailing the effects of item usage.
- **FontGenerator**
    - Manages font aesthetics and styles throughout the game.
- **GameCharacter**
    - A class depicting characters within the game, each with unique attributes and stories. 
- **Item**
    - An enumeration detailing all available items within the game
- **Room**
    - A class representing a room, defined by its boundaries and properties
- **ScoreBoardEntry**
    - Describes individual records on the scoreboard, encapsulating player achievements.
- **TilePoint**
    - A class denoting a type of tile, crucial for map generation and navigation
- **UserGameCharacters**
    - Extends "GameCharacter" to depict characters as saved by users, preserving their game state. 

___
---

### Backend Design:

There is a simple database designed to upload user score and compare it across with other players. 
The database is hosted on Google Cloud with a few features:
- Fetch top 100 players records
- Upload user records
- Register user
- Login user and upload their scores



## Resource:
- [pixelate-effect-image](https://pinetools.com/pixelate-effect-image) : we use this to generate pixelated image
- [piskelapp](https://www.piskelapp.com/p/create/sprite): we import pixelized image and use this to generate charactors preview
- [gdx-texture-packer-gui](https://github.com/crashinvaders/gdx-texture-packer-gui): we use this to generate altas
- [crop-image](https://imageresizer.com/crop-image/editor): we use this to crop the image after generation
- [BitmapFont](https://www.dafont.com/bitmap.php): we use this website to get free fonts!
- [Create OBJ models](https://www.figuro.io/Designer): we use this website to make simple obj models
  *shout out to all the creators above that made this dumb project possible*
