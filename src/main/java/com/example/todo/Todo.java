package com.example.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "todo")
public class Todo {// extends PanacheEntity {

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "title")
    public String title;

    @Column(name = "completed")
    public Boolean completed;

    // @JoinColumn(name = "priority_id", foreignKey = @ForeignKey(name =
    // "priority_id_fk"))
    // @ManyToOne(optional = false, cascade = CascadeType.ALL)
    // @ManyToOne(optional = false)
    public Long priority_id;

    public Todo() {
    }

    public Todo(Long id, String title, Boolean completed, Priority priority) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.priority_id = priority.priorityId;
    }
}