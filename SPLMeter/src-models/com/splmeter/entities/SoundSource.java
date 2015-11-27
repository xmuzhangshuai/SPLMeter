package com.splmeter.entities;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "SOUND_SOURCE".
 */
public class SoundSource {

    private Integer ssi_id;
    private String ssi_code;
    private String ssi_type;
    private String ssi_item_cn;
    private String ssi_item_en;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public SoundSource() {
    }

    public SoundSource(Integer ssi_id, String ssi_code, String ssi_type, String ssi_item_cn, String ssi_item_en) {
        this.ssi_id = ssi_id;
        this.ssi_code = ssi_code;
        this.ssi_type = ssi_type;
        this.ssi_item_cn = ssi_item_cn;
        this.ssi_item_en = ssi_item_en;
    }

    public Integer getSsi_id() {
        return ssi_id;
    }

    public void setSsi_id(Integer ssi_id) {
        this.ssi_id = ssi_id;
    }

    public String getSsi_code() {
        return ssi_code;
    }

    public void setSsi_code(String ssi_code) {
        this.ssi_code = ssi_code;
    }

    public String getSsi_type() {
        return ssi_type;
    }

    public void setSsi_type(String ssi_type) {
        this.ssi_type = ssi_type;
    }

    public String getSsi_item_cn() {
        return ssi_item_cn;
    }

    public void setSsi_item_cn(String ssi_item_cn) {
        this.ssi_item_cn = ssi_item_cn;
    }

    public String getSsi_item_en() {
        return ssi_item_en;
    }

    public void setSsi_item_en(String ssi_item_en) {
        this.ssi_item_en = ssi_item_en;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
