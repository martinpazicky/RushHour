
public class Main {

    public static void main(String[] args) {
        //x
        String blockedInput = "((cervene 2 3 2 h)(oranzove 2 1 1 h)(zlte 3 2 1 v)(fialove 2 5 1 v)" +
                "(zelene 3 2 4 v)(svetlomodre 3 6 3 h)(sive 2 5 5 h)(tmavomodre 3 1 6 v)(bloker 3 4 6 v))";
        //8
        String sampleInput = "((cervene 2 3 2 h)(oranzove 2 1 1 h)(zlte 3 2 1 v)(fialove 2 5 1 v)" +
                "(zelene 3 2 4 v)(svetlomodre 3 6 3 h)(sive 2 5 5 h)(tmavomodre 3 1 6 v))";
        //2
        String simpleInput1 = "((cervene 2 6 2 h)(zelene 3 4 4 v))";
        //3
        String simpleInput2 = "((cervene 2 4 2 h)(zelene 3 3 4 v)(zlte 2 2 4 h))";
        //5
        String simpleInput3 = "((cervene 2 4 2 h)(zelene 3 3 4 v)(zlte 2 2 4 h)(cierne 3 1 6 v)(oranzove 2 1 3 v))";
        //23
        String hardInput1 = "((cervene 2 3 2 h)(zelene 2 1 3 v)(cierne 3 1 4 h)(modre 2 2 1 h)(oranzove 2 2 4 v)(zlte 2 2 5 v)" +
                "(hnede 2 3 1 v)(biele 2 4 6 v)(sede 2 5 1 h)(bordove 3 5 3 h))";
        //26
        String hardInput2 = "((cervene 2 3 1 h)(modre 3 1 1 h)(zelene 2 1 4 v)(cierne 3 1 5 v)(oranzove 2 1 6 v)(fialove 2 2 3 v)" +
                "(hnede 2 3 6 v)(biele 2 4 3 v)(ruzove 2 4 4 h)(bordove 2 5 1 v)(sede 2 5 5 h)(zlte 2 6 2 h)(bezove 2 6 4 h))";
        //38
        String hardInput3 = "((cervene 2 3 1 h)(zelene 3 1 3 v)(cierne 2 1 4 h)(biele 3 1 6 v)(modre 2 2 4 v)(sede 2 4 1 v)" +
                "(bordove 3 4 2 h)(oranzove 2 5 2 h)(fialove 2 5 4 v))";

        //49
        String hardInput4 = "((cervene 2 3 3 h)(žlte 3 1 1 h)(biele 2 1 4 v)(fialove 3 1 5 v)(cierne 3 1 6 v)(sede 2 2 1 v)" +
                "(hnede 2 2 2 h)(levandulove 2 4 1 h)(oranzove 2 4 3 v)(ruzove 2 5 2 v)" +
                "(zelene 2 5 5 h)(tyrkysove 2 6 3 h)(blede 2 6 5 h))";
        //51
        String hardInput5 = "((cervene 2 3 4 h)(zelene 3 1 1 v)(biele 2 1 2 h)(fialove 2 2 2 v)(ruzove 2 2 3 v)(sede 2 1 5 v)" +
                "(blede 3 2 6 v)(hnede 3 4 1 h)(bordove 2 4 4 v)(jablkove 2 5 5 h)(oranzove 2 6 1 h)(levandulove 2 5 3 v)(modre 2 6 4 h))";

        double start = System.nanoTime();
        Solver solver = new Solver(hardInput4,false);
        solver.solveBFS();
//        solver.solveDFS();
        double end = System.nanoTime();
        double duration = end - start;
        System.out.println("Runtime: "  + duration / 1000000000 + " seconds");
    }
}