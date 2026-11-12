public class Main {
    public static void main(String[] args) {
        MyStringBuilder sb = new MyStringBuilder();

        sb.append("Hello");
        sb.append(" World");
        sb.append("!");

        System.out.println(sb);

        sb.undo();
        System.out.println(sb);

        sb.undo();
        System.out.println(sb);
    }
}