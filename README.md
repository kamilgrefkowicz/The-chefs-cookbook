# The-chefs-cookbook

> A set of tools for managing recipes for professional chefs.

## Table of Contents

* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Setup](#setup)
* [Features](#features)


## General Information

- The project aims to provide professional chefs with an easy-to-use service for creating, storing, re-using and sharing
  recipes.
  
- Menus in good restaurants change regularly, but in practice the recipes are written down in a hurry, on a napkin with a half-working sharpie, only to get lost to time. Even if a proper recipe book is written, it also more often than not lacks certain items or contains inconsistencies.

- Chefs CookBook aims to alleviate some issues of managing large sets of recipes in a dynamic environment of a professional kitchen.

## Technologies Used:

Base:
Java 11, Spring Boot, Hibernate, Spring Security, Itext7, Flyway, Lombok,

Testing:
H2 Database, Junit 5, Mockito,

Frontend:
Thymeleaf, Bootstrap

## Setup
Live project can be viewed on heroku [here](https://chefscookbook.herokuapp.com/)
To launch locally, you need to replace database connection information in application.properties.

Currently, language of both the ui and sample data is entirely Polish. If you're an English-only speaker, feel free to review the code, but I'm afraid you will not be able to enjoy a running app.

The front-end has been tested on a pc screen. It will most probably break when viewed on mobile.

## Features
- Three layers of item complexity, reflecting the usage in professional kitchens: basic (what you buy), intermediate (what you prepare in advance), and dish (what you cook to order).
- A small base of basic items for all users. Users can add more basic items for their own use as needed.
- Easy-to-use ui for creating new items in lego-blocks-style. User can create intermediate items (say, potato puree, or awesomesauce), set their recipes (ingredients, yield, description of preparation steps) and use them in creating higher level items (say, 'duck in puree, with awesomesauce on top'). 
- Application allows for any number of layers of intermediate items. Items will be tracked, so that the user can easily see how much of each basic item goes into the dish.
  
- User can calculate how much stuff will be needed to prepare an item with a single click. Need to know how much flour you'll need for 500 pierogi? No problem ;)
- Completed dishes can be added to menus. Once a menu is completed, user can generate a recipe book that will contain recipes for all dishes and all intermediates used in the menu, as well as a list of basic ingredients used throughout (that's important. Seriously.) and a table of contents.


