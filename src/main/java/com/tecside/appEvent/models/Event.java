package com.tecside.appEvent.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "promoter")
    private String promoter;

    @Column(name = "description")
    private String description;


    @Column(name = "location")
    private String location;

    @Column(name = "venue")
    private String venue;

    @Column(name = "tags")
    private String tags;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

}
