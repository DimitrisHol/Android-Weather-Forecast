package gr.uom.android.myweather;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class City {

    @Id
    private Long id;

    @NotNull
    private String cityName;
    private String countryCode;

    @Generated(hash = 1675138370)
    public City(Long id, @NotNull String cityName, String countryCode) {
        this.id = id;
        this.cityName = cityName;
        this.countryCode = countryCode;
    }

    @Generated(hash = 750791287)
    public City() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
