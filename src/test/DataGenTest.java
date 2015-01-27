package test;
import com.github.javafaker.*;

public class DataGenTest {

	public static void main(String[] args) {
		Faker faker = new Faker();
	    System.out.println(faker.address().firstName());
        System.out.println(faker.business().creditCardExpiry());
        System.out.println(faker.address().streetAddress(true));
        System.out.println(faker.internet().emailAddress());
        System.out.println(faker.business().creditCardNumber());
	}

}
