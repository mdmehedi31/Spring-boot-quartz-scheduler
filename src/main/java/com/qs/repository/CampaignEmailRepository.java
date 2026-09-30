package com.qs.repository;

import com.qs.entity.CampaignEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignEmailRepository extends JpaRepository<CampaignEmailEntity,Integer> {

    List<CampaignEmailEntity> findAllByCampId(Integer campaignId);
}
