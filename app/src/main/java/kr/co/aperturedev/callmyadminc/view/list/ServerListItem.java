package kr.co.aperturedev.callmyadminc.view.list;

/**
 * Created by jckim on 2017-10-29.
 */

public class ServerListItem {

    private String svName;
    private String svAdmin;
    private String svPeople;

    public void setSvName(String svName) {
        this.svName = svName;
    }

    public void setSvAdmin(String svAdmin) {
        this.svAdmin = svAdmin;
    }

    public void setSvPeople(String svPeople) {
        this.svPeople = svPeople;
    }

    public String getSvName() {
        return this.svName;
    }

    public String getSvAdmin() {
        return this.svAdmin;
    }

    public String getSvPeople() {
        return this.svPeople;
    }
}
