import java.lang.*;


/**
 * Created by Павел Яковлев on 14.03.14.
 * Класс, контролирующий всю внутренню деятельность системы. То бишь main
 */
public class Driver {
    public static void main(String [ ] args){
        Graph Test = new Graph(10,15);
        Test.printGraphStat();
    }
}
