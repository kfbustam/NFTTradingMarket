package com.example.restservice;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.*;
import org.springframework.boot.context.*;
import com.example.restservice.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.text.SimpleDateFormat;
import java.text.Format;

/**
 * The type Airline reservation system rest controller.
 */
@RestController
public class NFTTradingMarketRESTController {

	@Autowired
	private Service service;

	/**
	 * Gets landing page.
	 *
	 */
	@GetMapping("/test")
	@ResponseBody
	public ResponseEntity<String> getLandingPage() {
		String errorMessage = "{\"BadRequest\": {\"code\": \" HttpStatus.BAD_REQUEST \",\"msg\": \" Sorry\"}}";
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("test", HttpStatus.FOUND);
	}
}
