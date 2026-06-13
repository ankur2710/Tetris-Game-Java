Tetris

The Tetris game is one of the most popular computer games ever created. The original game was designed and programmed by a Russian programmer Alexey Pajitnov in 1985. Since then, Tetris is available on almost every computer platform in lots of variations. Even my mobile phone has a modified version of the Tetris game.

Tetris is called a falling block puzzle game. In this game, we have seven different shapes called tetrominoes. S-shape, Z-shape, T-shape, L-shape, Line-shape, MirroredL-shape and a Square-shape. Each of these shapes is formed with four squares. The shapes are falling down the board. The object of the Tetris game is to move and rotate the shapes, so that they fit as much as possible. If we manage to form a row, the row is destroyed and we score. We play the tetris game until we top out.

<img width="328" height="132" alt="image" src="https://github.com/user-attachments/assets/7b75310c-8f71-4b75-947e-69a757186483" />

Figure: Tetrominoes

The development

The tetrominoes are drawn using the Swing painting API. We use the java.util.Timer to create a game cycle. The shapes move on a square by square basis (not pixel by pixel). Mathematically the board in the game is a simple list of numbers.

The game starts immediately after it is launched. We can pause the game by pressing the p key. The space key will drop the Tetris piece immediately to the bottom. The d key will drop the piece one line down. (It can be used to speed up the falling a bit.) The game goes at constant speed, no acceleration is implemented. The score is the number of lines that we have removed.

The Shape class provides information about a Tetris piece. 
protected enum Tetrominoe {
    NoShape, ZShape, SShape, LineShape,
    TShape, SquareShape, LShape, MirroredLShape
}

The Tetrominoe enum holds seven Tetris shape names and the empty shape called NoShape.

coords = new int[4][2];
setShape(Tetrominoe.NoShape);


The coords array holds the actual coordinates of a Tetris piece.

int[][][] coordsTable = new int[][][]{
    {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
    {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
    {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
    {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
    {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
    {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
    {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
    {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
};


The coordsTable array holds all possible coordinate values of the Tetris pieces. This is a template from which all pieces take their coordiate values.

for (int i = 0; i < 4; i++) {

    System.arraycopy(coordsTable[shape.ordinal()], 0, coords, 0, 4);
}

We put one row of the coordinate values from the coordsTable into the coords array of a Tetris piece. Note the usage of the ordinal() method. In C++, an enum type is essentially an integer. Unlike in C++, Java enums are full classes and the ordinal() method returns the current position of the enum type in the enum object.

The following image will help understand the coordinate values a bit more. The coords array saves the coordinates of the Tetris piece. For example, numbers (-1, 1), (-1, 0), (0, 0), and (0, -1) represent a rotated S-shape. The following diagram illustrates the shape.

<img width="272" height="230" alt="image" src="https://github.com/user-attachments/assets/8912eef7-b377-49e6-92bd-3f1b94e6ff88" />

Figure: Coordinates

Shape rotateLeft() {

    if (pieceShape == Tetrominoe.SquareShape) {

        return this;
    }

    var result = new Shape();
    result.pieceShape = pieceShape;

    for (int i = 0; i < 4; i++) {

        result.setX(i, y(i));
        result.setY(i, -x(i));
    }

    return result;
}

This code rotates a piece to the left. The square does not have to be rotated. That's why we simply return the reference to the current object. Looking at the previous image will help to understand the rotation.


Finally, we have the Board.java file. This is where the game logic is located.

private final int BOARD_WIDTH = 10;

private final int BOARD_HEIGHT = 22;

private final int PERIOD_INTERVAL = 300;

We have four constants. The BOARD_WIDTH and BOARD_HEIGHT define the size of the board. The PERIOD_INTERVAL constant defines the speed of the game.


...
private boolean isFallingFinished = false;
private boolean isStarted = false;
private boolean isPaused = false;
private int numLinesRemoved = 0;
private int curX = 0;
private int curY = 0;
...

Some important variables are initialized. The isFallingFinished determines if the Tetris shape has finished falling and we then need to create a new shape. The isStarted is used to check if the game has started. Likewise, the isPaused is used to check if the game is paused. The numLinesRemoved counts the number of lines that we have removed so far. The curX and curY determine the actual position of the falling Tetris shape.

private int squareWidth() {

    return (int) getSize().getWidth() / BOARD_WIDTH;
}



private int squareHeight() {

    return (int) getSize().getHeight() / BOARD_HEIGHT;
}

These lines determine the width and height of a single Tetrominoe square.

private Tetrominoe shapeAt(int x, int y) {

    return board[(y * BOARD_WIDTH) + x];
}

We determine the shape at the given coordinates. The shapes are stored in the board array.

void start() {

    curPiece = new Shape();
    board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
...


We create a new current shape and a new board.


clearBoard();
newPiece();

The board is cleared and the new falling piece is initialized.


timer = new Timer(PERIOD_INTERVAL, new GameCycle());
timer.start();

We create a timer. The timer is executed at PERIOD_INTERVAL intervals, creating a game cycle.

private void pause() {

    isPaused = !isPaused;

    if (isPaused) {

        statusbar.setText("paused");
    } else {

        statusbar.setText(String.valueOf(numLinesRemoved));
    }

    repaint();
}


The pause() method pauses or resumes the game. When the game is paused, we display the paused message in the statusbar.

Inside the doDrawing() method, we draw all objects on the board. The painting has two steps.


for (int i = 0; i < BOARD_HEIGHT; i++) {

    for (int j = 0; j < BOARD_WIDTH; j++) {
    Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);

        if (shape != Tetrominoe.NoShape) {
         drawSquare(g, j * squareWidth(),
                    boardTop + i * squareHeight(), shape);
        }
    }
}

In the first step we paint all the shapes or remains of the shapes that have been dropped to the bottom of the board. All the squares are remembered in the board array. We access it using the shapeAt() method.
        

if (curPiece.getShape() != Tetrominoe.NoShape) {

    for (int i = 0; i < 4; i++) {

        int x = curX + curPiece.x(i);
        int y = curY - curPiece.y(i);
          drawSquare(g, x * squareWidth(),
                boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                curPiece.getShape());
    }
}

In the second step, we paint the actual falling piece.


private void dropDown() {

    int newY = curY;

    while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1)) {

            break;
        }

        newY--;
    }
    pieceDropped();
}

If we press the Space key, the piece is dropped to the bottom. We simply try to drop the piece one line down until it reaches the bottom or the top of another fallen Tetris piece. When the Tetris piece finishes falling, the pieceDropped() is called.


private void oneLineDown() {

    if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
    }
}

In the oneLineDown() method we try to move the falling piece down one line until it is fully dropped.

private void clearBoard() {

    for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
    board[i] = Tetrominoe.NoShape;
    }
}
The clearBoard() method fills the board with empty Tetrominoe.NoShape. This is later used at collision detection.


private void pieceDropped() {

    for (int i = 0; i < 4; i++) {

        int x = curX + curPiece.x(i);
        int y = curY - curPiece.y(i);
        board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
    }
     removeFullLines();

    if (!isFallingFinished) {

        newPiece();
    }
}

The pieceDropped() method puts the falling piece into the board array. Once again, the board holds all the squares of the pieces and remains of the pieces that has finished falling. When the piece has finished falling, it is time to check if we can remove some lines off the board. This is the job of the removeFullLines() method. Then we create a new piece, or more precisely, we try to create a new piece.


private void newPiece() {

    curPiece.setRandomShape();
    curX = BOARD_WIDTH / 2 + 1;
    curY = BOARD_HEIGHT - 1 + curPiece.minY();

    if (!tryMove(curPiece, curX, curY)) {
     curPiece.setShape(Tetrominoe.NoShape);
        timer.stop();
         var msg = String.format("Game over. Score: %d", numLinesRemoved);
        statusbar.setText(msg);
    }
}


The newPiece() method creates a new Tetris piece. The piece gets a new random shape. Then we compute the initial curX and curY values. If we cannot move to the initial positions, the game is over—we top out. The timer is stopped and we display Game over string containing the score on the statusbar.


private boolean tryMove(Shape newPiece, int newX, int newY) {

    for (int i = 0; i < 4; i++) {

        int x = newX + newPiece.x(i);
        int y = newY - newPiece.y(i);
        if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

            return false;
        }
          if (shapeAt(x, y) != Tetrominoe.NoShape) {

            return false;
        }
    }
       curPiece = newPiece;
    curX = newX;
    curY = newY;
    repaint();

    return true;
}

The tryMove() method tries to move the Tetris piece. The method returns false if it has reached the board boundaries or it is adjacent to the already fallen Tetris pieces.


private void removeFullLines() {

    int numFullLines = 0;

for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

        boolean lineIsFull = true;

        
        for (int j = 0; j < BOARD_WIDTH; j++) {

            if (shapeAt(j, i) == Tetrominoe.NoShape) {

                lineIsFull = false;
                break;
            }
        }
        
        if (lineIsFull) {

            numFullLines++;

            for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                for (int j = 0; j < BOARD_WIDTH; j++) {
                    board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                }
            }
        }
    }
    if (numFullLines > 0) {

        numLinesRemoved += numFullLines;
        statusbar.setText(String.valueOf(numLinesRemoved));
        isFallingFinished = true;
        curPiece.setShape(Tetrominoe.NoShape);
    }
}


Inside the removeFullLines() method we check if there is any full row among all rows in the board. If there is at least one full line, it is removed. After finding a full line we increase the counter. We move all the lines above the full row one line down. This way we destroy the full line. Notice, that in our Tetris game, we use so called naive gravity. This means that the squares may be left floating above empty gaps.


private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {
 Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
    };
    var color = colors[shape.ordinal()];

    g.setColor(color);
    g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
    g.setColor(color.brighter());
    g.drawLine(x, y + squareHeight() - 1, x, y);
    g.drawLine(x, y, x + squareWidth() - 1, y);
    g.setColor(color.darker());
    g.drawLine(x + 1, y + squareHeight() - 1,
            x + squareWidth() - 1, y + squareHeight() - 1);
    g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
            x + squareWidth() - 1, y + 1);
}

