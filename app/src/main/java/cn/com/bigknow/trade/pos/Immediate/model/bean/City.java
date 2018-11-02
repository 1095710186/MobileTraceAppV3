package cn.com.bigknow.trade.pos.Immediate.model.bean;


import cn.com.bigknow.trade.pos.Immediate.app.util.PinyinUtils;

public class City implements Comparable<City> {

    public static String ID = "ID";
    public static String NAME = "Name";
    public static String PARENTID = "ParentId";
    public static String LEVELTYPE = "LevelType";
    public static String MERGERNAME = "MergerName";
    public static String PINYIN = "Pinyin";
    public static String SHORTNAME = "ShortName";


    private Long id;
    private String name;
    private Long parentId;
    private int levelType;
    private String mergerName;
    private String pinyin;
    private String shortName;

    public City(String name) {
        super();
        this.name = name;
        this.pinyin = PinyinUtils.getPinyin(name);
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        City.ID = ID;
    }

    public static String getNAME() {
        return NAME;
    }

    public static void setNAME(String NAME) {
        City.NAME = NAME;
    }

    public static String getPARENTID() {
        return PARENTID;
    }

    public static void setPARENTID(String PARENTID) {
        City.PARENTID = PARENTID;
    }

    public static String getLEVELTYPE() {
        return LEVELTYPE;
    }

    public static void setLEVELTYPE(String LEVELTYPE) {
        City.LEVELTYPE = LEVELTYPE;
    }

    public static String getMERGERNAME() {
        return MERGERNAME;
    }

    public static void setMERGERNAME(String MERGERNAME) {
        City.MERGERNAME = MERGERNAME;
    }

    public static String getPINYIN() {
        return PINYIN;
    }

    public static void setPINYIN(String PINYIN) {
        City.PINYIN = PINYIN;
    }

    public static String getSHORTNAME() {
        return SHORTNAME;
    }

    public static void setSHORTNAME(String SHORTNAME) {
        City.SHORTNAME = SHORTNAME;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int compareTo(City another) {
        return this.pinyin.compareTo(another.getPinyin());
    }

}
