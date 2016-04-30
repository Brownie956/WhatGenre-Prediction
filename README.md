**WHATGENRE PREDICTOR**
-
CLASSES:
* Analysis - Main method class
* ClassifierMLP - Neural network code (Neuroph)
* Conf - Framework settings
* DataSetCreator - General purpose dataset functions
* Utils - General purpose functions
* Window - Class to represent an audio window/frame

**INSTALLATION**

1. Import WhatGenre.jar into your project

**USING WHATGENRE**

WhatGenre makes use of the following frameworks:
* jAudio
https://github.com/dmcennis/jaudioGIT
* Neuroph
http://neuroph.sourceforge.net/

To classify an instance use
classifyInstance(File audioTrack)
found in **ClassifierMLP**

**DataSetCreator** can be used to extract audio features and store in csv files.

Framework settings can be set in **Conf**

Some general purpose functions can be found in **Utils**
