package Translate;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Translator {
	// private static final String CLIENT_ID = "FREE_TRIAL_ACCOUNT";
	// private static final String CLIENT_SECRET = "PUBLIC_SECRET";
	private static final String CLIENT_ID = "email";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";

	/**
	 * An example
	 */
	public static void main(String[] args) throws Exception {
		// TODO: Specify your translation requirements here:
		// String fromLang = "en";
		// String toLang = "zh-CN";
		String fromLangCode = "English";
		String toLangCode = "Chinese Simplified";
		String text = "Let's have some fun!";

		Translator.translate(fromLangCode, toLangCode, text);
	}

	/**
	 * Sends out a WhatsApp message via WhatsMate WA Gateway.
	 */
	public static String translate(String fromLang, String toLang, String text) throws Exception {
		// TODO: Should have used a 3rd party library to make a JSON string from an
		// object

		String fromLangCode = findLanguageCode(fromLang);
		String toLangCode = findLanguageCode(toLang);

		String jsonPayload = new StringBuilder().append("{").append("\"fromLang\":\"").append(fromLangCode)
				.append("\",").append("\"toLang\":\"").append(toLangCode).append("\",").append("\"text\":\"")
				.append(text).append("\"").append("}").toString();

		URL url = new URL(ENDPOINT);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
		conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
		conn.setRequestProperty("Content-Type", "application/json");

		OutputStream os = conn.getOutputStream();
		os.write(jsonPayload.getBytes());
		os.flush();
		os.close();

		int statusCode = conn.getResponseCode();
		System.out.println("Status Code: " + statusCode);
		BufferedReader br = new BufferedReader(
				new InputStreamReader((statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()));
		String output;
		StringBuilder result = new StringBuilder();
		while ((output = br.readLine()) != null) {

			System.out.println(output);
			result.append(output);
		}
		conn.disconnect();
		return result.toString();
	}

	/**
	 * A method to convert the language string to a language code (e.g. converts
	 * English to en)
	 * 
	 * @param language
	 *            the input language (e.g. English)
	 * @return the code of the language (e.g. en)
	 */
	public static String findLanguageCode(String language) {

		// Default language is English
		String languageCode = "LanguageNotFound";
		switch (language) {
		case "Afrikaans":
			languageCode = "af";
			break;
		case "Arabic":
			languageCode = "ar";
			break;
		case "Azerbaijani":
			languageCode = "az";
			break;
		case "Belarusian":
			languageCode = "be";
			break;
		case "Bulgarian":
			languageCode = "bg";
			break;
		case "Bengali":
			languageCode = "bn";
			break;
		case "Bosnian":
			languageCode = "bs";
			break;
		case "Catalan":
			languageCode = "ca";
			break;
		case "Cebuano":
			languageCode = "ceb";
			break;
		case "Czech":
			languageCode = "cs";
			break;
		case "Welsh":
			languageCode = "cy";
			break;
		case "Danish":
			languageCode = "da";
			break;
		case "German":
			languageCode = "de";
			break;
		case "Greek":
			languageCode = "el";
			break;
		case "English":
			languageCode = "en";
			break;
		case "Esperanto":
			languageCode = "eo";
			break;
		case "Spanish":
			languageCode = "es";
			break;
		case "Estonian":
			languageCode = "et";
			break;
		case "Basque":
			languageCode = "eu";
			break;
		case "Persian":
			languageCode = "fa";
			break;
		case "Finnish":
			languageCode = "fi";
			break;
		case "French":
			languageCode = "fr";
			break;
		case "Irish":
			languageCode = "ga";
			break;
		case "Galician":
			languageCode = "gl";
			break;
		case "Gujarati":
			languageCode = "gu";
			break;
		case "Hausa":
			languageCode = "ha";
			break;
		case "Hindi":
			languageCode = "hi";
			break;
		case "Hmong":
			languageCode = "hmn";
			break;
		case "Croatian":
			languageCode = "hr";
			break;
		case "Haitian Creole":
			languageCode = "ht";
			break;
		case "Hungarian":
			languageCode = "hu";
			break;
		case "Armenian":
			languageCode = "hy";
			break;
		case "Indonesian":
			languageCode = "id";
			break;
		case "Igbo":
			languageCode = "ig";
			break;
		case "Icelandic":
			languageCode = "is";
			break;
		case "Italian":
			languageCode = "it";
			break;
		case "Hebrew":
			languageCode = "iw";
			break;
		case "Japanese":
			languageCode = "ja";
			break;
		case "Javanese":
			languageCode = "jw";
			break;
		case "Georgian":
			languageCode = "ka";
			break;
		case "Kazakh":
			languageCode = "kk";
			break;
		case "Khmer":
			languageCode = "km";
			break;
		case "Kannada":
			languageCode = "kn";
			break;
		case "Korean":
			languageCode = "ko";
			break;
		case "Latin":
			languageCode = "la";
			break;
		case "Lao":
			languageCode = "lo";
			break;
		case "Lithuanian":
			languageCode = "lt";
			break;
		case "Latvian":
			languageCode = "lv";
			break;
		case "Punjabi":
			languageCode = "ma";
			break;
		case "Malagasy":
			languageCode = "mg";
			break;
		case "Maori":
			languageCode = "mi";
			break;
		case "Macedonian":
			languageCode = "mk";
			break;
		case "Malayalam":
			languageCode = "ml";
			break;
		case "Mongolian":
			languageCode = "mn";
			break;
		case "Marathi":
			languageCode = "mr";
			break;
		case "Malay":
			languageCode = "ms";
			break;
		case "Maltese":
			languageCode = "mt";
			break;
		case "Myanmar (Burmese)":
			languageCode = "my";
			break;
		case "Nepali":
			languageCode = "ne";
			break;
		case "Dutch":
			languageCode = "nl";
			break;
		case "Norwegian":
			languageCode = "no";
			break;
		case "Chichewa":
			languageCode = "ny";
			break;
		case "Polish":
			languageCode = "pl";
			break;
		case "Portuguese":
			languageCode = "pt";
			break;
		case "Romanian":
			languageCode = "ro";
			break;
		case "Russian":
			languageCode = "ru";
			break;
		case "Sinhala":
			languageCode = "si";
			break;
		case "Slovak":
			languageCode = "sk";
			break;
		case "Slovenian":
			languageCode = "sl";
			break;
		case "Somali":
			languageCode = "so";
			break;
		case "Albanian":
			languageCode = "sq";
			break;
		case "Serbian":
			languageCode = "sr";
			break;
		case "Sesotho":
			languageCode = "st";
			break;
		case "Sudanese":
			languageCode = "su";
			break;
		case "Swedish":
			languageCode = "sv";
			break;
		case "Swahili":
			languageCode = "sw";
			break;
		case "Tamil":
			languageCode = "af";
			break;
		case "Telugu":
			languageCode = "ta";
			break;
		case "Tajik":
			languageCode = "te";
			break;
		case "Thai":
			languageCode = "tg";
			break;
		case "Filipino":
			languageCode = "th";
			break;
		case "Turkish":
			languageCode = "tr";
			break;
		case "Ukrainian":
			languageCode = "uk";
			break;
		case "Urdu":
			languageCode = "ur";
			break;
		case "Uzbek":
			languageCode = "uz";
			break;
		case "Vietnamese":
			languageCode = "vi";
			break;
		case "Yiddish":
			languageCode = "yi";
			break;
		case "Yoruba":
			languageCode = "yo";
			break;
		case "Chinese Simplified":
			languageCode = "zh-CN";
			break;
		case "Chinese Traditional":
			languageCode = "zh-TW";
			break;

		default:
			break;
		}

		if (languageCode.equals("LanguageNotFound")) {
			System.out.println(
					"Hey! Your language selection couldn't be identified. Please, choose one of the following languages:"
							+ "\n" + "Afrikaans, Arabic, Azerbaijani, Belarusian, Bulgarian, Bengali, Bosnian, "
							+ "Catalan, Cebuano, Czech, Welsh, Danish, German, Greek, English, Esperanto, "
							+ "Spanish, Estonian, Basque, Persian, Finnish, French, Irish, Galician, "
							+ "Gujarati, Hausa, Hindi, Hmong, Croatian, Haitian Creole, Hungarian, "
							+ "Armenian, Indonesian, Igbo, Icelandic, Italian, Hebrew, Japanese, Javanese, "
							+ "Georgian, Kazakh, Khmer, Kannada, Korean, Latin, Lao, Lithuanian, "
							+ "Latvian, Punjabi, Malagasy, Maori, Macedonian, Malayalam, Mongolian, "
							+ "Marathi, Malay, Maltese, Myanmar (Burmese), Nepali, Dutch, Norwegian, "
							+ "Chichewa, Polish, Portuguese, Romanian, Russian, Sinhala, Slovak, "
							+ "Slovenian, Somali, Albanian, Serbian, Sesotho, Sudanese, Swedish, Swahili, "
							+ "Tamil, Telugu, Tajik, Thai, Filipino, Turkish, Ukrainian, Urdu, Uzbek, Vietnamese, "
							+ "Yiddish, Yoruba, Chinese Simplified, Chinese Traditional, Zulu");
		}
		return languageCode;

	}

}
