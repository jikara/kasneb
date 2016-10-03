/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "NON-KENYAN")
public class NonKenyanCentre extends CentreZone {

    @OneToMany(mappedBy = "zone")
    @JsonManagedReference
    private Collection<ExamCentre> examCentres;

    public NonKenyanCentre() {
        super();
    }

    public Collection<ExamCentre> getExamCentres() {
        return examCentres;
    }

    public void setExamCentres(Collection<ExamCentre> examCentres) {
        this.examCentres = examCentres;
    }

}
