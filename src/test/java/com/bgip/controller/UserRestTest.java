package com.bgip.controller;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;

public class UserRestTest {

	
	@BeforeClass
	public static void init(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}
	
	@Test
	public void testUserFetchesSuccess(){
		
	}
	
	
	
}
