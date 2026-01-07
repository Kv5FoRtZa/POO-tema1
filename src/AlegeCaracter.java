public class AlegeCaracter {
    public char alege(Colors col, char c){
        if(col == Colors.White){
            if(c == 'K'){
                char lol = '♔';
                return lol;
            }
            else if(c == 'N'){
                char lol = '♘';
                return lol;
            }
            else if(c == 'B'){
                char lol = '♗';
                return lol;
            }
            else if(c == 'R'){
                char lol = '♖';
                return lol;
            }
            else if(c == 'Q'){
                char lol = '♕';
                return lol;
            }
            else{
                char lol = '♙';
                return lol;
            }
        }
        else if(col == Colors.Black){
            if(c == 'K'){
                char lol = '♚';
                return lol;
            }
            else if(c == 'N'){
                char lol = '♞';
                return lol;
            }
            else if(c == 'B'){
                char lol = '♝';
                return lol;
            }
            else if(c == 'R'){
                char lol = '♜';
                return lol;
            }
            else if(c == 'Q'){
                char lol = '♛';
                return lol;
            }
            else {
                char lol = '♟';
                return lol;
            }
        }
        return 0;
    }
}
