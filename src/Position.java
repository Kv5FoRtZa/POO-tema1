import java.util.Objects;

public class Position implements Comparable<Position> {
    //aici e o pozitie pe tabla
    int Coord_num;
    char Coord_lit;
    public Position(char Letter, int nr) throws InvalidMoveException {
        if(Letter > 'H' || Letter < 'A' || nr < 1 || nr > 8) {
            throw new InvalidMoveException("Nu exista aceasta pozitie BO$$ulicc");
        }

        this.Coord_lit = Letter;
        this.Coord_num = nr;
    }
    public boolean inside(){
        int nr = this.Coord_num;
        char litera = this.Coord_lit;
        if(nr < 1 || nr > 8 || litera < 'A' || litera > 'H') {
            return false;
        }
        return true;
    }
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Position))
            return false;
        Position other = (Position) obj;
        return this.Coord_num == other.Coord_num && this.Coord_lit == other.Coord_lit;
    }

    public int hashCode () {
        return Objects.hash(Coord_num, Coord_lit);
    }

    public String toString() {
        String st = "";
        st += Coord_lit;
        st += Coord_num;
        return st;
    }
    @Override
    public int compareTo(Position o) {
        int BEFORE = -1;
        int AFTER = 1;
        int EQUAL = 0;
        if (o == this) {
            return EQUAL;
        }
        if (this.Coord_num < o.Coord_num) {
            return BEFORE;
        }
        else if (this.Coord_num > o.Coord_num) {
            return AFTER;
        }
        else {
            if (this.Coord_lit < o.Coord_lit) {
                return BEFORE;
            }
            else if (this.Coord_lit > o.Coord_lit) {
                return AFTER;
            }
            else {
                return EQUAL;
            }
        }
    }
    public void setCoord_num(int nr) {
        this.Coord_num = nr;
    }
    public void setCoord_lit(char let) {
        this.Coord_lit = let;
    }
    public int getCoord_num() {
        return this.Coord_num;
    }
    public char getCoord_lit() {
        return this.Coord_lit;
    }
}
