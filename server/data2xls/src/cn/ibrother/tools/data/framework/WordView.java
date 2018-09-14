package cn.ibrother.tools.data.framework;

/**
 * 字典表结构通用类
 * 2008-4-23 by ken
 */
public class WordView implements Comparable<WordView> {
    //ID
    private Integer id;
    //中文描述
    private String descCn;
    //英文描述
    private String descEn;

    public WordView() {
    	id = -1;
    	descCn = "";
    	descEn = "";
    }
    
    public WordView(Integer id, String desc, String descEn) {
    	this.id = id;
    	descCn = desc;
    	this.descEn = descEn;
    }
    
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getDescCn() {
        return descCn;
    }

    public void setDescCn(String descCn) {
        this.descCn = descCn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getDescEn() {
        return descEn;
    }

	@Override
	public int compareTo(WordView o) {
		return descCn.compareTo(o.descCn);
	}
}
