/**
 * class representing vehicle
 */
public class Vehicle {
    private int beginningR;
    private int beginningC;

    public int getEndR() {
        return endR;
    }

    public int getEndC() {
        return endC;
    }

    private int endR;
    private int endC;
    private char carRepresent;
    private char orientation;
    private int length;

    public Vehicle(int r, int c,char carRepresent)
    {
        this.beginningR = r;
        this.beginningC = c;
        this.carRepresent = carRepresent;
    }

    public void findEndPosition()
    {
        if(this.orientation == 'h')
        {
            this.endR = beginningR;
            this.endC = beginningC + this.length - 1;
        }
        if(this.orientation == 'v')
        {
            this.endR = beginningR + this.length - 1;
            this.endC = beginningC;
        }
    }

    public int getBeginningR() {
        return beginningR;
    }

    public int getBeginningC() {
        return beginningC;
    }


    public char getCarRepresent() {
        return carRepresent;
    }

    public char getOrientation() {
        return orientation;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOrientation(char orientation)
    {
        this.orientation = orientation;
    }
}
