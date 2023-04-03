package battleship;


import java.util.Arrays;
import java.util.Scanner;

public class Field {
    private char[][] grid;
    private int[] x;
    private char[] y;

    private int countOfAliveShips;

    public Field (int x,int y) {
        this.grid = new char[y][x];
        this.x = new int[x];
        this.y = new char[y];
        for(int i=0; i < y; i++) {
            Arrays.fill(this.grid[i], '~');
        }
        for (int i = 0; i < x; i++) {
            this.x[i] = i+1;
        }
        char letter = 'A';
        for (int i = 0; i < y; i++) {
            this.y[i] = letter;
            letter++;
        }
    }
    public void toggleShipIsPlaced(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (x < 0 || x > this.x.length-1) throw new ArrayIndexOutOfBoundsException();
        if (y < 0 || y > this.y.length-1) throw new ArrayIndexOutOfBoundsException();
        if (this.grid[y][x] == '~') {
            this.grid[y][x] = 'O';
        } else {
            this.grid[y][x] = '~';
        }
    }

    public void printField(boolean fow) {
        System.out.print("  ");
        for (int element: this.x) {
            System.out.print(element + " ");
        }
        System.out.println();
        for (int i = 0; i < this.y.length; i++) {
            System.out.print(this.y[i] + " ");
            for (char element : this.grid[i]) {
                if (fow) {
                    if (element == 'X' || element == 'M') System.out.print(element + " ");
                    else System.out.print("~ ");
                } else {
                    System.out.print(element + " ");
                }
            }
            System.out.println();
        }
    }

    public void inputShipPosition(int shipType) {
        String shipName;
        int shipLength;
        switch (shipType){
            case 2:
                shipName = "Battleship";
                shipLength = 4;
                break;
            case 3:
                shipName = "Submarine";
                shipLength = 3;
                break;
            case 4:
                shipName = "Cruiser";
                shipLength = 3;
                break;
            case 5:
                shipName = "Destroyer";
                shipLength = 2;
                break;
            default:
                shipName = "Aircraft Carrier";
                shipLength = 5;
                break;
        }

        System.out.printf("Enter the coordinates of the %s (%d cells)", shipName, shipLength);
        System.out.println();

        while(true) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input == null){
                System.out.println("Error! Invalid string!");
                continue;
            }
            String[] inputArray = input.split("\\s");
            if (inputArray.length > 2) {
                System.out.println("Error! Input only 2 coordinates!");
                continue;
            }
            String beginX, endX;
            char beginY, endY;
            beginY = inputArray[0].charAt(0);
            beginX = inputArray[0].substring(1);

            endY =  inputArray[1].charAt(0);
            endX = inputArray[1].substring(1);

            int beginXindex = 0, beginYindex = 0, endXindex = 0, endYindex = 0;

            boolean bxSet = false, exSet = false, bySet = false, eySet = false;

            for (int i = 0; i < 10; i++) {
                if (this.x[i] == Integer.parseInt(beginX)) {
                    beginXindex = i;
                    bxSet = true;
                }
                if (this.x[i] == Integer.parseInt(endX)) {
                    endXindex = i;
                    exSet = true;
                }
                if (this.y[i] == beginY) {
                    beginYindex = i;
                    bySet = true;
                }
                if (this.y[i] == endY) {
                    endYindex = i;
                    eySet = true;
                }
            }
            if (!bxSet || !exSet || !bySet || !eySet) {
                System.out.println("Error! Invalid coordinates entered. Try again!");
                continue;
            }
            /*
            System.out.println("beginXindex: " + beginXindex + "  x: " + this.x[beginXindex] + " beginYindex: "
                    + beginYindex + " y:" + this.y[beginYindex]);
            System.out.println("endXindex: " + endXindex + "  x: " + this.x[endXindex] + " endYindex: "
                    + endYindex + " y: " + this.y[endYindex]);
            */

            if(!isValidPosition(beginXindex,beginYindex,endXindex,endYindex,shipLength, shipName)) {
                continue;
            }
            break;
        }
    }
    private void placeShip(int i, int j, int orientation, int otherIndex) {
        for (int k = Math.min(i,j); k <= Math.max(i,j) ; k++) {
            if (orientation == 1) {
                toggleShipIsPlaced(k, otherIndex);
            } else {
                toggleShipIsPlaced(otherIndex, k);
            }
        }
    }
    private boolean isValidPosition(int bx, int by, int ex, int ey, int length, String shipName) {
        return isValidOrientation(bx, by, ex, ey, length, shipName);
    }
    private boolean isValidOrientation(int bx, int by, int ex, int ey, int length, String shipName) {
        boolean vertical = bx == ex, horizontal = by == ey;
        if (!(vertical || horizontal)) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        if (vertical) vertical = isValidLength(by, ey, length, shipName);
        if (horizontal) horizontal = isValidLength(bx, ex, length, shipName);
        if (vertical) vertical = isValidPlace(bx, by, ex, ey);
        if (horizontal) horizontal = isValidPlace(bx, by, ex, ey);
        if (vertical) placeShip(by, ey, 0, bx);
        if (horizontal) placeShip(bx, ex, 1, by);
        return vertical || horizontal;
    }
    private boolean isValidLength(int x, int y, int length, String shipName) {
        if (Math.abs(x - y) == length - 1) return true;
        System.out.printf("Error! Wrong length of the %s! Try again:",shipName);
        System.out.println();
        return false;
    }
    private boolean isValidPlace(int bx, int by, int ex, int ey) {
        if (bx == ex) {
            for (int i = Math.min(by, ey); i <= Math.max(by, ey); i++) {
                if(!isValidPos(i,bx)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        }
        if (by == ey) {
            for (int i = Math.min(bx, ex); i <= Math.max(bx, ex); i++) {
                if(!isValidPos(by,i)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        }
        return true;
    }
    private boolean isValidElement(int i, int j)
    {
        if (i < 0 || j < 0 || i > this.y.length - 1 || j > this.x.length - 1)
            return false;
        return true;
    }
    private boolean isValidPos( int i, int j) {
        if (isValidElement(i - 1, j - 1)) if(this.grid[i-1][j-1] == 'O') return false;
        if (isValidElement(i - 1, j))  if(this.grid[i-1][j] == 'O') return false;
        if (isValidElement(i - 1, j + 1)) if(this.grid[i-1][j+1] == 'O') return false;
        if (isValidElement(i, j - 1)) if(this.grid[i][j-1] == 'O') return false;
        if (isValidElement(i, j + 1)) if(this.grid[i][j+1] == 'O') return false;
        if (isValidElement(i + 1, j - 1)) if(this.grid[i+1][j-1] == 'O') return false;
        if (isValidElement(i + 1, j)) if(this.grid[i+1][j] == 'O') return false;
        if (isValidElement(i + 1, j + 1)) if(this.grid[i+1][j+1] == 'O') return false;
        return true;
    }

    public void takeAShoot() {
        this.countOfAliveShips = this.shipsToDestroy();
        while (true) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input == null){
                System.out.println("Error! Invalid string!");
                continue;
            }
            char y = input.charAt(0);
            String x = input.substring(1);

            int yIndex = 0, xIndex = 0;
            boolean xSet = false, ySet = false;

            for (int i = 0; i < 10; i++) {
                if (this.x[i] == Integer.parseInt(x)) {
                    xIndex = i;
                    xSet = true;
                }
                if (this.y[i] == y) {
                    yIndex = i;
                    ySet = true;
                }
            }
            if (!xSet || !ySet) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }

            shoot(xIndex,yIndex);
            break;
        }
    }

    private void shoot(int x, int y) {
        if (this.grid[y][x] == 'O' || this.grid[y][x] == 'X') {
            this.grid[y][x] = 'X';
            this.printField(true);
            if(this.shipsToDestroy() == 0) return;
            if(this.countOfAliveShips > this.shipsToDestroy()) {
                System.out.println("You sank a ship!");
            } else {
                System.out.println("You hit a ship!");
            }
        } else {
            this.grid[y][x] = 'M';
            System.out.println("You missed:");
        }
    }

    public int shipsToDestroy() {
        int count = 0;
        for (int i = 0; i < this.y.length; i++) {
            boolean shipFound = false;
            boolean shipIsNotDestroyed = false;
            int length = 0;
            for (int j = 0; j < this.x.length; j++) {
                if (this.grid[i][j] == 'O' && !shipFound) {
                    shipFound = true;
                    shipIsNotDestroyed = true;
                    length++;
                } else if (this.grid[i][j] == 'X' && !shipFound) {
                    shipFound = true;
                    length++;
                } else if (this.grid[i][j] == 'X' && shipFound) {
                    length++;
                } else if (this.grid[i][j] == 'O' && shipFound) {
                    shipIsNotDestroyed = true;
                    length++;
                } else if ((this.grid[i][j] == 'M' || this.grid[i][j] == '~') && shipFound) {
                    if (length > 1 && shipIsNotDestroyed) {
                        count++;
                    }
                    length = 0;
                    shipFound = false;
                    shipIsNotDestroyed = false;
                }
            }
            if(shipFound && shipIsNotDestroyed && length > 1) {
                count++;
            }
        }
        for (int j = 0; j < this.y.length; j++) {
            boolean shipFound = false;
            boolean shipIsNotDestroyed = false;
            int length = 0;
            for (int i = 0; i < this.x.length; i++) {
                if (this.grid[i][j] == 'O' && !shipFound) {
                    shipFound = true;
                    shipIsNotDestroyed = true;
                    length++;
                } else if (this.grid[i][j] == 'X' && !shipFound) {
                    shipFound = true;
                    length++;
                } else if (this.grid[i][j] == 'X' && shipFound) {
                    length++;
                } else if (this.grid[i][j] == 'O' && shipFound) {
                    shipIsNotDestroyed = true;
                    length++;
                } else if ((this.grid[i][j] == 'M' || this.grid[i][j] == '~') && shipFound) {
                    if (length > 1 && shipIsNotDestroyed) {
                        count++;
                    }
                    length = 0;
                    shipFound = false;
                    shipIsNotDestroyed = false;
                }
            }
            if(shipFound && shipIsNotDestroyed && length > 1) {
                count++;
            }
        }
        return count;
    }

}
