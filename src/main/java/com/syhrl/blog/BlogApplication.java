package com.syhrl.blog;

import java.io.File;

import com.syhrl.blog.controller.PostController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		new File(PostController.uploadDirectory).mkdir();
		SpringApplication.run(BlogApplication.class, args);
	}
	

}
