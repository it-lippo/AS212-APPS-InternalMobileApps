package com.itams.visionet.customerserviceapps.model;

import java.util.List;

/**
 * Created by alde.asprilla on 23/11/2016.
 */

public class   priorityPassModel {
    private String PPNo;
    private int batchSeq;
    private String psCode;
    private String psName;
    private String memberCode;
    private String memberName;
    private String scmCode;
    private String scmName;
    private String IDNo;
    private String NPWP;
    private String PPStatus;
    private String PPStatusName;
    private String DealingTime;
    private String RegisterTime;
    private String PPPrice;
    private String ProjectCodeBooked;
    private String ClusterCodeBooked;
    private String UnitCodeBooked;
    private String UnitNoBooked;
    private String KTPImgUrl;
    private String NPWPImgUrl;



    private int allowedPrefUnit;
    private List<String> RelatedPP;

    /*public priorityPassModel(priorityPassRealmObject obj){
        this.PPNo = obj.getPPNo();
        this.batchSeq = obj.getBatchSeq();
        this.psCode = obj.getPsCode();
        this.psName = obj.getPsName();
        this.memberCode = obj.getMemberCode();
        this.memberName = obj.getMemberName();
        this.scmCode = obj.getScmCode();
        this.scmName = obj.getScmName();
        this.IDNo = obj.getIDNo();
        this.NPWP = obj.getNPWP();
        this.PPStatus = obj.getPPStatus();
        this.PPStatusName = obj.getPPStatusName();
        this.DealingTime = obj.getDealingTime();
        this.RegisterTime = obj.getRegisterTime();
        this.PPPrice = obj.getPPPrice();
        this.ProjectCodeBooked = obj.getProjectCodeBooked();
        this.ClusterCodeBooked = obj.getClusterCodeBooked();
        this.UnitCodeBooked = obj.getUnitCodeBooked();
        this.UnitNoBooked = obj.getUnitNoBooked();
        this.KTPImgUrl = obj.getKTPImgUrl();
        this.NPWPImgUrl = obj.getNPWPImgUrl();
        this.RelatedPP = obj.getRelatedPP();
    }*/

    public priorityPassModel(){}

    public String getPPNo() {
        return PPNo;
    }

    public void setPPNo(String PPNo) {
        this.PPNo = PPNo;
    }

    public int getBatchSeq() {
        return batchSeq;
    }

    public void setBatchSeq(int batchSeq) {
        this.batchSeq = batchSeq;
    }

    public String getPsCode() {
        return psCode;
    }

    public void setPsCode(String psCode) {
        this.psCode = psCode;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getScmCode() {
        return scmCode;
    }

    public void setScmCode(String scmCode) {
        this.scmCode = scmCode;
    }

    public String getScmName() {
        return scmName;
    }

    public void setScmName(String scmName) {
        this.scmName = scmName;
    }

    public String getIDNo() {
        return IDNo;
    }

    public void setIDNo(String IDNo) {
        this.IDNo = IDNo;
    }

    public String getNPWP() {
        return NPWP;
    }

    public void setNPWP(String NPWP) {
        this.NPWP = NPWP;
    }

    public String getPPStatus() {
        return PPStatus;
    }

    public void setPPStatus(String PPStatus) {
        this.PPStatus = PPStatus;
    }

    public String getPPStatusName() {
        return PPStatusName;
    }

    public void setPPStatusName(String PPStatusName) {
        this.PPStatusName = PPStatusName;
    }

    public String getDealingTime() {
        return DealingTime;
    }

    public void setDealingTime(String dealingTime) {
        DealingTime = dealingTime;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }

    public String getPPPrice() {
        return PPPrice;
    }

    public void setPPPrice(String PPPrice) {
        this.PPPrice = PPPrice;
    }

    public String getProjectCodeBooked() {
        return ProjectCodeBooked;
    }

    public void setProjectCodeBooked(String projectCodeBooked) {
        ProjectCodeBooked = projectCodeBooked;
    }

    public String getClusterCodeBooked() {
        return ClusterCodeBooked;
    }

    public void setClusterCodeBooked(String clusterCodeBooked) {
        ClusterCodeBooked = clusterCodeBooked;
    }

    public String getUnitCodeBooked() {
        return UnitCodeBooked;
    }

    public void setUnitCodeBooked(String unitCodeBooked) {
        UnitCodeBooked = unitCodeBooked;
    }

    public String getUnitNoBooked() {
        return UnitNoBooked;
    }

    public void setUnitNoBooked(String unitNoBooked) {
        UnitNoBooked = unitNoBooked;
    }

    public String getKTPImgUrl() {
        return KTPImgUrl;
    }

    public void setKTPImgUrl(String KTPImgUrl) {
        this.KTPImgUrl = KTPImgUrl;
    }

    public String getNPWPImgUrl() {
        return NPWPImgUrl;
    }

    public void setNPWPImgUrl(String NPWPImgUrl) {
        this.NPWPImgUrl = NPWPImgUrl;
    }

    public List<String> getRelatedPP() {
        return RelatedPP;
    }

    public void setRelatedPP(List<String> relatedPP) {
        RelatedPP = relatedPP;
    }

    public int getAllowedPrefUnit() {
        return allowedPrefUnit;
    }

    public void setAllowedPrefUnit(int allowedPrefUnit) {
        this.allowedPrefUnit = allowedPrefUnit;
    }

}
