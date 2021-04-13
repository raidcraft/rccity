package de.raidcraft.rccity.entities;

import io.ebean.annotation.Index;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.silthus.ebean.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rccity_cities")
public class City extends BaseEntity {

    @Index(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Resident> residents = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<CityProgress> progressHistory = new ArrayList<>();

    /**
     * @return the current active progress of this city
     */
    public CityProgress progress() {

        return progressHistory()
                .stream().filter(cityProgress -> cityProgress.status() == CityProgress.Status.ACTIVE)
                .findFirst()
                .orElse(CityProgress.create(this));
    }
}
