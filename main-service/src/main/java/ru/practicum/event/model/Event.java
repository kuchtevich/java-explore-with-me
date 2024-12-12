package ru.practicum.event.model;

import jakarta.persistence.*;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;
import java.time.LocalDateTime;
import ru.practicum.event.model.State;

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    Integer confirmedRequests;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private String title;

    private String annotation;

    private String description;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    private Boolean paid;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Transient
    Integer views;
}
