package com.example.restservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.context.*;
import com.example.restservice.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.activation.FileTypeMap;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.text.Format;


public class NFTTradingMarketSpringMVCController {
	@RequestMapping(value = "/")
	public String index() {
		return "index";
	}
}
