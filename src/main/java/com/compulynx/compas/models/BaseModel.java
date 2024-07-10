package com.compulynx.compas.models;
 
import javax.persistence.*;
 
@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    protected BaseModel() {
    }
 
    public Long getId() {
        return id;
    }
     
    public void setId(Long id) {
        this.id =id;
    }
}