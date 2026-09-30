package com.qs.dto;

import java.util.List;

public class CampaignDTO {

    public Integer id;
    public String name;
    public List<CampaignEmailDTO> emails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CampaignEmailDTO> getEmails() {
        return emails;
    }

    public void setEmails(List<CampaignEmailDTO> emails) {
        this.emails = emails;
    }
}
