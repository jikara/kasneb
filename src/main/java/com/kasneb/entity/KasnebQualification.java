/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("KASNEB")
public class KasnebQualification extends Qualification {

    @OneToMany(mappedBy = "qualification")
    private Collection<KasnebCourseType> kasnebQualificationTypes;

    public Collection<KasnebCourseType> getKasnebQualificationTypes() {
        return kasnebQualificationTypes;
    }

    public void setKasnebQualificationTypes(Collection<KasnebCourseType> kasnebQualificationTypes) {
        this.kasnebQualificationTypes = kasnebQualificationTypes;
    }
}
