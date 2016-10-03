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
@DiscriminatorValue(value = "KENYAN")
public class KenyanCentre extends CentreZone {

    @OneToMany(mappedBy = "zone")
    @JsonManagedReference
    private Collection<CentreRegion> centreRegions;

    public KenyanCentre() {
        super();
    }

    public Collection<CentreRegion> getCentreRegions() {
        return centreRegions;
    }

    public void setCentreRegions(Collection<CentreRegion> centreRegions) {
        this.centreRegions = centreRegions;
    }
}
