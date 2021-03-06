package com.salamanca.jcs.celebritynetworth;

/**
 * Created by jcs on 4/9/15.
 */
public class Celebrity {

    private String name;
    private String netWorthString;
    private String annualSalaryString;
    private String dateOfBirth;
    private String placeOfBirth;
    private String profession;
    private String category;
    private String info;
    private String url;
    private String imageUrl;


    @Override
    public String toString() {
        return "Celebrity{" +
                "name='" + name + '\'' +
                ", netWorthString='" + netWorthString + '\'' +
                ", annualSalaryString='" + annualSalaryString + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", profession='" + profession + '\'' +
                ", category='" + category + '\'' +
                ", info='" + info + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetWorthString() {
        return netWorthString;
    }

    public void setNetWorthString(String netWorthString) {
        this.netWorthString = netWorthString;
    }

    public String getAnnualSalaryString() {
        return annualSalaryString;
    }

    public void setAnnualSalaryString(String annualSalaryString) {
        this.annualSalaryString = annualSalaryString;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Celebrity(String name, String netWorthString, String annualSalaryString, String dateOfBirth, String placeOfBirth, String profession, String category, String info, String url, String imageUrl) {

        this.name = name;
        this.netWorthString = netWorthString;
        this.annualSalaryString = annualSalaryString;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.profession = profession;
        this.category = category;
        this.info = info;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public Celebrity() {


    }
    public Celebrity(Celebrity celebrity) {
        this.name = celebrity.getName();
        this.netWorthString = celebrity.getNetWorthString();
        this.annualSalaryString = celebrity.getAnnualSalaryString();
        this.dateOfBirth = celebrity.getDateOfBirth();
        this.placeOfBirth = celebrity.getPlaceOfBirth();
        this.profession = celebrity.getProfession();
        this.category = celebrity.getCategory();
        this.info = celebrity.getInfo();
        this.url = celebrity.getUrl();
        this.imageUrl = celebrity.getImageUrl();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Celebrity celebrity = (Celebrity) o;

        if (!dateOfBirth.equals(celebrity.dateOfBirth)) return false;
        if (!name.equals(celebrity.name)) return false;
        if (!url.equals(celebrity.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
