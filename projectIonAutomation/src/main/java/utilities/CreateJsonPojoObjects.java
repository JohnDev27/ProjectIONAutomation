package utilities;

import java.util.List;

import io.github.sharelison.jsontojava.JsonToJava;
import io.github.sharelison.jsontojava.converter.JsonClassResult;

public class CreateJsonPojoObjects
{
	String pathToJsonFile = "res/mailhogJSON.json";
	JsonToJava jsonToJava = new JsonToJava();
	//jackson annotations is generated by default. use jsonToJava.jsonToJava(pathToJsonFile, "MyJsonToJavaObject", "org.example.jsontojava", false) to generate class without annotations.

	List<JsonClassResult> jsonResult = jsonToJava.jsonToJava(pathToJsonFile, "MyJsonToJavaObject", "utilities.jsontojava");

	//Do something with generated list of classes created.
	//Class JsonClassResult holds 2 String properties: The object name and the generated class in a string.
	
	
	
}
