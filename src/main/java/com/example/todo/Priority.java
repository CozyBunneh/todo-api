package com.example.todo;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "priority")
public class Priority { // extends PanacheEntityBase {
    @Id
    @Column(name = "id")
    // @JoinColumn(name = "priority")
    // @ManyToOne(cascade = CascadeType.ALL)
    public Long priorityId;

    @Column(name = "name")
    public String name;

    public Priority() {
    }

    public Priority(Long id, String name) {
        this.priorityId = id;
        this.name = name;
    }
}