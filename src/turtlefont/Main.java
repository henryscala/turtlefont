package turtlefont;

public class Main {
	public static void main(String[] args) {
		String str = "中国abc历史def地理";
		str.codePoints().forEach(System.out::println);
	}
}
