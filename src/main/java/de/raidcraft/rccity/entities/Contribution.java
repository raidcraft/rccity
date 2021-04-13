package de.raidcraft.rccity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rccity_contributions")
public class Contribution extends BaseEntity {

    public static Contribution create(CityProgress cityProgress, Resident resident) {

        Contribution contribution = new Contribution(cityProgress, resident);
        contribution.save();
        return contribution;
    }

    public static Contribution create(CityProgress progress, Resident resident, float value) {

        Contribution contribution = new Contribution(progress, resident).value(value);
        contribution.save();
        return contribution;
    }

    @ManyToOne
    private CityProgress progress;

    @ManyToOne
    private Resident resident;
    private String source;
    private float value;

    Contribution(CityProgress progress, Resident resident) {
        this.progress = progress;
        this.resident = resident;
    }
}
