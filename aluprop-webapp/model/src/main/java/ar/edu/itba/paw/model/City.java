package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_id_seq")
    @SequenceGenerator(sequenceName = "cities_id_seq", name = "cities_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(length = 75)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provinceId")
    private Province province;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryId")
    private Country country;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Collection<Neighbourhood> neighbourhoods;

    /* package */ City() { }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Province getProvince() {
        return province;
    }

    public Country getCountry() {
        return country;
    }

    public Collection<Neighbourhood> getNeighbourhoods() {
        return neighbourhoods;
    }

    public static class CityBuilder {
        public static final String ID_EXCEPTION = "id must be provided";
        public static final String PROVINCE_AND_COUNTRY_NOT_PROVIDED = "either province or country must be provided";
        private City city = new City();

        public City build() {
            if(city.id < 1) throw new IllegalArgumentException(ID_EXCEPTION);
            checkEitherProvinceOrCountryGiven();
            return city;
        }

        private void checkEitherProvinceOrCountryGiven() {
            if(city.country == null && city.province == null)
                throw new IllegalArgumentException(PROVINCE_AND_COUNTRY_NOT_PROVIDED);
        }

        public CityBuilder withId(long id) {
            city.id = id;
            return this;
        }

        public CityBuilder withName(String name) {
            city.name = name;
            return this;
        }

        public CityBuilder withProvince(Province province) {
            city.province = province;
            return this;
        }

        public CityBuilder withCountry(Country country) {
            city.country = country;
            return this;
        }

        public CityBuilder withNeighbouhoods(Collection<Neighbourhood> neighbourhoods) {
            city.neighbourhoods = neighbourhoods;
            return this;
        }
    }
}
