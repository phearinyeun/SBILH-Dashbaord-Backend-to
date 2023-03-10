package com.sbilhbank.insur.entity.primary;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Dashboard {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "url")
    public String url;
    @Column(name = "response_name")
    public String response_Name;
    @Column(name = "description")
    public String description;
    @Column(name = "icon_file")
    public String icon_file;

    @Column(name = "guideline_file")
    public String guideline_file;
}
