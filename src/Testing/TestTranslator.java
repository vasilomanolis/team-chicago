package Testing;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import Translate.Translator;

/**
 * /** This class includes the testing for the methods of the Translate class
 * 
 * @author chicago
 *
 */
public class TestTranslator {

	@Test
	public void TestFindLanguageCode1() { // testing the findLanguageCode() method of the Translate Class

		String language = "Greek";

		String actualString = Translator.findLanguageCode(language);
		String expectedString = "el";

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestFindLanguageCode2() { // testing the findLanguageCode() method of the Translate Class

		String language = "Chinese Simplified";

		String actualString = Translator.findLanguageCode(language);
		String expectedString = "zh-CN";

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestFindLanguageCode3() { // testing the findLanguageCode() method of the Translate Class for Unknown
											// Language

		String language = "Wrong String";

		String actualString = Translator.findLanguageCode(language);
		String expectedString = "LanguageNotFound";

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestTranslate1() throws Exception { // testing the translate() method of the Translate Class

		String fromLangCode = "English";
		String toLangCode = "Greek";
		String sourceText = "Let's have some fun!";
		String translatedText = "Ας διασκεδάσουμε!";

		String actualString = Translator.translate(fromLangCode, toLangCode, sourceText);

		String expectedString = translatedText;

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestTranslate2() throws Exception { // testing the translate() method of the Translate Class

		String fromLangCode = "Greek";
		String toLangCode = "English";
		String sourceText = "Ας διασκεδάσουμε!";
		String translatedText = "Let's have fun!";

		String actualString = Translator.translate(fromLangCode, toLangCode, sourceText);

		String expectedString = translatedText;

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestTranslate3() throws Exception { // testing the translate() method of the Translate Class

		String fromLangCode = "Greek";
		String toLangCode = "English";
		String sourceText = "fgdfgagargrgaregaer";
		String translatedText = "fgdfgagargrgaregaer";

		String actualString = Translator.translate(fromLangCode, toLangCode, sourceText);

		String expectedString = translatedText;

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestTranslate4() throws Exception { // testing the translate() method of the Translate Class

		String fromLangCode = "English";
		String toLangCode = "Greek";
		String sourceText = "?";
		String translatedText = ";";

		String actualString = Translator.translate(fromLangCode, toLangCode, sourceText);

		String expectedString = translatedText;

		assertEquals(expectedString, actualString);

	}

	@Test
	public void TestTranslate5() throws Exception { // testing the translate() method of the Translate Class

		String fromLangCode = "English";
		String toLangCode = "Greek";
		String sourceText = "1234567890";
		String translatedText = "1234567890";

		String actualString = Translator.translate(fromLangCode, toLangCode, sourceText);

		String expectedString = translatedText;

		assertEquals(expectedString, actualString);

	}

}