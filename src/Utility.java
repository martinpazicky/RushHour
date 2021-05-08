/**
 * Class contains helper functions to run the program
 * constants from Solver class are used
 */
public class Utility {
    /**
     * changes char at a given position in string
     * @param position position of the char to be changed
     * @param ch new char
     * @param str string where the char is changed
     * @return new string
     */
    public static String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    /**
     * converts row and column of 2D array to index in 1D array (string)
     * @param r row
     * @param c column
     * @return index in string
     */
    public static int rcToIndex(int r, int c) {
        return r * Solver.ROWS + c;
    }

    /**
     * gets col number in 2d array from index in 1d array (string)
     * @param index index to be converted
     * @return col number in 2d array
     */
    public static int indexToC(int index)
    {
        return index % Solver.COLS;
    }

    /**
     * gets row number in 2d array from index in 1d array (string
     * @param index index to be converted
     * @return row number in 2d array
     */
    public static int indexToR(int index)
    {
        return index / Solver.COLS;
    }

    /**
     * checks whether string contains given char
     * @param ch char to check
     * @param str string where to check
     * @return true if char is in string, false otherwise
     */
    public static boolean charInString(char ch, String str)
    {
        return str.indexOf(ch) >= 0;
    }

    /**
     * prints 1d array as 2d array
     * @param map string representing map
     */
    public static void printMap(String map)
    {
        for (int i = 0; i < Solver.ROWS; i++)
        {
            System.out.println(map.substring(Solver.ROWS * i, Solver.ROWS * i + Solver.COLS));
        }
        System.out.println();
    }
}
