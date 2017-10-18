Reviewer: Daniel Lai (u6352900)
Component: <...>
Author: Wangchao Wei (u6263937)

Review Comments:
I will be reviewing the Board class (code written by Wangchao Wei) under comp1110.ass2.gui.assets (task #7)
I will also review CheckFlipTest and CheckRotateTest classes.

(note: this class is distinct from the Board class under META-INF)

Overall, variables are properly named according to convention.
    The structure is good field -> constructors -> methods
    The code functions as intended.
    Some of the methods for Board do not have good clarity.

Specific comments:

 (1) CheckFlipTest()
     Good test for true cases!
     -You may want to  write a false example, to check the method correctly returns false?
     -E.g. 0,0,0,2,1,1,2,2,2 would not flip to 1,0,0,2,2,1,1,1,0

(2)  Covers true cases that are within bounds well! Good.
     Add a test to see what happens when you rotate more than 3 times. Does it break? Or does it correctly return to 0.
      -So add the test case rotatePieceNTimesTestRotateFour()
      -Also you may want to check what happens if we use negative numbers.
      -Also you may want to add some false cases to check against.

(3) snapToHome() (line ):
    Maybe a quick comment to say what the method does:
    (i) Image of piece gets placed to 'home'.
    (ii) placement needs to be updated with the piece removed.

(4) rotate() (line 267-325):
    For readability might be helpful to break the big method into smaller methods and then compose those methods together.
    For example you can write separate methods for the booleans and separate methods for making a piece rotate,
    then you can compose those methods e.g. if(isOffBoard){rotate()} else{takeOffBoard() and rotate()}.

        -Should use NOT_PLACED instead of 'Z' in:
            piecestate[piece-'A'].charAt(2) == 'Z' ... (line 269)

        -The code within the if-else statement (line 269 - 284) seems a bit complex.

        -Maybe a comment to say that rotate() also includes flipping the piece. or rename e.g. rotateAndFlip()

        -You have used % so it doesn't seem necessary to write a separate case for D and H.
            *setRotate((getRotate() + 90) % 360); ... line 281
         This should reset the angle to 0 after cycling through 4 letters.

        -Also you can use % for orientation
            *orientation = (char)(orientation+1) ... line 282
            e.g. orientation = (char)(orientation+1%8) ... since there are 8 pieces.

        -Might be helpful to rename the boolean
            *boolean isOffBoard = piecestate[piece-'A'].charAt(2) == 'Z' ... (line 269)
         This will make what the else statement (line 287) a little clearer
         i.e. we need to take the piece off the board before we can rotate it.

         -(Line 299 - 321)
          My understanding is if the (i) Piece is on the board && (ii) Piece on the border
          then (iii) Which rotations of this piece are valid?

          Naming the booleans within the if statements would also help (e.g. isOnBorder) ... line 300

          Instead of creating 3 new strings representing the pieces why not make a list of all 8 rotations, then
          filter out the rotations that do not satisfy isPlacementSequenceValid.
          e.g. List<orientations> filter(List<Character> orientations){
                 //initialise filteredList to empty list
                 for(Character o: orientations){
                   if(isPlacementSequenceValid(orientation);
                   filteredList.add(orientation);
                  }
                  return filteredList;
          We can cycle through the list with the mouse scroll.
          This is just a suggestion, might not be the best way, just seems more clear than the current method.




* What are the best features of this code?
* Is the code well-documented?
* Is the program decomposition (class and method structure) appropriate?
* Does it follow Java code conventions (for example, are methods and variables properly named), and is the style consistent throughout?
* If you suspect an error in the code, suggest a particular situation in which the program will not function correctly.
