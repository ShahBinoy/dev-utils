package bs.utils;

import bs.utils.db.ThreadedDataLoader;

import java.util.Arrays;

/**
 * Created by shahb on 2/13/15.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("args = [" + Arrays.toString(args) + "]");

        if(args[0].equalsIgnoreCase("data"))
        {
            ThreadedDataLoader tdl = new ThreadedDataLoader(args[1],Integer.parseInt(args[2]));
            tdl.execute();
        }
    }
}
