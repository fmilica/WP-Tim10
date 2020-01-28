package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Disc;
import model.Organisation;
import model.VMResource;
import model.VirtualMachine;

public class Organisations {

	private HashMap<String, Organisation> organisationsMap;
	
	public Organisations() {}
	
	public Organisations(String filePath) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		/*
		String responseJson = new String(responseBody); // from the service endpoint
		// which format has the response of the server
		final TypeToken<ServiceResponse> requestListTypeToken = new TypeToken<ServiceResponse>() {};
		
		final RuntimeTypeAdapterFactory<AbstractContainer> typeFactory = RuntimeTypeAdapterFactory  
		        .of(VMResource.class, "type") // Here you specify which is the parent class and what field particularizes the child class.
		        .registerSubtype(VirtualMachine.class, "dog") // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
		        .registerSubtype(Disc.class, "cat");

		// add the polymorphic specialization
		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeFactory).create();

		// do the mapping
		final ServiceResponse deserializedRequestList = gson.fromJson(responseJson, requestListTypeToken.getType() );
		*/
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Organisation> organisations = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "organisations.json"), new TypeToken<HashMap<String, Organisation>>(){}.getType());
		this.organisationsMap = organisations;
	}

	public Organisations(HashMap<String, Organisation> organisationsMap) {
		this.organisationsMap = organisationsMap;
	}

	public HashMap<String, Organisation> getOrganisationsMap() {
		return organisationsMap;
	}
	public void setOrganisationsMap(HashMap<String, Organisation> organisationsMap) {
		this.organisationsMap = organisationsMap;
	}
}
