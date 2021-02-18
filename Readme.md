
# First Person Perspective Flatland Game

Edwin A. Abbott wrote Flatland in 1884.  He designed a 2D world in which polygons live and interact.  He described the society of these 'Flatlanders,' and emphasized how important shape recognition is to the class system in Flatland: the more sides a polygon has, the higher class he has.  This game recreates the world of Flatland and places the player as A Square in Flatland.  This game also set in a first person perspective from a Flatlander: you see a one dimensional image.

Ok, so here is an overview of the game:

- First Person Perspective (I built a simple raytracer to render the view)
- Character: A Square
- Plot: A Square's Grandson ran away and you have to find him.
- Programmed 100% in Java.
- Controls:
  - Move mouse to look left and right
  - WASD/Arrow Keys to move
	
![Screenshot from Game](docs/images/screenshot.png)

This is a project for my Honors class, Stories and Structures, at Eastern Kentucky University, Spring2014.

## Build

```sh
mvn package
```

Or download a pre-compiled jar [https://github.com/smikulcik/first-person-flatland/releases](https://github.com/smikulcik/first-person-flatland/releases)

## Run

```sh
java -jar ./target/flatland-game-1.0.jar
```
