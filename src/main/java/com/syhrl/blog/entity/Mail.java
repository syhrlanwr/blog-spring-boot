package com.syhrl.blog.entity;

import java.util.Map;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "mail")
public class Mail {
    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;
}
