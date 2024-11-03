import java.lang.ProcessBuilder.Redirect.Type;
import java.util.Scanner;

public class Calculator {
    public ST<Character, Double> vars = new ST<>();
    public ST<Character, Integer> opST = new ST<>();

    public Stack<String> vals;
    public Stack<Character> ops;

    public void fillOpST() {
        opST.put('=', 0);
        opST.put('(', 1);
        opST.put('+', 2);
        opST.put('-', 2);
        opST.put('*', 3);
        opST.put('/', 3);
        opST.put('%', 3);
        opST.put('@', 3);
        opST.put(')', 4);
    }

    public Calculator() {
        fillOpST();
    }

    public double exponent(double base, double power) {
        return Math.pow(base, power);
    }

    public String[] readNumber(String line, int i) {
        String temp = ""; 
        while (i < line.length() && Character.isDigit(line.charAt(i))) { 
            temp += (line.charAt(i));
            i++;
        }
        if (i < line.length() && line.charAt(i) == '.') { 
            temp += line.charAt(i);
            i++;
            while (Character.isDigit(line.charAt(i))) { 
                temp += (line.charAt(i));
                i++;
            }
            i--; 
        }
        else {
            i--; 
        }
        String[] result = {temp, Integer.toString(i)};
        return result;
    }

    public double compute(String val1, String val2, char op) {
        switch(op) {
            case '+':
                return Double.parseDouble(val1) + Double.parseDouble(val2);
            case '-':
                return Double.parseDouble(val1) - Double.parseDouble(val2);
            case '*':
                return Double.parseDouble(val1) * Double.parseDouble(val2);
            case '/':
                return Double.parseDouble(val1) / Double.parseDouble(val2);
            case '@':
                return Math.pow(Double.parseDouble(val1), Double.parseDouble(val2));
            case '%':
                return Double.parseDouble(val1) % Double.parseDouble(val2);
            case '=':
                vars.put(val1.charAt(0), Double.parseDouble(val2));
                return vars.get(val1.charAt(0));
            default:
                throw new IllegalArgumentException("Operator " + op + " not recongnized");
        }
    }

    public void evaluate(String line) {
        vals = new Stack<>();
        ops = new Stack<>(); 

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                //ignore
            }
            else if (Character.isDigit(line.charAt(i))) { 
                vals.push(readNumber(line, i)[0]);
                i = Integer.parseInt(readNumber(line, i)[1]);
            }
            else if (Character.isLetter(line.charAt(i))) {
                vals.push(Character.toString(line.charAt(i)));
            }
            else if (opST.contains(line.charAt(i))) {
                char curOp = line.charAt(i);
                while (!ops.isEmpty() && ops.peek() != '(' && opST.get(curOp).compareTo(opST.get(ops.peek())) <= 0) {
                    String val2 = vals.pop();
                    String val1 = vals.pop();
                    char op = ops.pop();
                    if (op == ')') { //probably a way to make this whole parentheses business more elegant. find
                        op = ops.pop();
                    }
                    vals.push(Double.toString(compute(val1, val2, op)));
                    if (!ops.isEmpty() && ops.peek() == '(') {
                        ops.pop();
                    }
                }
                ops.push(line.charAt(i));
            }
        }
        while(!ops.isEmpty()) {
            String val2 = vals.pop();
            String val1 = vals.pop();
            // System.out.println("val1: " + val1);
            // System.out.println(Character.isLetter(val1.charAt(0)));
            // System.out.println(".get: " + vars.get('a'));
            // System.out.println(".get var: " + vars.get(val1.charAt(0)));
            // if (Character.isLetter(val1.charAt(0))) {
            //     // val1 = vars.get(val1.charAt(0));
            //     System.out.println("true!!!");
            //     System.out.println("letter " + (val1.charAt(0) == 'a'));
            //     System.out.println(".get var 2: " + vars.get(val1.charAt(0))); // fully able to print this, but if i try to use vars.get(etc) it throws null pointer
            // }
            char op = ops.pop();
            vals.push(Double.toString(compute(val1, val2, op)));
        }
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner scan = new Scanner(System.in);
        int i = 0;
        while (scan.hasNextLine()) {
            String current = scan.nextLine();
            calc.evaluate(current);
            System.out.println(calc.vals.pop());
        }
        scan.close();
    }
}
