# PROIECT PAOO

# Golden Eagle
### Apetroae Tudor - Macovei Paul

## Notable features

### 3 Levels
**Level 1** - Forest, with tigers as enemies<br>
**Level 2** - Cave, with different types of skeletons as enemies. Here the player can find a goblin NPC that sells potions.<br>
**Level 3** -  Ancient city. Here the player must face different enemies such as a wizard, minotaurs and ghosts. The player must progress using a grappling hook.<br>

### Map loading
**The levels** are tile-based, loaded using the Flyweight design pattern to prevent loading the same resources into memory multiple times.<br>
**The level data** is taken from CSV files.<br>

### A main menu
**From the main menu** the player can select to start a new game, continue from the last save point or quit the game. While in the game the player can also go back to this menu.<br>

### Player movement
**The player movement** is implemented using key listeners. Some key input safety measures are also implemented.<br>
The player can also **jump** by pressing SPACE.<br>

### Camera
**The Camera** is implemented in order to only draw a portion of the screen, that follows the player.<br>

### Turn Based Combat
**The combat** is implemented in a turn based fashion. When the player encounters an enemy, a different state starts, and the player must attack. After dealing damage, the enemy takes its turn, and the player can press **SPACE** in order to block a part of this damage. The player can also **ESCAPE** from the fight.<br>

### Collisions
**Collisions** are implemented using a special **Behavior CSV**, from which the current tile behavior is deduced. For example, tile ID 0 means death.<br>

### Saving and Loading
**Saving and Loadind** is realized through the use of an sqlite database, using a JDBC driver. The data management system is implemented using a Proxy Design Pattern. Between the database and the actual game lies a data buffer, into which data is first loaded.<br>

### Exception handling
Some data validation **exceptions** are managed with custom classes derived from the java Exception clas.<br>

### Score management
The **top 3 scores** are kept in the database. The logic to deduce which score is the highest is implemented in the game code. The current score and top 3 scores are displayed after defeating the Wizard in the third level.<br>

