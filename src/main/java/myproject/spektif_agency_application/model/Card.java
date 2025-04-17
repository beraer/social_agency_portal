package myproject.spektif_agency_application.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 3000)
    private String description;

    private LocalDate dueDate;

    private boolean isProject = false;

    @ManyToOne
    private BoardList boardList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project projectDetails;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardAttachment> attachments = new ArrayList<>();
}
